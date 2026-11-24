package geradorArquivos;

import dominio.ComparadorParada;
import dominio.Conexao;
import dominio.LinhaOnibus;
import dominio.Parada;
import listaEncadeada.ListaEncadeada;
import listaEncadeada.No;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Locale;
import java.util.Random;

/**
 * Gera uma massa de dados realista em arquivo .txt para testar os algoritmos
 * de roteamento (Dijkstra e BFS) do sistema de transporte público.
 */
public class GeradorArquivosSistemaTransporte {

    private static final int NUM_PARADAS = 150;
    private static final int NUM_LINHAS = 25;
    private static final int PARADAS_POR_LINHA_MIN = 6;
    private static final int PARADAS_POR_LINHA_MAX = 15;
    private static final String NOME_ARQUIVO = "malhaTransporteTeste.txt";

    private static final String[] TIPOS_PONTO = {
            "Terminal", "Estação", "Ponto", "Praça", "Avenida", "Largo", "Trevo", "Passarela"
    };

    private static final String[] NOMES_LOCAIS = {
            "Central", "das Flores", "Paulista", "da República", "Getúlio Vargas", "Sete de Setembro",
            "Independência", "Beira-Mar", "Vitória", "Jardim Camburi", "Praia do Canto", "Enseada",
            "Laranjeiras", "Campo Grande", "Vila Velha", "Itaparica", "Glória", "São Torquato",
            "Carapina", "Serra Sede", "Jardim da Penha", "Goiabeiras", "Maruípe", "Consolação",
            "Bento Ferreira", "Santa Lúcia", "Ilha do Príncipe", "Santo Antônio", "Tabuazeiro",
            "Universitário", "Hospitalar", "Industrial", "Rodoviária", "Aeroporto", "Porto"
    };

    private static final String[] LOGRADOUROS = {
            "Av. Princesa Isabel", "Av. Nossa Senhora da Penha", "Av. Fernando Ferrari",
            "Av. Dante Michelini", "Rua Sete de Setembro", "Av. Vitória", "Av. Jerônimo Monteiro",
            "Av. Leitão da Silva", "Av. César Hilal", "Av. Marechal Campos", "Rod. Norte-Sul",
            "Av. Expedito Garcia", "Av. Champagnat", "Av. Carlos Lindenberg", "Rua Aleixo Netto"
    };

    private static final String[] NOMES_LINHAS = {
            "Expresso Metropolitano", "Circular Centro", "Troncal Norte-Sul", "Alimentador Praia",
            "Interbairros Leste", "Corredor Universitário", "Linha Verde", "Expresso Terminal",
            "Via Beira-Mar", "Seletivo Executivo", "Noturno Central", "Integrador Oeste"
    };

    public static void main(String[] args) {
        gerarArquivo();
    }

    private static void gerarArquivo() {
        Random random = new Random();

        // Mantém as paradas ordenadas por ID utilizando o ComparadorParada
        ListaEncadeada<Parada> paradas = new ListaEncadeada<>(new ComparadorParada(), true);
        ListaEncadeada<LinhaOnibus> linhas = new ListaEncadeada<>();

        // Ordena as conexões pela parada de origem para facilitar a leitura do arquivo
        Comparator<Conexao> comparadorConexao = new Comparator<Conexao>() {
            @Override
            public int compare(Conexao c1, Conexao c2) {
                int cmpOrigem = Integer.compare(c1.getOrigem().getId(), c2.getOrigem().getId());
                if (cmpOrigem != 0) return cmpOrigem;
                return Integer.compare(c1.getDestino().getId(), c2.getDestino().getId());
            }
        };
        ListaEncadeada<Conexao> conexoes = new ListaEncadeada<>(comparadorConexao, true);

        System.out.println("1. Gerando " + NUM_PARADAS + " paradas de ônibus/estações...");
        for (int id = 1; id <= NUM_PARADAS; id++) {
            String nome = gerarNomeParadaAleatorio(random, id);
            String endereco = gerarEnderecoAleatorio(random);
            paradas.adicionar(new Parada(id, nome, endereco));
        }

        System.out.println("2. Gerando " + NUM_LINHAS + " linhas e suas conexões ponderadas...");
        for (int idLinha = 100; idLinha < 100 + NUM_LINHAS; idLinha++) {
            String nomeLinha = NOMES_LINHAS[random.nextInt(NOMES_LINHAS.length)] + " " + idLinha;
            LinhaOnibus linha = new LinhaOnibus(idLinha, nomeLinha);

            int qtdParadasNaLinha = PARADAS_POR_LINHA_MIN
                    + random.nextInt(PARADAS_POR_LINHA_MAX - PARADAS_POR_LINHA_MIN + 1);

            Parada paradaAnterior = null;

            for (int passo = 0; passo < qtdParadasNaLinha; passo++) {
                Parada paradaAtual = sortearParada(paradas, random);

                // Evita que a mesma parada apareça duas vezes seguidas na mesma linha
                if (paradaAtual.equals(paradaAnterior)) {
                    continue;
                }

                linha.adicionarParada(paradaAtual);

                // Cria uma aresta direcionada entre a parada anterior e a atual da linha
                if (paradaAnterior != null) {
                    // Tempo de viagem realista entre 2.0 e 18.0 minutos
                    float tempoMinutos = 2.0f + random.nextInt(17);
                    conexoes.adicionar(new Conexao(paradaAnterior, paradaAtual, tempoMinutos, linha));
                }

                paradaAnterior = paradaAtual;
            }

            linhas.adicionar(linha);
        }

        gravarArquivoTxt(paradas, linhas, conexoes);
    }

    /**
     * Percorre a ListaEncadeada até uma posição sorteada para escolher uma parada aleatória.
     */
    private static Parada sortearParada(ListaEncadeada<Parada> paradas, Random random) {
        int indiceSorteado = random.nextInt(paradas.quantidadeNos());
        No<Parada> cursor = paradas.getPrimeiro();
        for (int i = 0; i < indiceSorteado && cursor != null; i++) {
            cursor = cursor.getProximo();
        }
        return cursor.getValor();
    }

    private static String gerarNomeParadaAleatorio(Random random, int id) {
        String tipo = TIPOS_PONTO[random.nextInt(TIPOS_PONTO.length)];
        String local = NOMES_LOCAIS[random.nextInt(NOMES_LOCAIS.length)];
        return tipo + " " + local + " #" + id;
    }

    private static String gerarEnderecoAleatorio(Random random) {
        String rua = LOGRADOUROS[random.nextInt(LOGRADOUROS.length)];
        int numero = 10 + random.nextInt(2990);
        return rua + ", nº " + numero;
    }

    private static void gravarArquivoTxt(ListaEncadeada<Parada> paradas,
                                         ListaEncadeada<LinhaOnibus> linhas,
                                         ListaEncadeada<Conexao> conexoes) {
        System.out.println("3. Gravando arquivo: " + NOME_ARQUIVO + "...");

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(NOME_ARQUIVO, StandardCharsets.UTF_8))) {

            // Cabeçalho com as quantidades totais de cada entidade
            writer.write(paradas.quantidadeNos() + ";"
                    + linhas.quantidadeNos() + ";"
                    + conexoes.quantidadeNos() + "\n");

            // Seção 1: Paradas (id;nome;endereco)
            writer.write("[PARADAS]\n");
            No<Parada> noParada = paradas.getPrimeiro();
            while (noParada != null) {
                Parada p = noParada.getValor();
                writer.write(p.getId() + ";" + p.getNome() + ";" + p.getEndereco() + "\n");
                noParada = noParada.getProximo();
            }

            // Seção 2: Linhas de Ônibus (id;nome)
            writer.write("[LINHAS]\n");
            No<LinhaOnibus> noLinha = linhas.getPrimeiro();
            while (noLinha != null) {
                LinhaOnibus l = noLinha.getValor();
                writer.write(l.getId() + ";" + l.getNome() + "\n");
                noLinha = noLinha.getProximo();
            }

            // Seção 3: Conexões / Arestas (idOrigem;idDestino;tempoMinutos;idLinha)
            writer.write("[CONEXOES]\n");
            No<Conexao> noConexao = conexoes.getPrimeiro();
            while (noConexao != null) {
                Conexao c = noConexao.getValor();
                String tempoFormatado = String.format(Locale.US, "%.1f", c.getTempoMinutos());
                writer.write(c.getOrigem().getId() + ";"
                        + c.getDestino().getId() + ";"
                        + tempoFormatado + ";"
                        + c.getLinha().getId() + "\n");
                noConexao = noConexao.getProximo();
            }

            System.out.println("Arquivo gerado com sucesso! ("
                    + paradas.quantidadeNos() + " paradas, "
                    + linhas.quantidadeNos() + " linhas, "
                    + conexoes.quantidadeNos() + " conexões)");

        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }
}
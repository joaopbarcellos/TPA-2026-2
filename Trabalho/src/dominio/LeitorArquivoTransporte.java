package dominio;

import listaEncadeada.ListaEncadeada;
import listaEncadeada.No;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Lê o arquivo .txt gerado pelo GeradorArquivosTransporte e preenche
 * o SistemaTransporte (paradas, linhas, conexões e o Grafo da malha viária).
 */
public class LeitorArquivoTransporte {
    private static final String NOME_ARQUIVO_PADRAO = "malhaTransporteTeste.txt";

    public SistemaTransporte lerArquivo() {
        return lerArquivo(NOME_ARQUIVO_PADRAO);
    }

    public SistemaTransporte lerArquivo(String caminhoArquivo) {
        long inicio = System.nanoTime();
        SistemaTransporte sistema = new SistemaTransporte();

        try (BufferedReader reader = Files.newBufferedReader(Path.of(caminhoArquivo), StandardCharsets.UTF_8)) {
            // Lê o cabeçalho com os totais esperados (paradas;linhas;conexoes)
            String cabecalho = reader.readLine();
            if (cabecalho != null && !cabecalho.trim().isEmpty()) {
                String[] totais = cabecalho.trim().split(";");
                if (totais.length == 3) {
                    System.out.println("Cabeçalho do arquivo -> Paradas: " + totais[0]
                            + " | Linhas: " + totais[1]
                            + " | Conexões: " + totais[2]);
                }
            }

            String secaoAtual = "";
            String linha;

            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) {
                    continue;
                }

                // Identifica a mudança de seção no arquivo
                if (linha.equals("[PARADAS]") || linha.equals("[LINHAS]") || linha.equals("[CONEXOES]")) {
                    secaoAtual = linha;
                    continue;
                }

                switch (secaoAtual) {
                    case "[PARADAS]":
                        processarLinhaParada(linha, sistema);
                        break;
                    case "[LINHAS]":
                        processarLinhaOnibus(linha, sistema);
                        break;
                    case "[CONEXOES]":
                        processarLinhaConexao(linha, sistema);
                        break;
                    default:
                        System.err.println("Linha fora de seção reconhecida: " + linha);
                }
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        long tempo = System.nanoTime() - inicio;
        double tempoEmSegundos = tempo / 1_000_000_000.0;
        double tempoEmMilissegundos = tempo / 1_000_000.0;

        System.out.println("Tempo de leitura e construção do grafo: "
                + String.format("%.6f s | %.3f ms", tempoEmSegundos, tempoEmMilissegundos));

        return sistema;
    }

    /**
     * Formato esperado: id;nome;endereco
     */
    private void processarLinhaParada(String linha, SistemaTransporte sistema) {
        String[] partes = linha.split(";");
        if (partes.length != 3) {
            System.err.println("Linha de parada inválida: " + linha);
            return;
        }

        try {
            int id = Integer.parseInt(partes[0].trim());
            String nome = partes[1].trim();
            String endereco = partes[2].trim();

            sistema.adicionarParada(new Parada(id, nome, endereco));
        } catch (NumberFormatException e) {
            System.err.println("ID de parada inválido na linha: " + linha);
        }
    }

    /**
     * Formato esperado: id;nome
     */
    private void processarLinhaOnibus(String linha, SistemaTransporte sistema) {
        String[] partes = linha.split(";");
        if (partes.length != 2) {
            System.err.println("Linha de ônibus inválida: " + linha);
            return;
        }

        try {
            int id = Integer.parseInt(partes[0].trim());
            String nome = partes[1].trim();

            sistema.adicionarLinha(new LinhaOnibus(id, nome));
        } catch (NumberFormatException e) {
            System.err.println("ID de linha inválido na linha: " + linha);
        }
    }

    /**
     * Formato esperado: idOrigem;idDestino;tempoMinutos;idLinha
     * Vincula os objetos já carregados, adiciona a conexão no sistema e cria a aresta no grafo.
     */
    private void processarLinhaConexao(String linha, SistemaTransporte sistema) {
        String[] partes = linha.split(";");
        if (partes.length != 4) {
            System.err.println("Linha de conexão inválida: " + linha);
            return;
        }

        try {
            int idOrigem = Integer.parseInt(partes[0].trim());
            int idDestino = Integer.parseInt(partes[1].trim());
            float tempoMinutos = Float.parseFloat(partes[2].trim());
            int idLinha = Integer.parseInt(partes[3].trim());

            // Busca as instâncias já cadastradas na ListaEncadeada do sistema
            Parada origem = buscarParadaPorId(sistema.getParadas(), idOrigem);
            Parada destino = buscarParadaPorId(sistema.getParadas(), idDestino);
            LinhaOnibus linhaOnibus = buscarLinhaPorId(sistema.getLinhas(), idLinha);

            if (origem == null || destino == null || linhaOnibus == null) {
                System.err.println("Referência não encontrada para a conexão: " + linha);
                return;
            }

            // Também registra as paradas no itinerário da linha de ônibus
            if (linhaOnibus.getParadas().pesquisar(origem) == null) {
                linhaOnibus.adicionarParada(origem);
            }
            if (linhaOnibus.getParadas().pesquisar(destino) == null) {
                linhaOnibus.adicionarParada(destino);
            }

            // Adiciona a conexão no sistema e insere a aresta ponderada no Grafo
            Conexao conexao = new Conexao(origem, destino, tempoMinutos, linhaOnibus);
            sistema.adicionarConexao(conexao);

        } catch (NumberFormatException e) {
            System.err.println("Valor numérico inválido na conexão: " + linha);
        }
    }

    /**
     * Utiliza a pesquisa otimizada pelo ComparadorParada na ListaEncadeada ordenada.
     */
    private Parada buscarParadaPorId(ListaEncadeada<Parada> paradas, int id) {
        return paradas.pesquisar(new Parada(id, "", ""));
    }

    /**
     * Percorre a ListaEncadeada de linhas para encontrar a linha pelo seu ID.
     */
    private LinhaOnibus buscarLinhaPorId(ListaEncadeada<LinhaOnibus> linhas, int id) {
        No<LinhaOnibus> cursor = linhas.getPrimeiro();
        while (cursor != null) {
            if (cursor.getValor().getId() == id) {
                return cursor.getValor();
            }
            cursor = cursor.getProximo();
        }
        return null;
    }
}
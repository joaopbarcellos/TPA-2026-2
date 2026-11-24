package servico;

import dominio.Conexao;
import dominio.LeitorArquivoTransporte;
import dominio.LinhaOnibus;
import dominio.Parada;
import dominio.SistemaTransporte;
import grafo.Grafo;
import grafo.ResultadoDijkstra;
import listaEncadeada.ListaEncadeada;
import listaEncadeada.No;

/**
 * Camada de Serviço (Regras de Negócio).
 * Orquestra o carregamento de dados, inserção de novos nós/arestas
 * e execução dos algoritmos de busca de rotas (Dijkstra e BFS).
 */
public class TransporteServico {
    private SistemaTransporte sistema;
    private LeitorArquivoTransporte leitor;

    public TransporteServico() {
        this.sistema = new SistemaTransporte();
        this.leitor = new LeitorArquivoTransporte();
    }

    /**
     * Carrega a malha viária a partir de um arquivo .txt.
     */
    public void carregarDadosDoArquivo(String caminhoArquivo) {
        this.sistema = leitor.lerArquivo(caminhoArquivo);
    }

    /**
     * Adiciona um novo vértice (Parada) ao sistema e ao grafo.
     */
    public boolean cadastrarParada(int id, String nome, String endereco) {
        if (buscarParadaPorId(id) != null) {
            return false; // ID já cadastrado
        }
        Parada novaParada = new Parada(id, nome, endereco);
        sistema.adicionarParada(novaParada);
        return true;
    }

    /**
     * Adiciona uma nova aresta ponderada (Conexão) entre duas paradas existentes.
     */
    public boolean cadastrarConexao(int idOrigem, int idDestino, float tempoMinutos, int idLinha, String nomeLinha) {
        Parada origem = buscarParadaPorId(idOrigem);
        Parada destino = buscarParadaPorId(idDestino);

        if (origem == null || destino == null || tempoMinutos <= 0) {
            return false;
        }

        LinhaOnibus linha = buscarOuCriarLinha(idLinha, nomeLinha);
        if (linha.getParadas().pesquisar(origem) == null) {
            linha.adicionarParada(origem);
        }
        if (linha.getParadas().pesquisar(destino) == null) {
            linha.adicionarParada(destino);
        }

        Conexao novaConexao = new Conexao(origem, destino, tempoMinutos, linha);
        sistema.adicionarConexao(novaConexao);
        return true;
    }

    /**
     * Resolve o problema de menor tempo utilizando Dijkstra e formata o relatório.
     */
    public String calcularRotaMaisRapida(int idOrigem, int idDestino) {
        Parada origem = buscarParadaPorId(idOrigem);
        Parada destino = buscarParadaPorId(idDestino);

        if (origem == null || destino == null) {
            return "Erro: Parada de origem ou destino não encontrada.";
        }

        ListaEncadeada<Parada> caminho = sistema.buscarPercursoMaisRapido(origem, destino);
        if (caminho.estaVazia()) {
            return "Não existe percurso disponível entre " + origem.getNome() + " e " + destino.getNome() + ".";
        }

        // Recupera o tempo total calculado pelo Dijkstra para o destino
        float tempoTotal = obterTempoTotalDijkstra(origem, destino);

        StringBuilder sb = new StringBuilder();
        sb.append("=== PERCURSO DE MENOR TEMPO (DIJKSTRA) ===\n");
        sb.append("Origem : ").append(origem).append("\n");
        sb.append("Destino: ").append(destino).append("\n");
        sb.append(String.format("Tempo Total Estimado: %.1f minutos\n", tempoTotal));
        sb.append("Total de Paradas: ").append(caminho.quantidadeNos()).append("\n\n");
        sb.append("Itinerário:\n");

        No<Parada> cursor = caminho.getPrimeiro();
        int passo = 1;
        while (cursor != null) {
            sb.append("  ").append(passo++).append(". ").append(cursor.getValor()).append("\n");
            cursor = cursor.getProximo();
        }
        return sb.toString();
    }

    /**
     * Resolve o problema de menor número de paradas/conexões utilizando BFS.
     */
    public String calcularRotaMenosConexoes(int idOrigem, int idDestino) {
        Parada origem = buscarParadaPorId(idOrigem);
        Parada destino = buscarParadaPorId(idDestino);

        if (origem == null || destino == null) {
            return "Erro: Parada de origem ou destino não encontrada.";
        }

        ListaEncadeada<Parada> caminho = sistema.buscarPercursoMenosParadas(origem, destino);
        if (caminho.estaVazia()) {
            return "Não existe percurso disponível entre " + origem.getNome() + " e " + destino.getNome() + ".";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== PERCURSO COM MENOR NÚMERO DE CONEXÕES (BFS) ===\n");
        sb.append("Origem : ").append(origem).append("\n");
        sb.append("Destino: ").append(destino).append("\n");
        sb.append("Conexões (trechos): ").append(caminho.quantidadeNos() - 1).append("\n");
        sb.append("Total de Paradas: ").append(caminho.quantidadeNos()).append("\n\n");
        sb.append("Itinerário:\n");

        No<Parada> cursor = caminho.getPrimeiro();
        int passo = 1;
        while (cursor != null) {
            sb.append("  ").append(passo++).append(". ").append(cursor.getValor()).append("\n");
            cursor = cursor.getProximo();
        }
        return sb.toString();
    }

    public String gerarResumoMalhaViaria() {
        Grafo<Parada> grafo = sistema.getMalhaViaria();
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTRUTURA ATUAL DO GRAFO (LISTA DE ADJACÊNCIAS) ===\n");
        sb.append("Total de Vértices (Paradas): ").append(sistema.getParadas().quantidadeNos()).append("\n");
        sb.append("Total de Arestas (Conexões): ").append(sistema.getConexoes().quantidadeNos()).append("\n");
        sb.append("Total de Linhas de Ônibus  : ").append(sistema.getLinhas().quantidadeNos()).append("\n");
        sb.append("------------------------------------------------------\n");

        No<grafo.Vertice<Parada>> cursorVertice = grafo.getVertices().getPrimeiro();
        while (cursorVertice != null) {
            Parada p = cursorVertice.getValor().getValor();
            sb.append("[").append(p.getId()).append("] ").append(p.getNome()).append(" -> ");

            No<grafo.Aresta<Parada>> aresta = cursorVertice.getValor().getDestinos().getPrimeiro();
            if (aresta == null) {
                sb.append("(sem saídas)");
            }
            while (aresta != null) {
                Parada dest = aresta.getValor().getDestino().getValor();
                sb.append("[").append(dest.getId()).append(" - ")
                        .append(dest.getNome()).append(" (")
                        .append(aresta.getValor().getPeso()).append(" min)] ");
                aresta = aresta.getProximo();
            }
            sb.append("\n");
            cursorVertice = cursorVertice.getProximo();
        }
        return sb.toString();
    }

    public Parada buscarParadaPorId(int id) {
        return sistema.getParadas().pesquisar(new Parada(id, "", ""));
    }

    public ListaEncadeada<Parada> listarParadas() {
        return sistema.getParadas();
    }

    private LinhaOnibus buscarOuCriarLinha(int idLinha, String nomeLinha) {
        No<LinhaOnibus> cursor = sistema.getLinhas().getPrimeiro();
        while (cursor != null) {
            if (cursor.getValor().getId() == idLinha) {
                return cursor.getValor();
            }
            cursor = cursor.getProximo();
        }
        String nomeFinal = (nomeLinha == null || nomeLinha.trim().isEmpty())
                ? "Linha " + idLinha
                : nomeLinha.trim();
        LinhaOnibus nova = new LinhaOnibus(idLinha, nomeFinal);
        sistema.adicionarLinha(nova);
        return nova;
    }

    private float obterTempoTotalDijkstra(Parada origem, Parada destino) {
        ListaEncadeada<ResultadoDijkstra<Parada>> resultados = sistema.getMalhaViaria().dijkstra(origem);
        No<ResultadoDijkstra<Parada>> cursor = resultados.getPrimeiro();
        while (cursor != null) {
            if (cursor.getValor().getVertice().getValor().equals(destino)) {
                return cursor.getValor().getDistancia();
            }
            cursor = cursor.getProximo();
        }
        return -1f;
    }
}
package dominio;

import grafo.Grafo;
import listaEncadeada.ListaEncadeada;

/**
 * Fachada principal do sistema de mobilidade urbana.
 * Gerencia paradas, linhas, conexões e consulta rotas otimizadas usando o Grafo.
 */
public class SistemaTransporte {
    private ListaEncadeada<Parada> paradas;
    private ListaEncadeada<LinhaOnibus> linhas;
    private ListaEncadeada<Conexao> conexoes;
    private Grafo<Parada> malhaViaria;

    public SistemaTransporte() {
        // Utiliza o ComparadorParada para manter o cadastro de paradas ordenado por ID
        this.paradas = new ListaEncadeada<>(new ComparadorParada(), true);
        this.linhas = new ListaEncadeada<>();
        this.conexoes = new ListaEncadeada<>();
        this.malhaViaria = new Grafo<>();
    }

    public void adicionarParada(Parada parada) {
        if (paradas.pesquisar(parada) == null) {
            paradas.adicionar(parada);
            malhaViaria.adicionarVertice(parada);
        }
    }

    public void adicionarLinha(LinhaOnibus linha) {
        linhas.adicionar(linha);
    }

    public void adicionarConexao(Conexao conexao) {
        conexoes.adicionar(conexao);
        // Garante que origem e destino existam como vértices no grafo
        adicionarParada(conexao.getOrigem());
        adicionarParada(conexao.getDestino());
        malhaViaria.adicionarAresta(
                conexao.getOrigem(),
                conexao.getDestino(),
                conexao.getTempoMinutos()
        );
    }

    /**
     * Calcula o percurso de menor tempo total entre duas paradas (Dijkstra).
     */
    public ListaEncadeada<Parada> buscarPercursoMaisRapido(Parada origem, Parada destino) {
        return malhaViaria.caminhoMaisRapido(origem, destino);
    }

    /**
     * Calcula o percurso passando pelo menor número de paradas/conexões (BFS).
     */
    public ListaEncadeada<Parada> buscarPercursoMenosParadas(Parada origem, Parada destino) {
        return malhaViaria.caminhoMenorNumeroConexoes(origem, destino);
    }

    public ListaEncadeada<Parada> getParadas() {
        return paradas;
    }

    public ListaEncadeada<LinhaOnibus> getLinhas() {
        return linhas;
    }

    public ListaEncadeada<Conexao> getConexoes() {
        return conexoes;
    }

    public Grafo<Parada> getMalhaViaria() {
        return malhaViaria;
    }
}
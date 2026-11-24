package grafo;

import java.util.Comparator;
import listaEncadeada.ListaEncadeada;
import listaEncadeada.No;

/**
 * Grafo direcionado e ponderado implementado com listas de adjacências.
 */
public class Grafo<T> {
    private ListaEncadeada<Vertice<T>> vertices;

    public Grafo() {
        this.vertices = new ListaEncadeada<>();
    }

    public Vertice<T> adicionarVertice(T valor) {
        Vertice<T> existente = getVertice(valor);
        if (existente != null) {
            return existente;
        }

        Vertice<T> novoVertice = new Vertice<>(valor);
        vertices.adicionar(novoVertice);
        return novoVertice;
    }

    public boolean adicionarAresta(T origem, T destino, float peso) {
        Vertice<T> verticeOrigem = getVertice(origem);
        Vertice<T> verticeDestino = getVertice(destino);
        if (verticeOrigem == null || verticeDestino == null) {
            return false;
        }

        verticeOrigem.adicionarDestino(verticeDestino, peso);
        return true;
    }

    public Vertice<T> getVertice(T valor) {
        No<Vertice<T>> cursor = vertices.getPrimeiro();
        while (cursor != null) {
            T valorAtual = cursor.getValor().getValor();
            if (valorAtual == valor || (valorAtual != null && valorAtual.equals(valor))) {
                return cursor.getValor();
            }
            cursor = cursor.getProximo();
        }
        return null;
    }

    public ListaEncadeada<Vertice<T>> getVertices() {
        return vertices;
    }

    /**
     * Busca em Largura (BFS): percorre o grafo em camadas a partir da origem.
     */
    public ListaEncadeada<Vertice<T>> buscarEmLargura(T valorOrigem) {
        Vertice<T> origem = getVertice(valorOrigem);
        ListaEncadeada<Vertice<T>> visitados = new ListaEncadeada<>();
        if (origem == null) {
            return visitados;
        }

        ListaEncadeada<Vertice<T>> fila = new ListaEncadeada<>();
        fila.adicionar(origem);
        visitados.adicionar(origem);

        while (!fila.estaVazia()) {
            Vertice<T> atual = fila.getPrimeiro().getValor();
            fila.remover(atual);

            No<Aresta<T>> aresta = atual.getDestinos().getPrimeiro();
            while (aresta != null) {
                Vertice<T> vizinho = aresta.getValor().getDestino();
                if (visitados.pesquisar(vizinho) == null) {
                    visitados.adicionar(vizinho);
                    fila.adicionar(vizinho);
                }
                aresta = aresta.getProximo();
            }
        }
        return visitados;
    }

    /**
     * Encontra o caminho com a menor quantidade de paradas/conexões (grafo não ponderado)
     * utilizando Busca em Largura (BFS).
     */
    public ListaEncadeada<T> caminhoMenorNumeroConexoes(T valorOrigem, T valorDestino) {
        ListaEncadeada<T> caminho = new ListaEncadeada<>();
        Vertice<T> origem = getVertice(valorOrigem);
        Vertice<T> destino = getVertice(valorDestino);
        if (origem == null || destino == null) {
            return caminho;
        }

        // Reaproveita ResultadoDijkstra apenas para rastrear o vértice anterior de cada parada
        ListaEncadeada<ResultadoDijkstra<T>> rastreamento = new ListaEncadeada<>();
        ListaEncadeada<Vertice<T>> fila = new ListaEncadeada<>();

        rastreamento.adicionar(new ResultadoDijkstra<>(origem, 0, null));
        fila.adicionar(origem);

        boolean encontrou = false;

        while (!fila.estaVazia() && !encontrou) {
            Vertice<T> atual = fila.getPrimeiro().getValor();
            fila.remover(atual);

            ResultadoDijkstra<T> infoAtual = buscarResultado(rastreamento, atual);
            No<Aresta<T>> aresta = atual.getDestinos().getPrimeiro();

            while (aresta != null) {
                Vertice<T> vizinho = aresta.getValor().getDestino();
                if (buscarResultado(rastreamento, vizinho) == null) {
                    rastreamento.adicionar(new ResultadoDijkstra<>(vizinho, infoAtual.getDistancia() + 1, atual));
                    if (vizinho.equals(destino)) {
                        encontrou = true;
                        break;
                    }
                    fila.adicionar(vizinho);
                }
                aresta = aresta.getProximo();
            }
        }

        if (!encontrou) {
            return caminho;
        }

        // Reconstrói o trajeto inserindo sempre no início da lista encadeada
        Vertice<T> cursor = destino;
        while (cursor != null) {
            caminho.adicionarNoInicio(cursor.getValor());
            ResultadoDijkstra<T> passo = buscarResultado(rastreamento, cursor);
            cursor = (passo != null) ? passo.getAnterior() : null;
        }

        return caminho;
    }

    /**
     * Algoritmo de Dijkstra: calcula o menor tempo de deslocamento da origem
     * até todos os demais vértices alcançáveis.
     */
    public ListaEncadeada<ResultadoDijkstra<T>> dijkstra(T valorOrigem) {
        Vertice<T> origem = getVertice(valorOrigem);
        ListaEncadeada<ResultadoDijkstra<T>> resultados = new ListaEncadeada<>();
        if (origem == null) {
            return resultados;
        }

        // Fila de prioridade improvisada usando ListaEncadeada ordenada pelo Comparator
        Comparator<ResultadoDijkstra<T>> comparador = new ComparadorResultadoDijkstra<>();
        ListaEncadeada<ResultadoDijkstra<T>> pendentes = new ListaEncadeada<>(comparador, true);

        No<Vertice<T>> cursorVertice = vertices.getPrimeiro();
        while (cursorVertice != null) {
            Vertice<T> v = cursorVertice.getValor();
            float distanciaInicial = v.equals(origem) ? 0f : Float.POSITIVE_INFINITY;
            ResultadoDijkstra<T> registro = new ResultadoDijkstra<>(v, distanciaInicial, null);

            resultados.adicionar(registro);
            pendentes.adicionar(registro);
            cursorVertice = cursorVertice.getProximo();
        }

        while (!pendentes.estaVazia()) {
            // Como a lista está ordenada pelo Comparator, o primeiro nó é sempre o de menor tempo
            ResultadoDijkstra<T> atual = pendentes.getPrimeiro().getValor();
            pendentes.remover(atual);

            if (atual.getDistancia() == Float.POSITIVE_INFINITY) {
                break; // Os vértices restantes são inalcançáveis
            }

            No<Aresta<T>> aresta = atual.getVertice().getDestinos().getPrimeiro();
            while (aresta != null) {
                Vertice<T> vizinho = aresta.getValor().getDestino();
                ResultadoDijkstra<T> registroVizinho = buscarResultado(resultados, vizinho);

                // Verifica se o vizinho ainda está na lista de pendentes
                if (registroVizinho != null && pendentes.pesquisar(registroVizinho) != null) {
                    float novoTempo = atual.getDistancia() + aresta.getValor().getPeso();

                    if (novoTempo < registroVizinho.getDistancia()) {
                        // Remove e reinsere para atualizar a posição ordenada na ListaEncadeada
                        pendentes.remover(registroVizinho);
                        registroVizinho.setDistancia(novoTempo);
                        registroVizinho.setAnterior(atual.getVertice());
                        pendentes.adicionar(registroVizinho);
                    }
                }
                aresta = aresta.getProximo();
            }
        }

        return resultados;
    }

    /**
     * Retorna a sequência exata de paradas para o percurso de menor tempo (Dijkstra).
     */
    public ListaEncadeada<T> caminhoMaisRapido(T origem, T destino) {
        ListaEncadeada<T> caminho = new ListaEncadeada<>();
        Vertice<T> verticeDestino = getVertice(destino);
        if (verticeDestino == null) {
            return caminho;
        }

        ListaEncadeada<ResultadoDijkstra<T>> resultados = dijkstra(origem);
        ResultadoDijkstra<T> resultadoDestino = buscarResultado(resultados, verticeDestino);

        if (resultadoDestino == null || resultadoDestino.getDistancia() == Float.POSITIVE_INFINITY) {
            return caminho;
        }

        // Reconstrói o caminho de trás para frente inserindo no início da ListaEncadeada
        Vertice<T> cursor = resultadoDestino.getVertice();
        while (cursor != null) {
            caminho.adicionarNoInicio(cursor.getValor());
            ResultadoDijkstra<T> passo = buscarResultado(resultados, cursor);
            cursor = (passo != null) ? passo.getAnterior() : null;
        }

        return caminho;
    }

    private ResultadoDijkstra<T> buscarResultado(ListaEncadeada<ResultadoDijkstra<T>> lista, Vertice<T> alvo) {
        No<ResultadoDijkstra<T>> cursor = lista.getPrimeiro();
        while (cursor != null) {
            if (cursor.getValor().getVertice().equals(alvo)) {
                return cursor.getValor();
            }
            cursor = cursor.getProximo();
        }
        return null;
    }

    public void imprimirGrafo() {
        No<Vertice<T>> cursor = vertices.getPrimeiro();
        while (cursor != null) {
            System.out.print(cursor.getValor().getValor() + " -> ");
            No<Aresta<T>> aresta = cursor.getValor().getDestinos().getPrimeiro();
            while (aresta != null) {
                System.out.print("[" + aresta.getValor() + "] ");
                aresta = aresta.getProximo();
            }
            System.out.println();
            cursor = cursor.getProximo();
        }
    }
}
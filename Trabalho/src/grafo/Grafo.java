package grafo;

import listaEncadeada.ListaEncadeada;
import listaEncadeada.No;

/** Grafo genérico representado por lista de adjacências. */
public class Grafo<T> {
    private ListaEncadeada<Vertice<T>> vertices;

    public Grafo() {
        this.vertices = new ListaEncadeada<Vertice<T>>();
    }

    public Vertice<T> adicionarVertice(T valor) {
        Vertice<T> existente = obterVertice(valor);
        if (existente != null) return existente;

        Vertice<T> novo = new Vertice<T>(valor);
        vertices.adicionar(novo);
        return novo;
    }

    public boolean adicionarAresta(T origem, T destino, float peso) {
        Vertice<T> verticeOrigem = obterVertice(origem);
        Vertice<T> verticeDestino = obterVertice(destino);
        if (verticeOrigem == null || verticeDestino == null) return false;

        verticeOrigem.adicionarDestino(verticeDestino, peso);
        return true;
    }

    public boolean adicionarAresta(Vertice<T> origem, Vertice<T> destino, float peso) {
        if (origem == null || destino == null) return false;
        origem.adicionarDestino(destino, peso);
        return true;
    }

    private Vertice<T> obterVertice(T valor) {
        No<Vertice<T>> atual = vertices.getPrimeiro();
        while (atual != null) {
            if (atual.getValor().getValor() != null
                    && atual.getValor().getValor().equals(valor)) {
                return atual.getValor();
            }
            if (atual.getValor().getValor() == null && valor == null) {
                return atual.getValor();
            }
            atual = atual.getProximo();
        }
        return null;
    }

    public Vertice<T> getVertice(T valor) {
        return obterVertice(valor);
    }

    public ListaEncadeada<Vertice<T>> getVertices() {
        return vertices;
    }

    /** Caminhamento em largura iniciando na raiz indicada. */
    public ListaEncadeada<Vertice<T>> buscarEmLargura(T valorOrigem) {
        Vertice<T> origem = obterVertice(valorOrigem);
        ListaEncadeada<Vertice<T>> visitados = new ListaEncadeada<Vertice<T>>();
        if (origem == null) return visitados;

        ListaEncadeada<Vertice<T>> fila = new ListaEncadeada<Vertice<T>>();
        fila.adicionar(origem);

        while (fila.quantidadeNos() > 0) {
            Vertice<T> atual = fila.getPrimeiro().getValor();
            fila.remover(atual);

            if (visitados.pesquisar(atual) != null) continue;
            visitados.adicionar(atual);

            No<Aresta<T>> aresta = atual.getDestinos().getPrimeiro();
            while (aresta != null) {
                Vertice<T> destino = aresta.getValor().getDestino();
                if (visitados.pesquisar(destino) == null) fila.adicionar(destino);
                aresta = aresta.getProximo();
            }
        }
        return visitados;
    }

    /** Executa Dijkstra e devolve a distância mínima até cada vértice alcançado. */
    public ListaEncadeada<ResultadoDijkstra<T>> dijkstra(T valorOrigem) {
        Vertice<T> origem = obterVertice(valorOrigem);
        ListaEncadeada<ResultadoDijkstra<T>> resultado = new ListaEncadeada<ResultadoDijkstra<T>>();
        if (origem == null) return resultado;

        No<Vertice<T>> atualVertice = vertices.getPrimeiro();
        while (atualVertice != null) {
            float distancia = atualVertice.getValor().equals(origem)
                    ? 0 : Float.POSITIVE_INFINITY;
            resultado.adicionar(new ResultadoDijkstra<T>(atualVertice.getValor(), distancia, null));
            atualVertice = atualVertice.getProximo();
        }

        ListaEncadeada<Vertice<T>> naoVisitados = new ListaEncadeada<Vertice<T>>();
        atualVertice = vertices.getPrimeiro();
        while (atualVertice != null) {
            naoVisitados.adicionar(atualVertice.getValor());
            atualVertice = atualVertice.getProximo();
        }

        while (naoVisitados.quantidadeNos() > 0) {
            Vertice<T> atual = obterMenorNaoVisitado(naoVisitados, resultado);
            if (atual == null) break;
            naoVisitados.remover(atual);

            ResultadoDijkstra<T> resultadoAtual = buscarResultado(resultado, atual);
            if (resultadoAtual == null || resultadoAtual.getDistancia() == Float.POSITIVE_INFINITY) continue;

            No<Aresta<T>> aresta = atual.getDestinos().getPrimeiro();
            while (aresta != null) {
                Vertice<T> destino = aresta.getValor().getDestino();
                if (naoVisitados.pesquisar(destino) != null) {
                    float novaDistancia = resultadoAtual.getDistancia() + aresta.getValor().getPeso();
                    ResultadoDijkstra<T> resultadoDestino = buscarResultado(resultado, destino);
                    if (novaDistancia < resultadoDestino.getDistancia()) {
                        resultadoDestino.setDistancia(novaDistancia);
                        resultadoDestino.setAnterior(atual);
                    }
                }
                aresta = aresta.getProximo();
            }
        }
        return resultado;
    }

    private ResultadoDijkstra<T> buscarResultado(ListaEncadeada<ResultadoDijkstra<T>> resultado,
                                                 Vertice<T> vertice) {
        No<ResultadoDijkstra<T>> atual = resultado.getPrimeiro();
        while (atual != null) {
            if (atual.getValor().getVertice().equals(vertice)) return atual.getValor();
            atual = atual.getProximo();
        }
        return null;
    }

    private Vertice<T> obterMenorNaoVisitado(ListaEncadeada<Vertice<T>> naoVisitados,
                                             ListaEncadeada<ResultadoDijkstra<T>> resultado) {
        Vertice<T> menor = null;
        float menorDistancia = Float.POSITIVE_INFINITY;
        No<Vertice<T>> atual = naoVisitados.getPrimeiro();
        while (atual != null) {
            ResultadoDijkstra<T> candidato = buscarResultado(resultado, atual.getValor());
            if (candidato != null && candidato.getDistancia() < menorDistancia) {
                menor = atual.getValor();
                menorDistancia = candidato.getDistancia();
            }
            atual = atual.getProximo();
        }
        return menor;
    }

    public void print() {
        No<Vertice<T>> atual = vertices.getPrimeiro();
        while (atual != null) {
            System.out.print(atual.getValor().getValor() + " -> ");
            No<Aresta<T>> aresta = atual.getValor().getDestinos().getPrimeiro();
            while (aresta != null) {
                System.out.print("[" + aresta.getValor() + "] ");
                aresta = aresta.getProximo();
            }
            System.out.println();
            atual = atual.getProximo();
        }
    }

    public ListaEncadeada<T> caminhoMaisCurto(T origem, T destino) {
        ListaEncadeada<T> caminho = new ListaEncadeada<T>();
        Vertice<T> destinoVertice = obterVertice(destino);
        if (destinoVertice == null) return caminho;

        ListaEncadeada<ResultadoDijkstra<T>> resultados = dijkstra(origem);
        ResultadoDijkstra<T> atual = buscarResultado(resultados, destinoVertice);
        if (atual == null || atual.getDistancia() == Float.POSITIVE_INFINITY) return caminho;

        ListaEncadeada<T> caminhoReverso = new ListaEncadeada<T>();
        Vertice<T> cursor = atual.getVertice();
        while (cursor != null) {
            caminhoReverso.adicionar(cursor.getValor());
            ResultadoDijkstra<T> passo = buscarResultado(resultados, cursor);
            cursor = passo == null ? null : passo.getAnterior();
        }

        No<T> noReverso = caminhoReverso.getPrimeiro();
        ListaEncadeada<T> temporario = new ListaEncadeada<T>();
        while (noReverso != null) {
            temporario.adicionar(noReverso.getValor());
            noReverso = noReverso.getProximo();
        }
        No<T> noTemp = temporario.getPrimeiro();
        while (noTemp != null) {
            noTemp = noTemp.getProximo();
        }

        T[] valores = (T[]) new Object[caminhoReverso.quantidadeNos()];
        int indice = 0;
        No<T> no = caminhoReverso.getPrimeiro();
        while (no != null) {
            valores[indice++] = no.getValor();
            no = no.getProximo();
        }
        for (int i = valores.length - 1; i >= 0; i--) caminho.adicionar(valores[i]);
        return caminho;
    }
}

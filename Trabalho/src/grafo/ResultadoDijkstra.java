package grafo;

/**
 * Armazena o estado de cada vértice durante a execução do algoritmo de Dijkstra:
 * o menor tempo acumulado até ele e o vértice anterior na rota.
 */
public class ResultadoDijkstra<T> {
    private Vertice<T> vertice;
    private float distancia;
    private Vertice<T> anterior;

    public ResultadoDijkstra(Vertice<T> vertice, float distancia, Vertice<T> anterior) {
        this.vertice = vertice;
        this.distancia = distancia;
        this.anterior = anterior;
    }

    public Vertice<T> getVertice() {
        return vertice;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public Vertice<T> getAnterior() {
        return anterior;
    }

    public void setAnterior(Vertice<T> anterior) {
        this.anterior = anterior;
    }

    @Override
    public String toString() {
        String tempo = (distancia == Float.POSITIVE_INFINITY) ? "inalcançável" : distancia + " min";
        return vertice.getValor() + " -> " + tempo;
    }
}
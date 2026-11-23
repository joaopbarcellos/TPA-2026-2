package grafo;

/** Resultado do Dijkstra para um vértice específico. */
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
        String textoDistancia = distancia == Float.POSITIVE_INFINITY
                ? "infinito" : String.valueOf(distancia);
        return vertice.getValor() + " = " + textoDistancia;
    }
}

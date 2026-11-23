package grafo;

/** Representa uma conexão direcionada entre dois vértices. */
public class Aresta<T> {
    private Vertice<T> destino;
    private float peso;

    public Aresta(Vertice<T> destino, float peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public Vertice<T> getDestino() {
        return destino;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return destino.getValor() + " (" + peso + " min)";
    }
}

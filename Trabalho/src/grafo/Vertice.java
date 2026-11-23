package grafo;

import listaEncadeada.ListaEncadeada;

/** Vértice genérico que guarda seu valor e a lista de adjacências. */
public class Vertice<T> {
    private T valor;
    private ListaEncadeada<Aresta<T>> destinos;

    public Vertice(T valor) {
        this.valor = valor;
        this.destinos = new ListaEncadeada<>();
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    public ListaEncadeada<Aresta<T>> getDestinos() {
        return destinos;
    }

    public void adicionarDestino(Vertice<T> destino, float peso) {
        destinos.adicionar(new Aresta<T>(destino, peso));
    }

    @Override
    public boolean equals(Object objeto) {
        if (!(objeto instanceof Vertice)) return false;
        Vertice<?> outro = (Vertice<?>) objeto;
        return valor == outro.valor || (valor != null && valor.equals(outro.valor));
    }

    @Override
    public int hashCode() {
        return valor == null ? 0 : valor.hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }
}

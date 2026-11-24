package grafo;

import listaEncadeada.ListaEncadeada;

/**
 * Representa um ponto no grafo (no domínio, uma parada ou estação)
 * e mantém sua lista de adjacências (conexões de saída).
 */
public class Vertice<T> {
    private T valor;
    private ListaEncadeada<Aresta<T>> adjacencias;

    public Vertice(T valor) {
        this.valor = valor;
        this.adjacencias = new ListaEncadeada<>();
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    public ListaEncadeada<Aresta<T>> getDestinos() {
        return adjacencias;
    }

    public void adicionarDestino(Vertice<T> destino, float peso) {
        adjacencias.adicionar(new Aresta<>(destino, peso));
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
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
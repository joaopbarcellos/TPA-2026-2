package listaEncadeada;

public class No<T>  {
    private No<T> proximo;
    private T valor;

    public No(T valor) {
        this.proximo = null;
        this.valor = valor;
    }

    public No<T> getProximo() {
        return proximo;
    }

    public void setProximo(No<T> proximo) {
        this.proximo = proximo;
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }
}

package arvoreBinaria;

public class No<T>  {
    private No<T> direita, esquerda;
    private T valor;

    public No(T valor) {
        this.direita = null;
        this.esquerda = null;
        this.valor = valor;
    }

    public No<T> getDireita() {
        return direita;
    }

    public void setDireita(No<T> direita) {
        this.direita = direita;
    }

    public No<T> getEsquerda() {
        return esquerda;
    }

    public void setEsquerda(No<T> esquerda) {
        this.esquerda = esquerda;
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }
}

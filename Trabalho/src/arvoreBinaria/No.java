package arvoreBinaria;

public class No<T>  {
    private No<T> direita, esquerda;
    private T valor;
    private int fb;

    public No(T valor) {
        this.direita = null;
        this.esquerda = null;
        this.valor = valor;
        this.fb = 0;
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

    public int getFb() {
        return fb;
    }

    public void setFb(int fb) {
        this.fb = fb;
    }

    public int obterAltura() {
        return obterAltura(this);
    }

    private int obterAltura(No<T> no) {
        if (no == null) {
            return -1;
        }

        int hd = obterAltura(no.getDireita());
        int he = obterAltura(no.getEsquerda());

        if (hd > he) {
            return hd + 1;
        }
        return he + 1;
    }

    public int fatorBalanceamento() {
        return obterAltura(this.direita) - obterAltura(this.esquerda);
    }
}

package listaEncadeada;

import java.util.Comparator;
import colecao.IColecao;

public class ListaEncadeada<T> implements IColecao<T> {
    private No<T> primeiro, ultimo;
    private int quantidade;
    private final boolean ordenada;
    private Comparator<T> comparador;

    public ListaEncadeada() {
        this(null, false);
    }

    public ListaEncadeada(Comparator<T> comparador, boolean ordenada) {
        this.primeiro = null;
        this.ultimo = null;
        this.quantidade = 0;
        this.ordenada = ordenada;
        this.comparador = comparador;
    }

    @Override
    public boolean adicionar(T elemento) {
        if (this.ordenada) {
            return inserirElementoOrdenado(elemento);
        }
        return inserirElementoNaoOrdenado(elemento);
    }

    public boolean inserirElementoOrdenado(T elemento) {
        No<T> novo = new No<>(elemento);
        No<T> atual, anterior;
        atual = this.primeiro;
        anterior = null;
        if (this.primeiro == null) {
            this.primeiro = novo;
            this.ultimo = novo;
            this.quantidade++;
            return true;
        }

        while (atual != null && comparador.compare(atual.getValor(), elemento) < 0) {
            anterior = atual;
            atual = atual.getProximo();
        }

        if (anterior == null) {
            novo.setProximo(this.primeiro);
            this.primeiro = novo;
        } else if (atual == null){
            this.ultimo.setProximo(novo);
            this.ultimo = novo;
        } else {
            anterior.setProximo(novo);
            novo.setProximo(atual);
        }
        this.quantidade++;
        return true;
    }

    public boolean inserirElementoNaoOrdenado(T elemento) {
        No<T> novoNo = new No<T>(elemento);
        if (this.primeiro == null) {
            this.primeiro = novoNo;
        } else {
            this.ultimo.setProximo(novoNo);
        }
        this.ultimo = novoNo;
        this.quantidade++;
        return true;
    }

    @Override
    public T pesquisar(T elemento) {
        long inicio = System.nanoTime();
        No<T> aux = this.primeiro;
        while (aux != null) {
            if (comparador.compare(aux.getValor(), elemento) == 0) {
                long tempo = System.nanoTime() - inicio;
                imprimirTempo("Tempo de pesquisa", tempo);
                return aux.getValor();
            }
            aux = aux.getProximo();
        }
        long tempo = System.nanoTime() - inicio;
        imprimirTempo("Tempo de pesquisa", tempo);
        return null;
    }

    @Override
    public boolean remover(T elemento) {
        long inicio = System.nanoTime();
        No<T> aux = this.primeiro;
        No<T> anterior = null;
        while (aux != null) {
            if (comparador.compare(aux.getValor(), elemento) == 0) {
                if (aux == this.primeiro) {
                    this.primeiro = this.primeiro.getProximo();
                    if (aux == this.ultimo) {
                        this.ultimo = null;
                    }
                } else {
                    anterior.setProximo(aux.getProximo());
                    if (aux == this.ultimo) {
                        this.ultimo = anterior;
                    }
                }
                this.quantidade--;
                long tempo = System.nanoTime() - inicio;
                imprimirTempo("Tempo de remoção", tempo);
                return true;
            }
            anterior = aux;
            aux = aux.getProximo();
        }
        long tempo = System.nanoTime() - inicio;
        imprimirTempo("Tempo de remoção", tempo);
        return false;
    }

    private void imprimirTempo(String nome, long tempoNano) {
        double tempoEmSegundos = tempoNano / 1_000_000_000.0;
        double tempoEmMilissegundos = tempoNano / 1_000_000.0;
        System.out.println(nome + ": " + String.format("%.6f s | %.3f ms", tempoEmSegundos, tempoEmMilissegundos));
    }

    @Override
    public int quantidadeNos() {
        return this.quantidade;
    }

    @Override
    public String toString() {
        No aux = this.primeiro;
        String retorno = "[";
        while (aux != null) {
            retorno += aux.getValor();
            if (aux != this.ultimo) retorno += ", ";
            aux = aux.getProximo();
        }
        return retorno + "]";
    }
}

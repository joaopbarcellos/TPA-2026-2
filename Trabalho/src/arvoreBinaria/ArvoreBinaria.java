package arvoreBinaria;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class ArvoreBinaria<T> extends ArvoreBinariaBase<T> {
    private No<T> raiz;

    public ArvoreBinaria(Comparator<T> comparator) {
        super(comparator);
        this.raiz = null;
    }

    public int altura() {
        return altura(this.raiz);
    }

    public String caminharEmNivel() {
        if (this.raiz == null) {
            return "[]";
        }

        StringBuilder resultado = new StringBuilder("[");
        Queue<No<T>> fila = new LinkedList<>();
        fila.add(this.raiz);
        boolean primeiro = true;

        while (!fila.isEmpty()) {
            int quantidadeNoNivel = fila.size();

            if (!primeiro) {
                resultado.append("\n");
            }

                for (int i =0; i < quantidadeNoNivel; i++) {
                    No<T> atual = fila.poll();
                if (i > 0) resultado.append(", ");
                resultado.append(atual.getValor());

                    if (atual.getEsquerda() != null) {
                    fila.add(atual.getEsquerda());
                }
                if (atual.getDireita() != null) {
                    fila.add(atual.getDireita());
                }
            }

            primeiro = false;
        }

        return resultado.append("]").toString();
    }

    public String caminharEmOrdem() {
        StringBuilder resultado = new StringBuilder("[");
        caminharEmOrdem(this.raiz, resultado);
        return resultado.append("]").toString();
    }

    private void caminharEmOrdem(No<T> no, StringBuilder resultado) {
        if (no == null) return;
        caminharEmOrdem(no.getEsquerda(), resultado);
        if (resultado.length() > 1) resultado.append(", ");
        resultado.append(no.getValor());
        caminharEmOrdem(no.getDireita(), resultado);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean adicionar(T novoValor) {
        No<T> novoNo = new No<>(novoValor);
        if (this.raiz == null) {
            this.raiz = novoNo;
            return true;
        }

        No<T> atual = this.raiz;
        while (true) {
            int comparacao = comparador.compare(novoValor, atual.getValor());
            if (comparacao <= 0) {
                if (atual.getEsquerda() == null) {
                    atual.setEsquerda(novoNo);
                    return true;
                }
                atual = atual.getEsquerda();
            } else {
                if (atual.getDireita() == null) {
                    atual.setDireita(novoNo);
                    return true;
                }
                atual = atual.getDireita();
            }
        }
    }

    @Override
    public T pesquisar(T valor) {
        No<T> atual = this.raiz;
        while (atual != null) {
            int comparacao = comparador.compare(valor, atual.getValor());
            if (comparacao == 0) {
                return atual.getValor();
            }
            atual = comparacao < 0 ? atual.getEsquerda() : atual.getDireita();
        }
        return null;
    }

    @Override
    public boolean remover(T valor) {
        if (this.raiz == null) {
            return false;
        }

        No<T> pai = null;
        No<T> atual = this.raiz;

        while (atual != null && comparador.compare(valor, atual.getValor()) != 0) {
            pai = atual;
            if (comparador.compare(valor, atual.getValor()) < 0) {
                atual = atual.getEsquerda();
            } else {
                atual = atual.getDireita();
            }
        }

        if (atual == null) {
            return false;
        }

        if (atual.getEsquerda() != null && atual.getDireita() != null) {
            No<T> paiSucessor = atual;
            No<T> sucessor = atual.getDireita();
            while (sucessor.getEsquerda() != null) {
                paiSucessor = sucessor;
                sucessor = sucessor.getEsquerda();
            }

            atual.setValor(sucessor.getValor());
            pai = paiSucessor;
            atual = sucessor;
        }

        No<T> filho = (atual.getEsquerda() != null) ? atual.getEsquerda() : atual.getDireita();

        if (pai == null) {
            this.raiz = filho;
        } else if (pai.getEsquerda() == atual) {
            pai.setEsquerda(filho);
        } else {
            pai.setDireita(filho);
        }

        return true;
    }

    @Override
    public int quantidadeNos() {
        return quantidadeNos(this.raiz);
    }

    private int altura(No<T> no) {
        if (no == null) {
            return -1;
        }
        return 1 + Math.max(altura(no.getEsquerda()), altura(no.getDireita()));
    }

    private int quantidadeNos(No<T> no) {
        if (no == null) {
            return 0;
        }
        return 1 + quantidadeNos(no.getEsquerda()) + quantidadeNos(no.getDireita());
    }
}

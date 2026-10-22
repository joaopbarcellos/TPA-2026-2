package arvoreBinaria;

import java.util.Comparator;

public class ArvoreAVL<T> extends ArvoreBinaria<T> {
    private No<T> raiz;
    private boolean cresceu;
    private boolean diminuiu;
    private boolean removeu;

    public ArvoreAVL(Comparator<T> comparador) {
        super(comparador);
        this.raiz = null;
    }

    @Override
    public boolean adicionar(T novoValor) {
        this.cresceu = false;
        this.raiz = inserirAVL(this.raiz, novoValor);
        return true;
    }

    private No<T> inserirAVL(No<T> no, T valor) {
        if (no == null) {
            No<T> novo = new No<>(valor);
            novo.setFb(0);
            this.cresceu = true;
            return novo;
        }

        int comparacao = comparador.compare(valor, no.getValor());
        if (comparacao <= 0) {
            no.setEsquerda(inserirAVL(no.getEsquerda(), valor));
            if (this.cresceu) {
                switch (no.getFb()) {
                    case -1:
                        if (no.getEsquerda().getFb() == -1) {
                            no = rotacaoDireita(no);
                        } else {
                            no = rotacaoEsquerdaDireita(no);
                        }
                        this.cresceu = false;
                        break;
                    case 0:
                        no.setFb(-1);
                        this.cresceu = true;
                        break;
                    case 1:
                        no.setFb(0);
                        this.cresceu = false;
                        break;
                    default:
                        break;
                }
            }
        } else {
            no.setDireita(inserirAVL(no.getDireita(), valor));
            if (this.cresceu) {
                switch (no.getFb()) {
                    case -1:
                        no.setFb(0);
                        this.cresceu = false;
                        break;
                    case 0:
                        no.setFb(1);
                        this.cresceu = true;
                        break;
                    case 1:
                        if (no.getDireita().getFb() == 1) {
                            no = rotacaoEsquerda(no);
                        } else {
                            no = rotacaoDireitaEsquerda(no);
                        }
                        this.cresceu = false;
                        break;
                    default:
                        break;
                }
            }
        }
        return no;
    }

    @Override
    public boolean remover(T valor) {
        this.diminuiu = false;
        this.removeu = false;
        this.raiz = removerAVL(this.raiz, valor);
        return this.removeu;
    }

    private No<T> removerAVL(No<T> no, T valor) {
        if (no == null) {
            this.diminuiu = false;
            return null;
        }

        int comparacao = comparador.compare(valor, no.getValor());
        if (comparacao < 0) {
            no.setEsquerda(removerAVL(no.getEsquerda(), valor));
            if (this.diminuiu) {
                no = rebalancearAposRemocaoEsquerda(no);
            }
            return no;
        }

        if (comparacao > 0) {
            no.setDireita(removerAVL(no.getDireita(), valor));
            if (this.diminuiu) {
                no = rebalancearAposRemocaoDireita(no);
            }
            return no;
        }

        this.removeu = true;
        if (no.getEsquerda() == null && no.getDireita() == null) {
            this.diminuiu = true;
            return null;
        }

        if (no.getEsquerda() == null) {
            this.diminuiu = true;
            return no.getDireita();
        }

        if (no.getDireita() == null) {
            this.diminuiu = true;
            return no.getEsquerda();
        }

        No<T> sucessor = menorNo(no.getDireita());
        no.setValor(sucessor.getValor());
        no.setDireita(removerAVL(no.getDireita(), sucessor.getValor()));
        if (this.diminuiu) {
            no = rebalancearAposRemocaoDireita(no);
        }
        return no;
    }

    private No<T> rebalancearAposRemocaoEsquerda(No<T> no) {
        switch (no.getFb()) {
            case -1:
                no.setFb(0);
                this.diminuiu = true;
                break;
            case 0:
                no.setFb(1);
                this.diminuiu = false;
                break;
            case 1:
                No<T> dir = no.getDireita();
                if (dir.getFb() >= 0) {
                    no = rotacaoEsquerdaRemocao(no);
                } else {
                    no = rotacaoDireitaEsquerda(no);
                }
                break;
            default:
                break;
        }
        return no;
    }

    private No<T> rebalancearAposRemocaoDireita(No<T> no) {
        switch (no.getFb()) {
            case 1:
                no.setFb(0);
                this.diminuiu = true;
                break;
            case 0:
                no.setFb(-1);
                this.diminuiu = false;
                break;
            case -1:
                No<T> esq = no.getEsquerda();
                if (esq.getFb() <= 0) {
                    no = rotacaoDireitaRemocao(no);
                } else {
                    no = rotacaoEsquerdaDireita(no);
                }
                break;
            default:
                break;
        }
        return no;
    }

    private No<T> menorNo(No<T> no) {
        No<T> atual = no;
        while (atual.getEsquerda() != null) {
            atual = atual.getEsquerda();
        }
        return atual;
    }

    private No<T> rotacaoDireita(No<T> y) {
        No<T> x = y.getEsquerda();
        y.setEsquerda(x.getDireita());
        x.setDireita(y);
        x.setFb(0);
        y.setFb(0);
        return x;
    }

    private No<T> rotacaoEsquerda(No<T> y) {
        No<T> x = y.getDireita();
        y.setDireita(x.getEsquerda());
        x.setEsquerda(y);
        x.setFb(0);
        y.setFb(0);
        return x;
    }

    private No<T> rotacaoEsquerdaDireita(No<T> y) {
        No<T> e = y.getEsquerda();
        No<T> d = e.getDireita();

        e.setDireita(d.getEsquerda());
        d.setEsquerda(e);

        y.setEsquerda(d.getDireita());
        d.setDireita(y);

        switch (d.getFb()) {
            case -1:
                e.setFb(0);
                y.setFb(1);
                break;
            case 0:
                e.setFb(0);
                y.setFb(0);
                break;
            case 1:
                e.setFb(-1);
                y.setFb(0);
                break;
            default:
                break;
        }
        d.setFb(0);
        return d;
    }

    private No<T> rotacaoDireitaEsquerda(No<T> y) {
        No<T> d = y.getDireita();
        No<T> e = d.getEsquerda();

        d.setEsquerda(e.getDireita());
        e.setDireita(d);

        y.setDireita(e.getEsquerda());
        e.setEsquerda(y);

        switch (e.getFb()) {
            case -1:
                y.setFb(0);
                d.setFb(1);
                break;
            case 0:
                y.setFb(0);
                d.setFb(0);
                break;
            case 1:
                y.setFb(-1);
                d.setFb(0);
                break;
            default:
                break;
        }
        e.setFb(0);
        return e;
    }

    private No<T> rotacaoEsquerdaRemocao(No<T> y) {
        No<T> x = y.getDireita();
        y.setDireita(x.getEsquerda());
        x.setEsquerda(y);

        if (x.getFb() == 0) {
            y.setFb(1);
            x.setFb(-1);
            this.diminuiu = false;
        } else {
            y.setFb(0);
            x.setFb(0);
            this.diminuiu = true;
        }
        return x;
    }

    private No<T> rotacaoDireitaRemocao(No<T> y) {
        No<T> x = y.getEsquerda();
        y.setEsquerda(x.getDireita());
        x.setDireita(y);

        if (x.getFb() == 0) {
            y.setFb(-1);
            x.setFb(1);
            this.diminuiu = false;
        } else {
            y.setFb(0);
            x.setFb(0);
            this.diminuiu = true;
        }
        return x;
    }
}

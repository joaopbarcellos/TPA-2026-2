package dominio;

import listaEncadeada.ListaEncadeada;

/**
 * Representa uma linha de ônibus e a sequência de paradas que ela atende.
 */
public class LinhaOnibus {
    private int id;
    private String nome;
    private ListaEncadeada<Parada> paradas;

    public LinhaOnibus(int id, String nome) {
        this.id = id;
        this.nome = nome;
        // Mantém a ordem exata do itinerário da linha (lista não ordenada)
        this.paradas = new ListaEncadeada<>();
    }

    public void adicionarParada(Parada parada) {
        paradas.adicionar(parada);
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public ListaEncadeada<Parada> getParadas() {
        return paradas;
    }

    @Override
    public String toString() {
        return "Linha " + id + " - " + nome;
    }
}
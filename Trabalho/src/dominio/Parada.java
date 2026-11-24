package dominio;

/**
 * Representa um ponto de ônibus ou estação dentro da malha de transporte público.
 */
public class Parada {
    private int id;
    private String nome;
    private String endereco;

    public Parada(int id, String nome, String endereco) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Parada)) return false;
        Parada outra = (Parada) obj;
        return this.id == outra.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return id + " - " + nome + " (" + endereco + ")";
    }
}
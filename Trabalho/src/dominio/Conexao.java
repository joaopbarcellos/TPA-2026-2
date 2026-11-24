package dominio;

/**
 * Representa o deslocamento direto entre duas paradas realizado por uma linha específica.
 */
public class Conexao {
    private Parada origem;
    private Parada destino;
    private float tempoMinutos;
    private LinhaOnibus linha;

    public Conexao(Parada origem, Parada destino, float tempoMinutos, LinhaOnibus linha) {
        this.origem = origem;
        this.destino = destino;
        this.tempoMinutos = tempoMinutos;
        this.linha = linha;
    }

    public Parada getOrigem() {
        return origem;
    }

    public Parada getDestino() {
        return destino;
    }

    public float getTempoMinutos() {
        return tempoMinutos;
    }

    public LinhaOnibus getLinha() {
        return linha;
    }

    @Override
    public String toString() {
        return origem.getNome() + " -> " + destino.getNome()
                + " [" + linha.getNome() + " - " + tempoMinutos + " min]";
    }
}
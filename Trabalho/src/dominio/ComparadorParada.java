package dominio;

import java.util.Comparator;

/**
 * Compara duas paradas pelo seu identificador único (ID).
 * Usado pela ListaEncadeada para ordenação e buscas rápidas de pontos de ônibus.
 */
public class ComparadorParada implements Comparator<Parada> {

    @Override
    public int compare(Parada p1, Parada p2) {
        if (p1 == null && p2 == null) return 0;
        if (p1 == null) return -1;
        if (p2 == null) return 1;
        return Integer.compare(p1.getId(), p2.getId());
    }
}
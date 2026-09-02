package dominio;

import java.util.Comparator;

public class ComparadorContatoPorNome implements Comparator<Contato> {
    @Override
    public int compare(Contato o1, Contato o2) {
        return o1.getNome().compareTo(o2.getNome());
    }
}

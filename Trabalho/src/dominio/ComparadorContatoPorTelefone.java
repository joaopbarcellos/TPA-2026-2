package dominio;

import java.util.Comparator;

public class ComparadorContatoPorTelefone implements Comparator<Contato> {
    @Override
    public int compare(Contato o1, Contato o2) {
        return o1.getTelefone().compareTo(o2.getTelefone());
    }
}

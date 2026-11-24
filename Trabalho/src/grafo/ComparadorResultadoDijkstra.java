package grafo;

import java.util.Comparator;

/**
 * Compara dois resultados do algoritmo de Dijkstra com base na menor distância (tempo).
 * Permite que a ListaEncadeada mantenha os vértices não visitados sempre ordenados.
 */
public class ComparadorResultadoDijkstra<T> implements Comparator<ResultadoDijkstra<T>> {

    @Override
    public int compare(ResultadoDijkstra<T> r1, ResultadoDijkstra<T> r2) {
        int comparacaoDistancia = Float.compare(r1.getDistancia(), r2.getDistancia());
        if (comparacaoDistancia != 0) {
            return comparacaoDistancia;
        }
        // Critério de desempate consistente para evitar falso positivo de igualdade na remoção
        if (r1.getVertice().equals(r2.getVertice())) {
            return 0;
        }
        return Integer.compare(r1.getVertice().hashCode(), r2.getVertice().hashCode());
    }
}
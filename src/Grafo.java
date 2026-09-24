import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Grafo {
    private final Map<String, No> nos;

    public Grafo() {
        nos = new LinkedHashMap<>();
        cadastrarNos();
        cadastrarArestas();
    }

    private void cadastrarNos() {
        adicionarNo("A", 0, 0);
        adicionarNo("B", 1, 0);
        adicionarNo("C", 4, 0);
        adicionarNo("D", 5, 0);
        adicionarNo("E", 7, 0);

        adicionarNo("F", 0, 2);
        adicionarNo("G", 1, 2);
        adicionarNo("H", 4, 2);
        adicionarNo("I", 7, 2);

        adicionarNo("J", 1, 3);
        adicionarNo("K", 3, 3);
        adicionarNo("L", 4, 3);
        adicionarNo("M", 5, 3);
        adicionarNo("N", 7, 3);

        adicionarNo("O", 4, 4);
        adicionarNo("P", 7, 4);

        adicionarNo("Q", 0, 6);
        adicionarNo("R", 1, 6);
        adicionarNo("S", 3, 6);
        adicionarNo("T", 4, 6);
        adicionarNo("U", 7, 6);
    }

    private void cadastrarArestas() {
        // A <-> B
        adicionarBidirecional("A", "B", 1);

        // B <-> C
        adicionarBidirecional("B", "C", 3);

        // C <-> D
        adicionarBidirecional("C", "D", 1);

        // D <-> E
        adicionarBidirecional("D", "E", 2);

        // A <-> F
        adicionarBidirecional("A", "F", 2);

        // B -> G
        adicionarAresta("B", "G", 2);

        // F <-> G
        adicionarBidirecional("F", "G", 1);

        // G <-> H
        adicionarBidirecional("G", "H", 3);

        // H -> C
        adicionarAresta("H", "C", 2);

        // H -> L
        adicionarAresta("H", "L", 1);

        // D <-> M
        adicionarBidirecional("D", "M", 3);

        // E -> I
        adicionarAresta("E", "I", 2);

        // I -> N
        adicionarAresta("I", "N", 1);

        // G -> J
        adicionarAresta("G", "J", 1);

        // K -> J
        adicionarAresta("K", "J", 2);

        // L -> K
        adicionarAresta("L", "K", 1);

        // M -> L
        adicionarAresta("M", "L", 1);

        // N -> M
        adicionarAresta("N", "M", 2);

        // J -> R
        adicionarAresta("J", "R", 2);

        // K <-> S
        adicionarBidirecional("K", "S", 2);

        // F <-> Q
        adicionarBidirecional("F", "Q", 3);

        // Q -> R
        adicionarAresta("Q", "R", 1);

        // R -> S
        adicionarAresta("R", "S", 2);

        // S -> T
        adicionarAresta("S", "T", 1);

        // O -> L
        adicionarAresta("O", "L", 1);

        // T -> O
        adicionarAresta("T", "O", 1);

        // P -> O
        adicionarAresta("P", "O", 3);

        // N -> P
        adicionarAresta("N", "P", 1);

        // P -> U
        adicionarAresta("P", "U", 1);

        // T <-> U
        adicionarBidirecional("T", "U", 3);
    }

    private void adicionarNo(String nome, int x, int y) {
        nos.put(nome, new No(nome, x, y));
    }

    public void adicionarAresta(String origem, String destino, int distancia) {
        No noOrigem = buscarNo(origem);
        No noDestino = buscarNo(destino);

        if (noOrigem == null || noDestino == null) {
            throw new IllegalArgumentException("Nó inexistente na aresta: "
                    + origem + " -> " + destino);
        }

        noOrigem.adicionarAresta(new Aresta(noOrigem, noDestino, distancia));
    }

    public void adicionarBidirecional(String primeiro, String segundo, int distancia) {
        adicionarAresta(primeiro, segundo, distancia);
        adicionarAresta(segundo, primeiro, distancia);
    }

    public No buscarNo(String nome) {
        return nos.get(nome.toUpperCase());
    }

    public Collection<No> getNos() {
        return Collections.unmodifiableCollection(nos.values());
    }

    public int quantidadeDeNos() {
        return nos.size();
    }

    public int quantidadeDeArestas() {
        int quantidade = 0;

        for (No no : nos.values()) {
            quantidade += no.getArestas().size();
        }

        return quantidade;
    }
}

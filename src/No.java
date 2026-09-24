import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class No {
    private final String nome;
    private final int x;
    private final int y;
    private final List<Aresta> arestas;

    public No(String nome, int x, int y) {
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.arestas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Aresta> getArestas() {
        return Collections.unmodifiableList(arestas);
    }

    void adicionarAresta(Aresta aresta) {
        arestas.add(aresta);
    }

    @Override
    public String toString() {
        return nome;
    }
}

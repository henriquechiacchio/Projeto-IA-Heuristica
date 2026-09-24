public class Aresta {
    private final No origem;
    private final No destino;
    private final int distancia;

    public Aresta(No origem, No destino, int distancia) {
        this.origem = origem;
        this.destino = destino;
        this.distancia = distancia;
    }

    public No getOrigem() {
        return origem;
    }

    public No getDestino() {
        return destino;
    }

    public int getDistancia() {
        return distancia;
    }

    @Override
    public String toString() {
        return origem.getNome() + " -> " + destino.getNome() + " (" + distancia + " U)";
    }
}
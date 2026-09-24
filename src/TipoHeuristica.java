public enum TipoHeuristica {
    MANHATTAN("Manhattan"),
    EUCLIDIANA("Euclidiana"),
    CHEBYSHEV("Chebyshev");

    private final String nome;

    TipoHeuristica(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
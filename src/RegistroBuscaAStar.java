import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegistroBuscaAStar {
    private final List<No> caminho;
    private final double custoAcumulado;
    private final double heuristica;
    private final double custoEstimado;

    public RegistroBuscaAStar(
            List<No> caminho,
            double custoAcumulado,
            double heuristica,
            double custoEstimado
    ) {
        this.caminho = Collections.unmodifiableList(new ArrayList<>(caminho));
        this.custoAcumulado = custoAcumulado;
        this.heuristica = heuristica;
        this.custoEstimado = custoEstimado;
    }

    public List<No> getCaminho() {
        return caminho;
    }

    public double getCustoAcumulado() {
        return custoAcumulado;
    }

    public double getHeuristica() {
        return heuristica;
    }

    public double getCustoEstimado() {
        return custoEstimado;
    }
}
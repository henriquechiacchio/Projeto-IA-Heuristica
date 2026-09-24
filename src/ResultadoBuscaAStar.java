import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultadoBuscaAStar {
    private final List<No> caminho;
    private final List<RegistroBuscaAStar> registros;

    public ResultadoBuscaAStar(List<No> caminho, List<RegistroBuscaAStar> registros) {
        this.caminho = Collections.unmodifiableList(new ArrayList<>(caminho));
        this.registros = Collections.unmodifiableList(new ArrayList<>(registros));
    }

    public List<No> getCaminho() {
        return caminho;
    }

    public List<RegistroBuscaAStar> getRegistros() {
        return registros;
    }

    public boolean encontrouCaminho() {
        return !caminho.isEmpty();
    }
}
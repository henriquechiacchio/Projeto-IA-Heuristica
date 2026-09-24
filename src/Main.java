import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // --- TESTE DAS HEURÍSTICAS ---
        testarHeuristicas();

        // Abre a interface gráfica
        SwingUtilities.invokeLater(Main::criarJanela);
    }

    private static void testarHeuristicas() {
        // Exemplo: Ponto A (0, 0) até Ponto U (7, 6)
        double x1 = 0, y1 = 0;
        double x2 = 7, y2 = 6;

        System.out.println("==========================================");
        System.out.println("      TESTE DAS FUNÇÕES HEURÍSTICAS       ");
        System.out.println("==========================================");
        System.out.println("Ponto A: (" + x1 + ", " + y1 + ")");
        System.out.println("Ponto U: (" + x2 + ", " + y2 + ")\n");

        double hManhattan = Heuristica.calcular(TipoHeuristica.MANHATTAN, x1, y1, x2, y2);
        double hEuclidiana = Heuristica.calcular(TipoHeuristica.EUCLIDIANA, x1, y1, x2, y2);
        double hChebyshev = Heuristica.calcular(TipoHeuristica.CHEBYSHEV, x1, y1, x2, y2);

        System.out.println("1. Manhattan (Esperado: 13.0)  -> Obter: " + hManhattan);
        System.out.println("2. Euclidiana (Esperado: 9.21954445729...) -> Obter: " + hEuclidiana);
        System.out.println("3. Chebyshev (Esperado: 7.0)  -> Obter: " + hChebyshev);
        System.out.println("==========================================\n");

        Grafo grafo = new Grafo();
        BuscaAStar busca = new BuscaAStar();
        No origem = grafo.buscarNo("A");
        No destino = grafo.buscarNo("U");

        for (TipoHeuristica tipo : TipoHeuristica.values()) {
            List<No> caminho = busca.buscar(tipo, origem, destino);
            System.out.println("A* com " + tipo + ": " + caminho);
            System.out.println("Custo do caminho: " + busca.calcularCusto(caminho));
        }
    }

    private static void criarJanela() {
        Grafo grafo = new Grafo();

        JFrame janela = new JFrame("Mapa - Busca A*");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
                "Mapa da cidade - clique em um ponto",
                SwingConstants.CENTER
        );
        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        janela.add(titulo, BorderLayout.NORTH);
        janela.add(new MapaPanel(grafo), BorderLayout.CENTER);

        janela.pack();
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }
}

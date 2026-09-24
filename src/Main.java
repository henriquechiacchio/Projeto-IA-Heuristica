import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Font;

public class Main {
    public static void main(String[] args) {
        // --- TESTE DAS HEURÍSTICAS ---
        testarHeuristicas();

        // Abre a interface gráfica
        SwingUtilities.invokeLater(Main::criarJanela);
    }

    private static void testarHeuristicas() {
        // Exemplo: Ponto A (2, 1) até Ponto B (5, 5)
        double x1 = 2, y1 = 1;
        double x2 = 5, y2 = 5;

        System.out.println("==========================================");
        System.out.println("      TESTE DAS FUNÇÕES HEURÍSTICAS       ");
        System.out.println("==========================================");
        System.out.println("Ponto 1: (" + x1 + ", " + y1 + ")");
        System.out.println("Ponto 2: (" + x2 + ", " + y2 + ")\n");

        double hManhattan = Heuristica.calcular(TipoHeuristica.MANHATTAN, x1, y1, x2, y2);
        double hEuclidiana = Heuristica.calcular(TipoHeuristica.EUCLIDIANA, x1, y1, x2, y2);
        double hChebyshev = Heuristica.calcular(TipoHeuristica.CHEBYSHEV, x1, y1, x2, y2);

        System.out.println("1. Manhattan (Esperado: 7.0)  -> Obter: " + hManhattan);
        System.out.println("2. Euclidiana (Esperado: 5.0) -> Obter: " + hEuclidiana);
        System.out.println("3. Chebyshev (Esperado: 4.0)  -> Obter: " + hChebyshev);
        System.out.println("==========================================\n");
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

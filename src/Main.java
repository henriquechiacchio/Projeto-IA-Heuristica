import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Font;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::criarJanela);
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

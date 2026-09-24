import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapaPanel extends JPanel {
    private static final int MARGEM_X = 70;
    private static final int MARGEM_Y = 70;
    private static final int TAMANHO_CELULA = 70;
    private static final int RAIO_PONTO = 10;

    private final Grafo grafo;
    private No pontoSelecionado;

    public MapaPanel(Grafo grafo) {
        this.grafo = grafo;

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(680, 620));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selecionarPonto(e.getX(), e.getY());
            }
        });
    }

    private void selecionarPonto(int mouseX, int mouseY) {
        for (No no : grafo.getNos()) {
            int x = telaX(no);
            int y = telaY(no);

            double distancia = Math.hypot(mouseX - x, mouseY - y);

            if (distancia <= RAIO_PONTO + 10) {
                pontoSelecionado = no;
                System.out.println("Ponto selecionado: " + no.getNome());
                repaint();
                return;
            }
        }
    }

    private int telaX(No no) {
        return MARGEM_X + no.getX() * TAMANHO_CELULA;
    }

    private int telaY(No no) {
        return MARGEM_Y + no.getY() * TAMANHO_CELULA;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics.create();

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        desenharGrade(g);
        desenharArestas(g);
        desenharNos(g);

        g.dispose();
    }

    private void desenharGrade(Graphics2D g) {
        g.setColor(new Color(225, 225, 225));
        g.setStroke(new BasicStroke(1));

        for (int x = 0; x <= 7; x++) {
            int posX = MARGEM_X + x * TAMANHO_CELULA;
            g.drawLine(posX, MARGEM_Y, posX, MARGEM_Y + 6 * TAMANHO_CELULA);
        }

        for (int y = 0; y <= 6; y++) {
            int posY = MARGEM_Y + y * TAMANHO_CELULA;
            g.drawLine(MARGEM_X, posY, MARGEM_X + 7 * TAMANHO_CELULA, posY);
        }
    }

    private void desenharArestas(Graphics2D g) {
        g.setColor(new Color(220, 30, 30));
        g.setStroke(new BasicStroke(4));

        for (No no : grafo.getNos()) {
            for (Aresta aresta : no.getArestas()) {
                desenharAresta(g, aresta);
            }
        }
    }

    private void desenharAresta(Graphics2D g, Aresta aresta) {
        int x1 = telaX(aresta.getOrigem());
        int y1 = telaY(aresta.getOrigem());
        int x2 = telaX(aresta.getDestino());
        int y2 = telaY(aresta.getDestino());

        g.drawLine(x1, y1, x2, y2);
        desenharSeta(g, x1, y1, x2, y2);
    }

    private void desenharSeta(Graphics2D g, int x1, int y1, int x2, int y2) {
        double angulo = Math.atan2(y2 - y1, x2 - x1);

        int comprimento = 9;
        int largura = 5;

        double posicao = 0.62;
        int cx = (int) (x1 + (x2 - x1) * posicao);
        int cy = (int) (y1 + (y2 - y1) * posicao);

        int xA = (int) (cx - comprimento * Math.cos(angulo)
                + largura * Math.sin(angulo));
        int yA = (int) (cy - comprimento * Math.sin(angulo)
                - largura * Math.cos(angulo));

        int xB = (int) (cx - comprimento * Math.cos(angulo)
                - largura * Math.sin(angulo));
        int yB = (int) (cy - comprimento * Math.sin(angulo)
                + largura * Math.cos(angulo));

        int[] xs = {cx, xA, xB};
        int[] ys = {cy, yA, yB};

        g.setColor(Color.BLACK);
        g.fillPolygon(xs, ys, 3);

        g.setColor(new Color(220, 30, 30));
    }

    private void desenharNos(Graphics2D g) {
        g.setFont(new Font("Arial", Font.BOLD, 20));

        for (No no : grafo.getNos()) {
            int x = telaX(no);
            int y = telaY(no);

            if (no == pontoSelecionado) {
                g.setColor(new Color(255, 190, 0));
                g.fillOval(
                        x - RAIO_PONTO - 5,
                        y - RAIO_PONTO - 5,
                        (RAIO_PONTO + 5) * 2,
                        (RAIO_PONTO + 5) * 2
                );
            }

            g.setColor(new Color(40, 70, 180));
            g.fillOval(
                    x - RAIO_PONTO,
                    y - RAIO_PONTO,
                    RAIO_PONTO * 2,
                    RAIO_PONTO * 2
            );

            g.setColor(Color.BLACK);
            g.drawString(no.getNome(), x + 12, y - 12);
        }
    }
}

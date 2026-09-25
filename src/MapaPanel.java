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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class MapaPanel extends JPanel {
    private static final int MARGEM_X = 70;
    private static final int MARGEM_Y = 70;
    private static final int TAMANHO_CELULA = 70;
    private static final int RAIO_PONTO = 10;

    private final Grafo grafo;
    private No origem;
    private No destino;
    private List<No> caminho =
            Collections.emptyList();
    private List<RegistroBuscaAStar> registros =
            Collections.emptyList();
    private Consumer<No> cliqueListener;

    public MapaPanel(Grafo grafo) {
        this.grafo = grafo;

        setBackground(Color.WHITE);

        setPreferredSize(
                new Dimension(680, 550)
        );

        addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {
                        selecionarPonto(
                                e.getX(),
                                e.getY()
                        );
                    }
                }
        );
    }

    public void setCliqueListener(
            Consumer<No> cliqueListener
    ) {
        this.cliqueListener =
                cliqueListener;
    }

    public void setOrigem(
            No origem
    ) {
        this.origem = origem;
        repaint();
    }

    public void setDestino(
            No destino
    ) {
        this.destino = destino;
        repaint();
    }

    public void setCaminho(
            List<No> caminho
    ) {
        this.caminho =
                caminho == null
                        ? Collections.emptyList()
                        : new ArrayList<>(
                                caminho
                        );

        repaint();
    }

    public void setRegistros(
            List<RegistroBuscaAStar> registros
    ) {
        this.registros =
                registros == null
                        ? Collections.emptyList()
                        : new ArrayList<>(
                                registros
                        );

        repaint();
    }

    private void selecionarPonto(
            int mouseX,
            int mouseY
    ) {
        No pontoClicado = null;

        for (No no :
                grafo.getNos()) {

            int x = telaX(no);
            int y = telaY(no);

            double distancia =
                    Math.hypot(
                            mouseX - x,
                            mouseY - y
                    );

            if (distancia
                    <= RAIO_PONTO + 10) {

                pontoClicado = no;
                break;
            }
        }

        if (pontoClicado != null
                && cliqueListener != null) {

            cliqueListener.accept(
                    pontoClicado
            );
        }
    }

    private int telaX(
            No no
    ) {
        return MARGEM_X
                + no.getX()
                * TAMANHO_CELULA;
    }

    private int telaY(
            No no
    ) {
        int deslocamentoLinhaInferior =
                no.getY() == 6
                        ? TAMANHO_CELULA
                        : 0;

        return MARGEM_Y
                + no.getY()
                * TAMANHO_CELULA
                - deslocamentoLinhaInferior;
    }

    @Override
    protected void paintComponent(
            Graphics graphics
    ) {
        super.paintComponent(
                graphics
        );

        Graphics2D g =
                (Graphics2D)
                        graphics.create();

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        desenharGrade(g);
        desenharArestas(g);
        desenharArestasTestadas(g);
        desenharCaminho(g);
        desenharSetas(g);
        desenharNos(g);
        desenharLegenda(g);

        g.dispose();
    }

    private void desenharGrade(
            Graphics2D g
    ) {
        g.setColor(
                new Color(
                        225,
                        225,
                        225
                )
        );

        g.setStroke(
                new BasicStroke(1)
        );

        for (int x = 0;
             x <= 7;
             x++) {

            int posX =
                    MARGEM_X
                            + x
                            * TAMANHO_CELULA;

            g.drawLine(
                    posX,
                    MARGEM_Y,
                    posX,
                    MARGEM_Y
                            + 5
                            * TAMANHO_CELULA
            );
        }

        for (int y = 0;
             y <= 5;
             y++) {

            int posY =
                    MARGEM_Y
                            + y
                            * TAMANHO_CELULA;

            g.drawLine(
                    MARGEM_X,
                    posY,
                    MARGEM_X
                            + 7
                            * TAMANHO_CELULA,
                    posY
            );
        }
    }

    private void desenharArestas(
            Graphics2D g
    ) {
        g.setColor(
                new Color(
                        220,
                        30,
                        30
                )
        );

        g.setStroke(
                new BasicStroke(4)
        );

        for (No no :
                grafo.getNos()) {

            for (Aresta aresta :
                    no.getArestas()) {

                int x1 =
                        telaX(
                                aresta.getOrigem()
                        );

                int y1 =
                        telaY(
                                aresta.getOrigem()
                        );

                int x2 =
                        telaX(
                                aresta.getDestino()
                        );

                int y2 =
                        telaY(
                                aresta.getDestino()
                        );

                g.drawLine(
                        x1,
                        y1,
                        x2,
                        y2
                );
            }
        }
    }

    private void desenharArestasTestadas(
            Graphics2D g
    ) {
        if (registros == null
                || registros.isEmpty()) {

            return;
        }

        g.setColor(
                new Color(
                        255,
                        193,
                        7
                )
        );

        g.setStroke(
                new BasicStroke(
                        6,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                )
        );

        Set<String> desenhadas =
                new HashSet<>();

        for (RegistroBuscaAStar registro :
                registros) {

            List<No> caminhoRegistro =
                    registro.getCaminho();

            if (caminhoRegistro == null
                    || caminhoRegistro.size()
                    < 2) {

                continue;
            }

            int ultimo =
                    caminhoRegistro.size()
                            - 1;

            No origemAresta =
                    caminhoRegistro.get(
                            ultimo - 1
                    );

            No destinoAresta =
                    caminhoRegistro.get(
                            ultimo
                    );

            if (estaNoCaminhoFinal(
                    origemAresta,
                    destinoAresta
            )) {
                continue;
            }

            String chave =
                    origemAresta.getNome()
                            + ">"
                            + destinoAresta.getNome();

            if (!desenhadas.add(
                    chave
            )) {
                continue;
            }

            g.drawLine(
                    telaX(origemAresta),
                    telaY(origemAresta),
                    telaX(destinoAresta),
                    telaY(destinoAresta)
            );
        }
    }

    private boolean estaNoCaminhoFinal(
            No origemAresta,
            No destinoAresta
    ) {
        if (caminho == null
                || caminho.size() < 2) {

            return false;
        }

        for (int i = 0;
             i < caminho.size() - 1;
             i++) {

            No atual =
                    caminho.get(i);

            No proximo =
                    caminho.get(i + 1);

            if (atual == origemAresta
                    && proximo == destinoAresta) {

                return true;
            }
        }

        return false;
    }

    private void desenharCaminho(
            Graphics2D g
    ) {
        if (caminho == null
                || caminho.size() < 2) {

            return;
        }

        g.setColor(
                new Color(
                        30,
                        100,
                        230
                )
        );

        g.setStroke(
                new BasicStroke(
                        7,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                )
        );

        for (int i = 0;
             i < caminho.size() - 1;
             i++) {

            No atual =
                    caminho.get(i);

            No proximo =
                    caminho.get(i + 1);

            g.drawLine(
                    telaX(atual),
                    telaY(atual),
                    telaX(proximo),
                    telaY(proximo)
            );
        }
    }

    private void desenharSetas(
            Graphics2D g
    ) {
        for (No no :
                grafo.getNos()) {

            for (Aresta aresta :
                    no.getArestas()) {

                desenharSeta(
                        g,
                        telaX(
                                aresta.getOrigem()
                        ),
                        telaY(
                                aresta.getOrigem()
                        ),
                        telaX(
                                aresta.getDestino()
                        ),
                        telaY(
                                aresta.getDestino()
                        )
                );
            }
        }
    }

    private void desenharSeta(
            Graphics2D g,
            int x1,
            int y1,
            int x2,
            int y2
    ) {
        double angulo =
                Math.atan2(
                        y2 - y1,
                        x2 - x1
                );

        int comprimento = 9;
        int largura = 5;

        double posicao = 0.62;

        int cx =
                (int) (
                        x1
                                + (x2 - x1)
                                * posicao
                );

        int cy =
                (int) (
                        y1
                                + (y2 - y1)
                                * posicao
                );

        int xA =
                (int) (
                        cx
                                - comprimento
                                * Math.cos(angulo)
                                + largura
                                * Math.sin(angulo)
                );

        int yA =
                (int) (
                        cy
                                - comprimento
                                * Math.sin(angulo)
                                - largura
                                * Math.cos(angulo)
                );

        int xB =
                (int) (
                        cx
                                - comprimento
                                * Math.cos(angulo)
                                - largura
                                * Math.sin(angulo)
                );

        int yB =
                (int) (
                        cy
                                - comprimento
                                * Math.sin(angulo)
                                + largura
                                * Math.cos(angulo)
                );

        int[] xs = {
                cx,
                xA,
                xB
        };

        int[] ys = {
                cy,
                yA,
                yB
        };

        g.setColor(
                Color.BLACK
        );

        g.fillPolygon(
                xs,
                ys,
                3
        );
    }

    private void desenharNos(
            Graphics2D g
    ) {
        g.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        for (No no :
                grafo.getNos()) {

            int x = telaX(no);
            int y = telaY(no);

            if (no == origem
                    && no == destino) {

                g.setColor(
                        new Color(
                                147,
                                51,
                                234
                        )
                );

                g.fillOval(
                        x - RAIO_PONTO - 5,
                        y - RAIO_PONTO - 5,
                        (RAIO_PONTO + 5) * 2,
                        (RAIO_PONTO + 5) * 2
                );

                g.setColor(
                        Color.BLACK
                );

                g.drawString(
                        no.getNome(),
                        x + 12,
                        y - 12
                );

                continue;
            }

            if (no == origem) {
                g.setColor(
                        new Color(
                                34,
                                197,
                                94
                        )
                );

                g.fillOval(
                        x - RAIO_PONTO - 5,
                        y - RAIO_PONTO - 5,
                        (RAIO_PONTO + 5) * 2,
                        (RAIO_PONTO + 5) * 2
                );

                g.setColor(
                        Color.BLACK
                );

                g.drawString(
                        no.getNome(),
                        x + 12,
                        y - 12
                );

                continue;
            }

            if (no == destino) {
                g.setColor(
                        new Color(
                                239,
                                68,
                                68
                        )
                );

                g.fillOval(
                        x - RAIO_PONTO - 5,
                        y - RAIO_PONTO - 5,
                        (RAIO_PONTO + 5) * 2,
                        (RAIO_PONTO + 5) * 2
                );

                g.setColor(
                        Color.BLACK
                );

                g.drawString(
                        no.getNome(),
                        x + 12,
                        y - 12
                );

                continue;
            }

            g.setColor(
                    new Color(
                            40,
                            70,
                            180
                    )
            );

            g.fillOval(
                    x - RAIO_PONTO,
                    y - RAIO_PONTO,
                    RAIO_PONTO * 2,
                    RAIO_PONTO * 2
            );

            g.setColor(
                    Color.BLACK
            );

            g.drawString(
                    no.getNome(),
                    x + 12,
                    y - 12
            );
        }
    }

    private void desenharLegenda(
            Graphics2D g
    ) {
        int y = getHeight() - 25;

        g.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        g.setStroke(
                new BasicStroke(5)
        );

        g.setColor(
                new Color(
                        30,
                        100,
                        230
                )
        );

        g.drawLine(
                30,
                y,
                55,
                y
        );

        g.setColor(
                Color.BLACK
        );

        g.drawString(
                "Caminho final",
                62,
                y + 4
        );

        g.setColor(
                new Color(
                        255,
                        193,
                        7
                )
        );

        g.drawLine(
                170,
                y,
                195,
                y
        );

        g.setColor(
                Color.BLACK
        );

        g.drawString(
                "Testada e não selecionada",
                202,
                y + 4
        );

        g.setColor(
                new Color(
                        34,
                        197,
                        94
                )
        );

        g.fillOval(
                385,
                y - 6,
                12,
                12
        );

        g.setColor(
                Color.BLACK
        );

        g.drawString(
                "Origem",
                403,
                y + 4
        );

        g.setColor(
                new Color(
                        239,
                        68,
                        68
                )
        );

        g.fillOval(
                470,
                y - 6,
                12,
                12
        );

        g.setColor(
                Color.BLACK
        );

        g.drawString(
                "Destino",
                488,
                y + 4
        );
    }
}
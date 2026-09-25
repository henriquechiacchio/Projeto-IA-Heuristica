import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Main {
    private static No origemSelecionada;
    private static No destinoSelecionado;
    private static MapaPanel mapaPanel;
    private static JTextField origemField;
    private static JTextField destinoField;
    private static JTextArea resultadoArea;
    private static JLabel statusLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::criarJanela);
    }

    private static void criarJanela() {
        Grafo grafo = new Grafo();

        JFrame janela = new JFrame("Mapa - Busca A*");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "Mapa da cidade - selecione origem e destino",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        mapaPanel = new MapaPanel(grafo);

        mapaPanel.setCliqueListener(no -> {
            if (origemSelecionada == null || destinoSelecionado != null) {
                origemSelecionada = no;
                destinoSelecionado = null;

                mapaPanel.setOrigem(no);
                mapaPanel.setDestino(null);
                mapaPanel.setCaminho(null);
                mapaPanel.setRegistros(null);

                resultadoArea.setText(
                        "Origem selecionada: " + no.getNome()
                                + "\nSelecione agora o destino."
                );

                statusLabel.setText(
                        "Origem selecionada: " + no.getNome()
                                + ". Agora selecione o destino."
                );
            } else {
                destinoSelecionado = no;
                mapaPanel.setDestino(no);

                statusLabel.setText(
                        "Destino selecionado: " + no.getNome()
                                + ". Escolha a heurística e execute."
                );
            }

            atualizarCampos();
        });

        JPanel painelControles = criarPainelControles();

        janela.add(titulo, BorderLayout.NORTH);
        janela.add(mapaPanel, BorderLayout.CENTER);
        janela.add(painelControles, BorderLayout.SOUTH);

        janela.pack();
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }

    private static JPanel criarPainelControles() {
        JPanel painel = new JPanel();
        painel.setLayout(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel linhaInputs = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 12, 5)
        );

        linhaInputs.setBackground(Color.WHITE);

        origemField = new JTextField("Nenhuma", 10);
        origemField.setEditable(false);

        destinoField = new JTextField("Nenhuma", 10);
        destinoField.setEditable(false);

        JLabel lblOrigem = new JLabel("Origem:");
        JLabel lblDestino = new JLabel("Destino:");
        JLabel lblHeuristica = new JLabel("Heurística:");

        JComboBox<TipoHeuristica> comboHeuristica =
                new JComboBox<>(TipoHeuristica.values());

        comboHeuristica.setSelectedItem(TipoHeuristica.MANHATTAN);

        JButton executarBotao = new JButton("Executar A*");

        executarBotao.addActionListener(
                e -> executarBusca(
                        (TipoHeuristica) comboHeuristica.getSelectedItem()
                )
        );

        JButton limparBotao = new JButton("Limpar seleção");

        limparBotao.addActionListener(
                e -> limparSelecao()
        );

        linhaInputs.add(lblOrigem);
        linhaInputs.add(origemField);
        linhaInputs.add(lblDestino);
        linhaInputs.add(destinoField);
        linhaInputs.add(lblHeuristica);
        linhaInputs.add(comboHeuristica);
        linhaInputs.add(executarBotao);
        linhaInputs.add(limparBotao);

        resultadoArea = new JTextArea(9, 50);
        resultadoArea.setEditable(false);
        resultadoArea.setLineWrap(true);
        resultadoArea.setWrapStyleWord(true);

        resultadoArea.setText(
                "Selecione primeiro a origem e depois o destino no mapa para iniciar a busca."
        );

        statusLabel = new JLabel(
                "Aguardando seleção do mapa."
        );

        statusLabel.setForeground(
                new Color(40, 40, 40)
        );

        painel.add(
                linhaInputs,
                BorderLayout.NORTH
        );

        painel.add(
                new JScrollPane(resultadoArea),
                BorderLayout.CENTER
        );

        painel.add(
                statusLabel,
                BorderLayout.SOUTH
        );

        return painel;
    }

    private static void atualizarCampos() {
        origemField.setText(
                origemSelecionada == null
                        ? "Nenhuma"
                        : origemSelecionada.getNome()
        );

        destinoField.setText(
                destinoSelecionado == null
                        ? "Nenhuma"
                        : destinoSelecionado.getNome()
        );
    }

    private static void executarBusca(
            TipoHeuristica tipoHeuristica
    ) {
        if (origemSelecionada == null
                || destinoSelecionado == null) {

            resultadoArea.setText(
                    "Primeiro selecione a origem e o destino no mapa."
            );

            statusLabel.setText(
                    "Faltam dados para executar a busca."
            );

            return;
        }

        BuscaAStar busca = new BuscaAStar();

        ResultadoBuscaAStar resultado = busca.executar(
                tipoHeuristica,
                origemSelecionada,
                destinoSelecionado
        );

        List<No> caminho = resultado.getCaminho();

        StringBuilder texto = new StringBuilder();

        texto.append("Origem: ")
                .append(origemSelecionada.getNome())
                .append("\n");

        texto.append("Destino: ")
                .append(destinoSelecionado.getNome())
                .append("\n");

        texto.append("Heurística: ")
                .append(tipoHeuristica)
                .append("\n");

        if (resultado.encontrouCaminho()) {
            texto.append("Caminho: ")
                    .append(formatarCaminho(caminho))
                    .append("\n");

            texto.append("Custo total: ")
                    .append(calcularCustoInteiro(caminho))
                    .append(" U")
                    .append("\n");

            texto.append(
                    "Arestas testadas e não selecionadas: "
            );

            Set<String> arestasTestadas =
                    obterArestasNaoSelecionadas(
                            resultado,
                            caminho
                    );

            if (arestasTestadas.isEmpty()) {
                texto.append("Nenhuma");
            } else {
                texto.append(
                        String.join(
                                ", ",
                                arestasTestadas
                        )
                );
            }

            texto.append("\n");

            statusLabel.setText(
                    "Busca concluída com "
                            + tipoHeuristica
                            + "."
            );
        } else {
            texto.append(
                    "Nenhum caminho encontrado."
            ).append("\n");

            texto.append(
                    "Arestas testadas: "
            );

            Set<String> arestasTestadas =
                    obterTodasArestasTestadas(
                            resultado
                    );

            if (arestasTestadas.isEmpty()) {
                texto.append("Nenhuma");
            } else {
                texto.append(
                        String.join(
                                ", ",
                                arestasTestadas
                        )
                );
            }

            texto.append("\n");

            statusLabel.setText(
                    "Não foi possível encontrar um caminho."
            );
        }

        resultadoArea.setText(
                texto.toString()
        );

        resultadoArea.setCaretPosition(0);

        mapaPanel.setRegistros(
                resultado.getRegistros()
        );

        mapaPanel.setCaminho(
                caminho
        );
    }

    private static String formatarCaminho(
            List<No> caminho
    ) {
        if (caminho == null
                || caminho.isEmpty()) {

            return "Nenhum";
        }

        StringBuilder texto =
                new StringBuilder();

        texto.append(
                caminho.get(0).getNome()
        );

        int custoAcumulado = 0;

        for (int i = 0;
             i < caminho.size() - 1;
             i++) {

            No atual =
                    caminho.get(i);

            No proximo =
                    caminho.get(i + 1);

            custoAcumulado +=
                    buscarDistancia(
                            atual,
                            proximo
                    );

            texto.append(" → ")
                    .append(proximo.getNome())
                    .append("(")
                    .append(custoAcumulado)
                    .append(")");
        }

        return texto.toString();
    }

    private static int calcularCustoInteiro(
            List<No> caminho
    ) {
        if (caminho == null
                || caminho.size() < 2) {

            return 0;
        }

        int custo = 0;

        for (int i = 0;
             i < caminho.size() - 1;
             i++) {

            custo += buscarDistancia(
                    caminho.get(i),
                    caminho.get(i + 1)
            );
        }

        return custo;
    }

    private static int buscarDistancia(
            No origem,
            No destino
    ) {
        for (Aresta aresta :
                origem.getArestas()) {

            if (aresta.getDestino()
                    == destino) {

                return aresta.getDistancia();
            }
        }

        throw new IllegalArgumentException(
                "Não existe aresta entre "
                        + origem.getNome()
                        + " e "
                        + destino.getNome()
        );
    }

    private static Set<String>
    obterArestasNaoSelecionadas(
            ResultadoBuscaAStar resultado,
            List<No> caminho
    ) {
        Set<String> arestas =
                new LinkedHashSet<>();

        for (RegistroBuscaAStar registro :
                resultado.getRegistros()) {

            List<No> caminhoTestado =
                    registro.getCaminho();

            if (caminhoTestado.size() < 2) {
                continue;
            }

            int ultimo =
                    caminhoTestado.size() - 1;

            No origem =
                    caminhoTestado.get(
                            ultimo - 1
                    );

            No destino =
                    caminhoTestado.get(
                            ultimo
                    );

            if (!estaNoCaminhoFinal(
                    origem,
                    destino,
                    caminho
            )) {
                arestas.add(
                        origem.getNome()
                                + " → "
                                + destino.getNome()
                );
            }
        }

        return arestas;
    }

    private static Set<String>
    obterTodasArestasTestadas(
            ResultadoBuscaAStar resultado
    ) {
        Set<String> arestas =
                new LinkedHashSet<>();

        for (RegistroBuscaAStar registro :
                resultado.getRegistros()) {

            List<No> caminhoTestado =
                    registro.getCaminho();

            if (caminhoTestado.size() < 2) {
                continue;
            }

            int ultimo =
                    caminhoTestado.size() - 1;

            No origem =
                    caminhoTestado.get(
                            ultimo - 1
                    );

            No destino =
                    caminhoTestado.get(
                            ultimo
                    );

            arestas.add(
                    origem.getNome()
                            + " → "
                            + destino.getNome()
            );
        }

        return arestas;
    }

    private static boolean estaNoCaminhoFinal(
            No origem,
            No destino,
            List<No> caminho
    ) {
        if (caminho == null
                || caminho.size() < 2) {

            return false;
        }

        for (int i = 0;
             i < caminho.size() - 1;
             i++) {

            if (caminho.get(i) == origem
                    && caminho.get(i + 1)
                    == destino) {

                return true;
            }
        }

        return false;
    }

    private static void limparSelecao() {
        origemSelecionada = null;
        destinoSelecionado = null;

        mapaPanel.setOrigem(null);
        mapaPanel.setDestino(null);
        mapaPanel.setCaminho(null);
        mapaPanel.setRegistros(null);

        atualizarCampos();

        resultadoArea.setText(
                "Selecione primeiro a origem e depois o destino no mapa para iniciar a busca."
        );

        statusLabel.setText(
                "Seleção limpa. Aguardando nova escolha no mapa."
        );
    }
}
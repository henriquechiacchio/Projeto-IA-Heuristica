import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

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
            if (origemSelecionada == null || (origemSelecionada != null && destinoSelecionado != null)) {
                origemSelecionada = no;
                destinoSelecionado = null;
                mapaPanel.setOrigem(no);
                mapaPanel.setDestino(null);
                mapaPanel.setCaminho(null);
                statusLabel.setText("Origem selecionada: " + no.getNome() + ". Agora selecione o destino.");
            } else {
                destinoSelecionado = no;
                mapaPanel.setDestino(no);
                statusLabel.setText("Destino selecionado: " + no.getNome() + ". Escolha a heurística e execute.");
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

        JPanel linhaInputs = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        linhaInputs.setBackground(Color.WHITE);

        origemField = new JTextField("Nenhuma", 10);
        origemField.setEditable(false);
        destinoField = new JTextField("Nenhuma", 10);
        destinoField.setEditable(false);

        JLabel lblOrigem = new JLabel("Origem:");
        JLabel lblDestino = new JLabel("Destino:");
        JLabel lblHeuristica = new JLabel("Heurística:");
        JComboBox<TipoHeuristica> comboHeuristica = new JComboBox<>(TipoHeuristica.values());
        comboHeuristica.setSelectedItem(TipoHeuristica.MANHATTAN);

        JButton executarBotao = new JButton("Executar A*");
        executarBotao.addActionListener(e -> executarBusca((TipoHeuristica) comboHeuristica.getSelectedItem()));

        JButton limparBotao = new JButton("Limpar seleção");
        limparBotao.addActionListener(e -> limparSelecao());

        linhaInputs.add(lblOrigem);
        linhaInputs.add(origemField);
        linhaInputs.add(lblDestino);
        linhaInputs.add(destinoField);
        linhaInputs.add(lblHeuristica);
        linhaInputs.add(comboHeuristica);
        linhaInputs.add(executarBotao);
        linhaInputs.add(limparBotao);

        resultadoArea = new JTextArea(5, 50);
        resultadoArea.setEditable(false);
        resultadoArea.setLineWrap(true);
        resultadoArea.setWrapStyleWord(true);
        resultadoArea.setText("Selecione primeiro a origem e depois o destino no mapa para iniciar a busca.");

        statusLabel = new JLabel("Aguardando seleção do mapa.");
        statusLabel.setForeground(new Color(40, 40, 40));

        painel.add(linhaInputs, BorderLayout.NORTH);
        painel.add(new JScrollPane(resultadoArea), BorderLayout.CENTER);
        painel.add(statusLabel, BorderLayout.SOUTH);

        return painel;
    }

    private static void atualizarCampos() {
        origemField.setText(origemSelecionada == null ? "Nenhuma" : origemSelecionada.getNome());
        destinoField.setText(destinoSelecionado == null ? "Nenhuma" : destinoSelecionado.getNome());
    }

    private static void executarBusca(TipoHeuristica tipoHeuristica) {
        if (origemSelecionada == null || destinoSelecionado == null) {
            resultadoArea.setText("Primeiro selecione a origem e o destino no mapa.");
            statusLabel.setText("Faltam dados para executar a busca.");
            return;
        }

        BuscaAStar busca = new BuscaAStar();
        List<No> caminho = busca.buscar(tipoHeuristica, origemSelecionada, destinoSelecionado);
        double custo = busca.calcularCusto(caminho);

        StringBuilder texto = new StringBuilder();
        texto.append("Origem: ").append(origemSelecionada.getNome()).append("\n");
        texto.append("Destino: ").append(destinoSelecionado.getNome()).append("\n");
        texto.append("Heurística: ").append(tipoHeuristica).append("\n");
        texto.append("Caminho: ").append(caminho).append("\n");
        texto.append("Custo total: ").append(custo).append("\n");

        resultadoArea.setText(texto.toString());
        statusLabel.setText("Busca concluída com " + tipoHeuristica + ".");
        mapaPanel.setCaminho(caminho);
    }

    private static void limparSelecao() {
        origemSelecionada = null;
        destinoSelecionado = null;
        mapaPanel.setOrigem(null);
        mapaPanel.setDestino(null);
        mapaPanel.setCaminho(null);
        atualizarCampos();
        resultadoArea.setText("Selecione primeiro a origem e depois o destino no mapa para iniciar a busca.");
        statusLabel.setText("Seleção limpa. Aguardando nova escolha no mapa.");
    }
}

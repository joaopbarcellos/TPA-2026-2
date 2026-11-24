package apresentacao;

import dominio.Parada;
import listaEncadeada.No;
import servico.TransporteServico;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

/**
 * Camada de Apresentação (Interface Gráfica Swing).
 * Permite carregar arquivos, adicionar vértices/arestas e consultar rotas.
 */
public class TelaSistemaTransporte extends JFrame {
    private final TransporteServico servico;

    // Componentes da aba de Consulta de Rotas
    private JComboBox<Parada> comboOrigem;
    private JComboBox<Parada> comboDestino;
    private JTextArea areaResultadoRota;

    // Componentes da aba de Adicionar Nó (Parada)
    private JTextField txtIdParada;
    private JTextField txtNomeParada;
    private JTextField txtEnderecoParada;

    // Componentes da aba de Adicionar Aresta (Conexão)
    private JComboBox<Parada> comboConexaoOrigem;
    private JComboBox<Parada> comboConexaoDestino;
    private JTextField txtTempoMinutos;
    private JTextField txtIdLinha;
    private JTextField txtNomeLinha;

    // Área de visualização do Grafo
    private JTextArea areaGrafo;
    private JLabel lblStatus;

    public TelaSistemaTransporte(TransporteServico servico) {
        this.servico = servico;
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle("Sistema de Roteamento de Transporte Público - Grafos");
        setSize(860, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        // Barra Superior: Carregamento de Arquivo
        JPanel painelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelSuperior.setBorder(BorderFactory.createTitledBorder("1. Base de Dados (Arquivo .txt)"));

        JTextField txtCaminhoArquivo = new JTextField("malhaTransporteTeste.txt", 30);
        JButton btnProcurar = new JButton("Procurar...");
        JButton btnCarregar = new JButton("Carregar Grafo");

        btnProcurar.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(".");
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File arquivo = chooser.getSelectedFile();
                txtCaminhoArquivo.setText(arquivo.getPath());
            }
        });

        btnCarregar.addActionListener(e -> {
            servico.carregarDadosDoArquivo(txtCaminhoArquivo.getText().trim());
            atualizarCombosParadas();
            atualizarVisualizacaoGrafo();
            lblStatus.setText("Arquivo carregado com sucesso! Total de paradas: "
                    + servico.listarParadas().quantidadeNos());
        });

        painelSuperior.add(new JLabel("Arquivo:"));
        painelSuperior.add(txtCaminhoArquivo);
        painelSuperior.add(btnProcurar);
        painelSuperior.add(btnCarregar);

        add(painelSuperior, BorderLayout.NORTH);

        // Abas Centrais
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Consultar Melhor Percurso", criarPainelConsultaRotas());
        abas.addTab("Adicionar Parada / Conexão", criarPainelCadastroElementos());
        abas.addTab("Visualizar Grafo (Adjacências)", criarPainelVisualizacaoGrafo());

        add(abas, BorderLayout.CENTER);

        // Barra Inferior de Status
        lblStatus = new JLabel("Pronto. Carregue um arquivo ou adicione paradas manualmente.");
        lblStatus.setBorder(new EmptyBorder(5, 10, 5, 10));
        add(lblStatus, BorderLayout.SOUTH);
    }

    private JPanel criarPainelConsultaRotas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel painelControles = new JPanel(new GridLayout(3, 2, 10, 10));
        comboOrigem = new JComboBox<>();
        comboDestino = new JComboBox<>();

        JButton btnDijkstra = new JButton("Menor Tempo (Dijkstra)");
        JButton btnBfs = new JButton("Menos Conexões (BFS)");

        btnDijkstra.addActionListener(e -> executarBuscaRota(true));
        btnBfs.addActionListener(e -> executarBuscaRota(false));

        painelControles.add(new JLabel("Parada de Origem:"));
        painelControles.add(comboOrigem);
        painelControles.add(new JLabel("Parada de Destino:"));
        painelControles.add(comboDestino);
        painelControles.add(btnDijkstra);
        painelControles.add(btnBfs);

        areaResultadoRota = new JTextArea();
        areaResultadoRota.setEditable(false);
        areaResultadoRota.setFont(new Font("Monospaced", Font.PLAIN, 13));

        painel.add(painelControles, BorderLayout.NORTH);
        painel.add(new JScrollPane(areaResultadoRota), BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelCadastroElementos() {
        JPanel painelPrincipal = new JPanel(new GridLayout(1, 2, 15, 10));
        painelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Formulário 1: Novo Vértice (Parada)
        JPanel painelParada = new JPanel(new GridLayout(5, 2, 8, 12));
        painelParada.setBorder(BorderFactory.createTitledBorder("Novo Vértice (Parada / Estação)"));

        txtIdParada = new JTextField();
        txtNomeParada = new JTextField();
        txtEnderecoParada = new JTextField();
        JButton btnAdicionarParada = new JButton("Adicionar Parada");

        btnAdicionarParada.addActionListener(e -> acaoAdicionarParada());

        painelParada.add(new JLabel("ID da Parada (int):"));
        painelParada.add(txtIdParada);
        painelParada.add(new JLabel("Nome do Ponto:"));
        painelParada.add(txtNomeParada);
        painelParada.add(new JLabel("Endereço:"));
        painelParada.add(txtEnderecoParada);
        painelParada.add(new JLabel(""));
        painelParada.add(btnAdicionarParada);

        // Formulário 2: Nova Aresta (Conexão)
        JPanel painelConexao = new JPanel(new GridLayout(6, 2, 8, 10));
        painelConexao.setBorder(BorderFactory.createTitledBorder("Nova Aresta (Conexão Ponderada)"));

        comboConexaoOrigem = new JComboBox<>();
        comboConexaoDestino = new JComboBox<>();
        txtTempoMinutos = new JTextField();
        txtIdLinha = new JTextField();
        txtNomeLinha = new JTextField();
        JButton btnAdicionarConexao = new JButton("Adicionar Conexão");

        btnAdicionarConexao.addActionListener(e -> acaoAdicionarConexao());

        painelConexao.add(new JLabel("Origem:"));
        painelConexao.add(comboConexaoOrigem);
        painelConexao.add(new JLabel("Destino:"));
        painelConexao.add(comboConexaoDestino);
        painelConexao.add(new JLabel("Tempo (minutos):"));
        painelConexao.add(txtTempoMinutos);
        painelConexao.add(new JLabel("ID da Linha:"));
        painelConexao.add(txtIdLinha);
        painelConexao.add(new JLabel("Nome da Linha:"));
        painelConexao.add(txtNomeLinha);
        painelConexao.add(new JLabel(""));
        painelConexao.add(btnAdicionarConexao);

        painelPrincipal.add(painelParada);
        painelPrincipal.add(painelConexao);

        return painelPrincipal;
    }

    private JPanel criarPainelVisualizacaoGrafo() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        areaGrafo = new JTextArea();
        areaGrafo.setEditable(false);
        areaGrafo.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JButton btnAtualizar = new JButton("Atualizar Visualização do Grafo");
        btnAtualizar.addActionListener(e -> atualizarVisualizacaoGrafo());

        painel.add(new JScrollPane(areaGrafo), BorderLayout.CENTER);
        painel.add(btnAtualizar, BorderLayout.SOUTH);

        return painel;
    }

    private void executarBuscaRota(boolean usarDijkstra) {
        Parada origem = (Parada) comboOrigem.getSelectedItem();
        Parada destino = (Parada) comboDestino.getSelectedItem();

        if (origem == null || destino == null) {
            JOptionPane.showMessageDialog(this, "Selecione as paradas de origem e destino.");
            return;
        }

        String resultado = usarDijkstra
                ? servico.calcularRotaMaisRapida(origem.getId(), destino.getId())
                : servico.calcularRotaMenosConexoes(origem.getId(), destino.getId());

        areaResultadoRota.setText(resultado);
        areaResultadoRota.setCaretPosition(0);
    }

    private void acaoAdicionarParada() {
        try {
            int id = Integer.parseInt(txtIdParada.getText().trim());
            String nome = txtNomeParada.getText().trim();
            String endereco = txtEnderecoParada.getText().trim();

            if (nome.isEmpty() || endereco.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha nome e endereço da parada.");
                return;
            }

            boolean adicionou = servico.cadastrarParada(id, nome, endereco);
            if (adicionou) {
                atualizarCombosParadas();
                atualizarVisualizacaoGrafo();
                txtIdParada.setText("");
                txtNomeParada.setText("");
                txtEnderecoParada.setText("");
                lblStatus.setText("Vértice adicionado: " + id + " - " + nome);
                JOptionPane.showMessageDialog(this, "Parada cadastrada com sucesso no grafo!");
            } else {
                JOptionPane.showMessageDialog(this, "Já existe uma parada com o ID " + id + ".");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe um número inteiro válido para o ID.");
        }
    }

    private void acaoAdicionarConexao() {
        try {
            Parada origem = (Parada) comboConexaoOrigem.getSelectedItem();
            Parada destino = (Parada) comboConexaoDestino.getSelectedItem();
            float tempo = Float.parseFloat(txtTempoMinutos.getText().trim().replace(",", "."));
            int idLinha = Integer.parseInt(txtIdLinha.getText().trim());
            String nomeLinha = txtNomeLinha.getText().trim();

            if (origem == null || destino == null) {
                JOptionPane.showMessageDialog(this, "Cadastre paradas antes de criar conexões.");
                return;
            }

            boolean adicionou = servico.cadastrarConexao(
                    origem.getId(), destino.getId(), tempo, idLinha, nomeLinha);

            if (adicionou) {
                atualizarVisualizacaoGrafo();
                txtTempoMinutos.setText("");
                lblStatus.setText("Aresta adicionada: " + origem.getNome() + " -> " + destino.getNome());
                JOptionPane.showMessageDialog(this, "Conexão adicionada com sucesso ao grafo!");
            } else {
                JOptionPane.showMessageDialog(this, "Dados inválidos. Verifique se o tempo é maior que zero.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique os valores numéricos de tempo e ID da linha.");
        }
    }

    private void atualizarCombosParadas() {
        comboOrigem.removeAllItems();
        comboDestino.removeAllItems();
        comboConexaoOrigem.removeAllItems();
        comboConexaoDestino.removeAllItems();

        No<Parada> cursor = servico.listarParadas().getPrimeiro();
        while (cursor != null) {
            Parada p = cursor.getValor();
            comboOrigem.addItem(p);
            comboDestino.addItem(p);
            comboConexaoOrigem.addItem(p);
            comboConexaoDestino.addItem(p);
            cursor = cursor.getProximo();
        }
    }

    private void atualizarVisualizacaoGrafo() {
        areaGrafo.setText(servico.gerarResumoMalhaViaria());
        areaGrafo.setCaretPosition(0);
    }
}
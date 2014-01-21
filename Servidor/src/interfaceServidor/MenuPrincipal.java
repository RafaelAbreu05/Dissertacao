package interfaceServidor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.AbstractCellEditor;
import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import servidorTCP.ServidorTCP;
import sistemaLocalizacao.SistemaLocalizacao;
import baseDados.BaseDados;
import carregarPlanta.ImageFileView;
import carregarPlanta.ImageFilter;
import carregarPlanta.ImagePreview;
import classes.Coordenadas;
import classes.Escala;
import classes.Planta;
import classes.PontoAcesso;
import classes.Sinal;
import java.awt.Font;

@SuppressWarnings("serial")
public class MenuPrincipal extends JPanel {

	/** JPanel */
	private JPanel panel_5;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel panel_6;
	private JPanel panel;
	private JPanel panel_7;
	private JPanel panel_8;
	private JPanel panel_12;
	private JPanel panel2;
	private JPanel panel_10;
	private JPanel panel_14;
	private JPanel panel_15;
	private JPanel panel_16;
	private JPanel panel_19;
	private JPanel panel_2;
	private JPanel panel_9;
	private JPanel panel_13;

	/** Panel */
	private Panel panel_21 = new Panel();
	private Panel panel_11 = new Panel();
	private Panel panel_17 = new Panel();
	private Panel panel_30 = new Panel();

	/** JTextField */
	private JTextField txtNomeDaRede;
	private JTextField SSID;
	private JTextField txtEnderecoMac;
	private JTextField BSSID1;
	private JTextField BSSID2;
	private JTextField textField_4;
	private JTextField BSSID3;
	private JTextField textField_6;
	private JTextField BSSID4;
	private JTextField textField_8;
	private JTextField BSSID5;
	private JTextField textField_12;
	private JTextField BSSID6;
	private JTextField textField_2;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField txtLarguraReal;
	private JTextField txtNomePlanta = new JTextField();
	private JTextField larguraR = new JTextField();
	private JTextField alturaR = new JTextField();
	private JTextField txtDimenses = new JTextField();
	private JTextField dimensoes = new JTextField();;
	private JTextField txtAlturaRealDo;
	private JTextField numCoordenasX = new JTextField();
	private JTextField numCoordenasY = new JTextField();

	/** JButton */
	private JButton adicionaPontoAcesso;
	private JButton iniciarServidor;
	private JButton pararServidor;
	private JButton carregarPlanta;
	private JButton localizar;

	/** JTable */
	private JTable tablePontosAcesso;
	private JTable tablePosicaoPA;
	private JTable tableTreino;

	/** Outros Componentes */
	private static JFrame frame;
	private JTabbedPane tabbedPane;
	private JTextArea messagesArea;
	private JSplitPane splitPane;
	private JFileChooser fc;
	private JLabel estadoServidor;

	/** Outras Variáveis */
	// PanZoom
	private PanZoom imagemPlanta = null;
	private PanZoom imagemPosicionarPA = null;
	private PanZoom imagemTreino = null;
	private PanZoom imagemLocalizacao = null;

	// Servidor
	static ServidorTCP mServer = null;

	// Base de Dados
	private BaseDados bd = null;

	// Package Outros
	public static Planta plant = null;
	public static ArrayList<Coordenadas> coordenadas = new ArrayList<Coordenadas>();
	public static TreeMap<Integer, PontoAcesso> pontosAcesso = new TreeMap<Integer, PontoAcesso>();
	public static TreeMap<Coordenadas, ArrayList<Sinal>> sinais = new TreeMap<Coordenadas, ArrayList<Sinal>>();
	private Image imagem = null;
	private File file = null;
	private Escala escala = null;
	private SistemaLocalizacao sistemaLocalizacao = null;


	/** Conteudo para a Janela */
	public MenuPrincipal() {
		super(new GridLayout(0, 1));

		sistemaLocalizacao = new SistemaLocalizacao();

		// Inciar janela com todos os dados
		construirTabelas();

		// Aceder à BD
		iniciarBD();

		// Inserir dados no Swing (Pontos Acesso, Planta, Sinais, etc)
		mostrarImagem(true);
		try {
			enviarPlanta();
			atualizarPontosAcesso();
			atualizarSinais();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Icons para as Abas
		ImageIcon icon1 = createImageIcon("../imagens/1.png");
		ImageIcon icon2 = createImageIcon("../imagens/2.png");
		ImageIcon icon3 = createImageIcon("../imagens/3.png");
		ImageIcon icon4 = createImageIcon("../imagens/4.png");
		ImageIcon icon5 = createImageIcon("../imagens/5.png");
		ImageIcon icon6 = createImageIcon("../imagens/6.png");

		// Abas
		tabbedPane = new JTabbedPane();

		// Aba 1
		JComponent panel1 = Servidor();
		tabbedPane.addTab("Servidor", icon1, panel1, "Servidor");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		// Aba 2
		JComponent panel2 = PontosAcesso();
		tabbedPane
				.addTab("Pontos de Acesso", icon2, panel2, "Pontos de Acesso");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		// Aba 3
		JComponent panel3 = Planta();
		tabbedPane.addTab("Planta", icon3, panel3, "Planta");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		// Aba 4
		JComponent panel4 = PosicionarPontosAcesso();
		tabbedPane.addTab("Posicionar Pontos Acesso", icon4, panel4,
				"Posicionar Pontos Acesso");
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

		// Aba 5
		JComponent panel5 = FaseTreino();
		tabbedPane.addTab("Fase de Treino", icon5, panel5, "Fase de Treino");
		tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);
		
		// Aba 6
		JComponent panel6 = Localizacao();
		tabbedPane.addTab("Localizacão", icon6, panel6, "Localizacão");
		tabbedPane.setMnemonicAt(5, KeyEvent.VK_6);

		// Adicionar as Abas ao Painel
		add(tabbedPane);

		// Permitir Scrolling Abas.
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

	}

	/** Aba 1 - Servidor */
	protected JComponent Servidor() {
		JPanel ativarServidor = new JPanel(false);
		GridBagLayout gbl_ativarServidor = new GridBagLayout();
		gbl_ativarServidor.columnWidths = new int[] { 0, 0 };
		gbl_ativarServidor.rowHeights = new int[] { 38, 179 };
		gbl_ativarServidor.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_ativarServidor.rowWeights = new double[] { 0.0, 1.0 };
		ativarServidor.setLayout(gbl_ativarServidor);

		panel_7 = new JPanel();
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.insets = new Insets(0, 0, 5, 0);
		gbc_panel_7.fill = GridBagConstraints.BOTH;
		gbc_panel_7.gridx = 0;
		gbc_panel_7.gridy = 0;
		ativarServidor.add(panel_7, gbc_panel_7);
		GridBagLayout gbl_panel_7 = new GridBagLayout();
		gbl_panel_7.columnWidths = new int[] { 165, 164, 0 };
		gbl_panel_7.rowHeights = new int[] { 50, 0 };
		gbl_panel_7.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel_7.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel_7.setLayout(gbl_panel_7);

		panel_8 = new JPanel();
		GridBagConstraints gbc_panel_8 = new GridBagConstraints();
		gbc_panel_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_8.gridwidth = 2;
		gbc_panel_8.gridx = 0;
		gbc_panel_8.gridy = 0;
		panel_7.add(panel_8, gbc_panel_8);
		GridBagLayout gbl_panel_8 = new GridBagLayout();
		gbl_panel_8.columnWidths = new int[] { 216, 62, 219, 0 };
		gbl_panel_8.rowHeights = new int[] { 37, 0 };
		gbl_panel_8.columnWeights = new double[] { 1.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_panel_8.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel_8.setLayout(gbl_panel_8);

		iniciarServidor = new JButton("Iniciar Servidor");
		ImageIcon iconOn = createImageIcon("../imagens/on.png");
		iniciarServidor.setIcon(iconOn);
		iniciarServidor
				.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
		iniciarServidor.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		iniciarServidor
				.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);

		iniciarServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// creates the object OnMessageReceived asked by the TCPServer
				// constructor
				mServer = new ServidorTCP(new ServidorTCP.OnMessageReceived() {
					@Override
					// this method declared in the interface from TCPServer
					// class is implemented here
					// this method is actually a callback method, because it
					// will run every time when it will be called from
					// TCPServer class (at while)
					public void messageReceived(String message) {
						messagesArea.append("\nCliente: " + message);
					}
				});
				messagesArea.append("Servidor Ligado");
				mServer.start();
				estadoServidor.setText("Servidor Ligado");
				ImageIcon iconLigado = createImageIcon("../imagens/ligado.png");
				estadoServidor.setIcon(iconLigado);
				pararServidor.setEnabled(true);
				iniciarServidor.setEnabled(false);
			}
		});
		GridBagConstraints gbc_iniciarServidor = new GridBagConstraints();
		gbc_iniciarServidor.fill = GridBagConstraints.VERTICAL;
		gbc_iniciarServidor.anchor = GridBagConstraints.EAST;
		gbc_iniciarServidor.insets = new Insets(0, 0, 0, 5);
		gbc_iniciarServidor.gridx = 0;
		gbc_iniciarServidor.gridy = 0;
		panel_8.add(iniciarServidor, gbc_iniciarServidor);

		pararServidor = new JButton("Parar Servidor");

		ImageIcon iconOff = createImageIcon("../imagens/off.png");

		pararServidor
				.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
		pararServidor.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		pararServidor
				.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
		pararServidor.setIcon(iconOff);

		pararServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mServer != null) {
					mServer.close();
					messagesArea.setText("");
				}
				estadoServidor.setText("Servidor Desligado");
				ImageIcon iconDesligado = createImageIcon("../imagens/desligado.png");
				estadoServidor.setIcon(iconDesligado);
				pararServidor.setEnabled(false);
				iniciarServidor.setEnabled(true);
			}
		});

		panel2 = new JPanel();
		GridBagConstraints gbc_panel2 = new GridBagConstraints();
		gbc_panel2.insets = new Insets(0, 0, 0, 5);
		gbc_panel2.fill = GridBagConstraints.BOTH;
		gbc_panel2.gridx = 1;
		gbc_panel2.gridy = 0;
		panel_8.add(panel2, gbc_panel2);
		panel2.setToolTipText("Servidor Desligado");
		GridBagLayout gbl_panel2 = new GridBagLayout();
		gbl_panel2.columnWidths = new int[] { 104, 0 };
		gbl_panel2.rowHeights = new int[] { 1, 0 };
		gbl_panel2.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panel2.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel2.setLayout(gbl_panel2);

		estadoServidor = new JLabel("Servidor Desligado");
		GridBagConstraints gbc_estadoServidor = new GridBagConstraints();
		gbc_estadoServidor.fill = GridBagConstraints.VERTICAL;
		gbc_estadoServidor.gridx = 0;
		gbc_estadoServidor.gridy = 0;
		ImageIcon iconDesligado = createImageIcon("../imagens/desligado.png");
		estadoServidor
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		estadoServidor.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		estadoServidor
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		estadoServidor.setIcon(iconDesligado);
		panel2.add(estadoServidor, gbc_estadoServidor);

		GridBagConstraints gbc_pararServidor = new GridBagConstraints();
		gbc_pararServidor.fill = GridBagConstraints.VERTICAL;
		gbc_pararServidor.anchor = GridBagConstraints.WEST;
		gbc_pararServidor.gridx = 2;
		gbc_pararServidor.gridy = 0;
		panel_8.add(pararServidor, gbc_pararServidor);

		panel_12 = new JPanel();
		GridBagConstraints gbc_panel_12 = new GridBagConstraints();
		gbc_panel_12.fill = GridBagConstraints.BOTH;
		gbc_panel_12.gridx = 0;
		gbc_panel_12.gridy = 1;
		ativarServidor.add(panel_12, gbc_panel_12);
		GridBagLayout gbl_panel_12 = new GridBagLayout();
		gbl_panel_12.columnWidths = new int[] { 33, 288, 23, 0 };
		gbl_panel_12.rowHeights = new int[] { 22, -72, 0 };
		gbl_panel_12.columnWeights = new double[] { 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_panel_12.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		panel_12.setLayout(gbl_panel_12);

		textField = new JTextField();
		textField.setText("Log:");
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setBorder(null);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.anchor = GridBagConstraints.NORTH;
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		panel_12.add(textField, gbc_textField);

		messagesArea = new JTextArea();
		messagesArea.setEditable(false);
		GridBagConstraints gbc_messagesArea = new GridBagConstraints();
		gbc_messagesArea.fill = GridBagConstraints.BOTH;
		gbc_messagesArea.gridx = 1;
		gbc_messagesArea.gridy = 1;
		panel_12.add(messagesArea, gbc_messagesArea);
		return ativarServidor;
	}

	/** Aba 2 - Pontos de Acesso */
	protected JComponent PontosAcesso() {

		try {
			enviarPontosAcesso();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		JPanel pontosAcessoPanel = new JPanel(false);
		GridBagLayout gbl_pontosAcessoPanel = new GridBagLayout();
		gbl_pontosAcessoPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_pontosAcessoPanel.rowHeights = new int[] { 52, 202 };
		gbl_pontosAcessoPanel.columnWeights = new double[] { 1.0, 1.0,
				Double.MIN_VALUE };
		gbl_pontosAcessoPanel.rowWeights = new double[] { 0.0, 1.0 };
		pontosAcessoPanel.setLayout(gbl_pontosAcessoPanel);

		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		pontosAcessoPanel.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 123, 90, 22, 60, 65, 0 };
		gbl_panel_1.rowHeights = new int[] { 34, 0 };
		gbl_panel_1.columnWeights = new double[] { 1.0, 1.0, 1.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		panel_5 = new JPanel();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.anchor = GridBagConstraints.NORTH;
		gbc_panel_5.gridwidth = 2;
		gbc_panel_5.insets = new Insets(0, 0, 0, 5);
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 0;
		panel_1.add(panel_5, gbc_panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[] { 184, 219, 0 };
		gbl_panel_5.rowHeights = new int[] { 37, 0 };
		gbl_panel_5.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel_5.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel_5.setLayout(gbl_panel_5);

		panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.insets = new Insets(0, 0, 0, 5);
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 0;
		panel_5.add(panel_4, gbc_panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[] { 115, 0 };
		gbl_panel_4.rowHeights = new int[] { 37, 0, 0 };
		gbl_panel_4.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel_4.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		panel_4.setLayout(gbl_panel_4);

		txtNomeDaRede = new JTextField();
		txtNomeDaRede.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_txtNomeDaRede = new GridBagConstraints();
		gbc_txtNomeDaRede.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNomeDaRede.insets = new Insets(0, 0, 5, 0);
		gbc_txtNomeDaRede.gridx = 0;
		gbc_txtNomeDaRede.gridy = 0;
		panel_4.add(txtNomeDaRede, gbc_txtNomeDaRede);
		txtNomeDaRede.setToolTipText("");
		txtNomeDaRede.setText("Nome da rede (SSID)");
		txtNomeDaRede.setEditable(false);
		txtNomeDaRede.setColumns(2);
		txtNomeDaRede.setBorder(null);

		txtEnderecoMac = new JTextField();
		txtEnderecoMac.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_txtEnderecoMac = new GridBagConstraints();
		gbc_txtEnderecoMac.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnderecoMac.gridx = 0;
		gbc_txtEnderecoMac.gridy = 1;
		panel_4.add(txtEnderecoMac, gbc_txtEnderecoMac);
		txtEnderecoMac.setText("Endereco MAC (BSSID)");
		txtEnderecoMac.setEditable(false);
		txtEnderecoMac.setColumns(2);
		txtEnderecoMac.setBorder(null);

		panel_6 = new JPanel();
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.gridx = 1;
		gbc_panel_6.gridy = 0;
		panel_5.add(panel_6, gbc_panel_6);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[] { 90, 0 };
		gbl_panel_6.rowHeights = new int[] { 37, 0, 0 };
		gbl_panel_6.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel_6.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		panel_6.setLayout(gbl_panel_6);

		SSID = new JTextField();
		GridBagConstraints gbc_SSID = new GridBagConstraints();
		gbc_SSID.fill = GridBagConstraints.HORIZONTAL;
		gbc_SSID.insets = new Insets(0, 0, 5, 0);
		gbc_SSID.gridx = 0;
		gbc_SSID.gridy = 0;
		panel_6.add(SSID, gbc_SSID);
		SSID.setColumns(30);

		panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.anchor = GridBagConstraints.BASELINE_LEADING;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 1;
		panel_6.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[] { 25, 15, 25, 15, 25, 15, 25, 15,
				25, 15, 24, 0 };
		gbl_panel_3.rowHeights = new int[] { 21, 0 };
		gbl_panel_3.columnWeights = new double[] { 0.0, 1.0, 1.0, 1.0, 1.0,
				1.0, 1.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel_3.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_3.setLayout(gbl_panel_3);

		BSSID1 = new JTextField(2);
		BSSID1.setHorizontalAlignment(SwingConstants.CENTER);
		BSSID1.setDocument(new JTextFieldLimit(2));
		BSSID1.addKeyListener(new KeyListener() {

			public void keyReleased(KeyEvent evt) {
				if (BSSID1.getText().length() == 2) {
					BSSID2.requestFocus();
				}
			}

			public void keyPressed(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		BSSID1.setColumns(2);
		GridBagConstraints gbc_BSSID1 = new GridBagConstraints();
		gbc_BSSID1.fill = GridBagConstraints.HORIZONTAL;
		gbc_BSSID1.insets = new Insets(0, 0, 0, 5);
		gbc_BSSID1.gridx = 0;
		gbc_BSSID1.gridy = 0;
		panel_3.add(BSSID1, gbc_BSSID1);

		textField_2 = new JTextField();
		textField_2.setText(":");
		textField_2.setHorizontalAlignment(SwingConstants.CENTER);
		textField_2.setForeground(Color.BLACK);
		textField_2.setEditable(false);
		textField_2.setDropMode(DropMode.INSERT);
		textField_2.setBorder(null);
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 0, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 0;
		panel_3.add(textField_2, gbc_textField_2);

		BSSID2 = new JTextField();
		BSSID2.setHorizontalAlignment(SwingConstants.CENTER);
		BSSID2.setColumns(2);
		BSSID2.setDocument(new JTextFieldLimit(2));
		BSSID2.addKeyListener(new KeyListener() {

			public void keyReleased(KeyEvent evt) {
				if (BSSID2.getText().length() == 2) {
					BSSID3.requestFocus();
				}
			}

			public void keyPressed(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		GridBagConstraints gbc_BSSID2 = new GridBagConstraints();
		gbc_BSSID2.fill = GridBagConstraints.HORIZONTAL;
		gbc_BSSID2.insets = new Insets(0, 0, 0, 5);
		gbc_BSSID2.gridx = 2;
		gbc_BSSID2.gridy = 0;
		panel_3.add(BSSID2, gbc_BSSID2);

		textField_4 = new JTextField();
		textField_4.setText(":");
		textField_4.setHorizontalAlignment(SwingConstants.CENTER);
		textField_4.setForeground(Color.BLACK);
		textField_4.setEditable(false);
		textField_4.setDropMode(DropMode.INSERT);
		textField_4.setBorder(null);
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.insets = new Insets(0, 0, 0, 5);
		gbc_textField_4.gridx = 3;
		gbc_textField_4.gridy = 0;
		panel_3.add(textField_4, gbc_textField_4);

		BSSID3 = new JTextField();
		BSSID3.setHorizontalAlignment(SwingConstants.CENTER);
		BSSID3.setColumns(2);
		BSSID3.setDocument(new JTextFieldLimit(2));
		BSSID3.addKeyListener(new KeyListener() {

			public void keyReleased(KeyEvent evt) {
				if (BSSID3.getText().length() == 2) {
					BSSID4.requestFocus();
				}
			}

			public void keyPressed(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		GridBagConstraints gbc_BSSID3 = new GridBagConstraints();
		gbc_BSSID3.fill = GridBagConstraints.HORIZONTAL;
		gbc_BSSID3.insets = new Insets(0, 0, 0, 5);
		gbc_BSSID3.gridx = 4;
		gbc_BSSID3.gridy = 0;
		panel_3.add(BSSID3, gbc_BSSID3);

		textField_6 = new JTextField();
		textField_6.setText(":");
		textField_6.setHorizontalAlignment(SwingConstants.CENTER);
		textField_6.setForeground(Color.BLACK);
		textField_6.setEditable(false);
		textField_6.setDropMode(DropMode.INSERT);
		textField_6.setBorder(null);
		GridBagConstraints gbc_textField_6 = new GridBagConstraints();
		gbc_textField_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_6.insets = new Insets(0, 0, 0, 5);
		gbc_textField_6.gridx = 5;
		gbc_textField_6.gridy = 0;
		panel_3.add(textField_6, gbc_textField_6);

		BSSID4 = new JTextField();
		BSSID4.setHorizontalAlignment(SwingConstants.CENTER);
		BSSID4.setColumns(2);
		BSSID4.setDocument(new JTextFieldLimit(2));
		BSSID4.addKeyListener(new KeyListener() {

			public void keyReleased(KeyEvent evt) {
				if (BSSID4.getText().length() == 2) {
					BSSID5.requestFocus();
				}
			}

			public void keyPressed(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		GridBagConstraints gbc_BSSID4 = new GridBagConstraints();
		gbc_BSSID4.fill = GridBagConstraints.HORIZONTAL;
		gbc_BSSID4.insets = new Insets(0, 0, 0, 5);
		gbc_BSSID4.gridx = 6;
		gbc_BSSID4.gridy = 0;
		panel_3.add(BSSID4, gbc_BSSID4);

		textField_8 = new JTextField();
		textField_8.setText(":");
		textField_8.setHorizontalAlignment(SwingConstants.CENTER);
		textField_8.setForeground(Color.BLACK);
		textField_8.setEditable(false);
		textField_8.setDropMode(DropMode.INSERT);
		textField_8.setBorder(null);
		GridBagConstraints gbc_textField_8 = new GridBagConstraints();
		gbc_textField_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_8.insets = new Insets(0, 0, 0, 5);
		gbc_textField_8.gridx = 7;
		gbc_textField_8.gridy = 0;
		panel_3.add(textField_8, gbc_textField_8);

		BSSID5 = new JTextField();
		BSSID5.setHorizontalAlignment(SwingConstants.CENTER);
		BSSID5.setColumns(2);
		BSSID5.setDocument(new JTextFieldLimit(2));
		BSSID5.addKeyListener(new KeyListener() {

			public void keyReleased(KeyEvent evt) {
				if (BSSID5.getText().length() == 2) {
					BSSID6.requestFocus();
				}
			}

			public void keyPressed(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		GridBagConstraints gbc_BSSID5 = new GridBagConstraints();
		gbc_BSSID5.fill = GridBagConstraints.HORIZONTAL;
		gbc_BSSID5.insets = new Insets(0, 0, 0, 5);
		gbc_BSSID5.gridx = 8;
		gbc_BSSID5.gridy = 0;
		panel_3.add(BSSID5, gbc_BSSID5);

		textField_12 = new JTextField();
		textField_12.setText(":");
		textField_12.setHorizontalAlignment(SwingConstants.CENTER);
		textField_12.setForeground(Color.BLACK);
		textField_12.setEditable(false);
		textField_12.setDropMode(DropMode.INSERT);
		textField_12.setBorder(null);
		GridBagConstraints gbc_textField_12 = new GridBagConstraints();
		gbc_textField_12.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_12.insets = new Insets(0, 0, 0, 5);
		gbc_textField_12.gridx = 9;
		gbc_textField_12.gridy = 0;
		panel_3.add(textField_12, gbc_textField_12);

		BSSID6 = new JTextField();
		BSSID6.setHorizontalAlignment(SwingConstants.CENTER);
		BSSID6.setColumns(2);
		BSSID6.setDocument(new JTextFieldLimit(2));
		BSSID6.addKeyListener(new KeyListener() {

			public void keyReleased(KeyEvent evt) {
				if (BSSID6.getText().length() == 2) {
					adicionaPontoAcesso.requestFocus();
				}
			}

			public void keyPressed(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		GridBagConstraints gbc_BSSID6 = new GridBagConstraints();
		gbc_BSSID6.fill = GridBagConstraints.HORIZONTAL;
		gbc_BSSID6.gridx = 10;
		gbc_BSSID6.gridy = 0;
		panel_3.add(BSSID6, gbc_BSSID6);

		ImageIcon iconMais = createImageIcon("../imagens/+.png");
		adicionaPontoAcesso = new JButton();
		adicionaPontoAcesso.setIcon(iconMais);
		adicionaPontoAcesso.setText("Ponto de Acesso");

		adicionaPontoAcesso
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		adicionaPontoAcesso
				.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		adicionaPontoAcesso
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

		adicionaPontoAcesso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (SSID.getText().equals("") || BSSID1.getText().equals("")
						|| BSSID2.getText().equals("")
						|| BSSID3.getText().equals("")
						|| BSSID4.getText().equals("")
						|| BSSID5.getText().equals("")
						|| BSSID6.getText().equals("")
						|| BSSID1.getText().length() != 2
						|| BSSID2.getText().length() != 2
						|| BSSID3.getText().length() != 2
						|| BSSID4.getText().length() != 2
						|| BSSID5.getText().length() != 2
						|| BSSID6.getText().length() != 2) {
					JOptionPane
							.showMessageDialog(
									frame,
									"Tem que preencher corretamente tanto o Nome da Rede (SSID) como"
											+ "\ntodos os campos do Endereço MAC (BSSID) do seu Ponto de Acesso!",
									"Erro nos dados introduzidos",
									JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				/*
				JTextField rssid0 = new JTextField(5);
				JTextField d = new JTextField(5);
				JTextField rssid = new JTextField(5);

				GridBagConstraints right = new GridBagConstraints();
				right.weightx = 2.0;
				right.fill = GridBagConstraints.HORIZONTAL;
				right.gridwidth = GridBagConstraints.REMAINDER;

				JPanel myPanel = new JPanel();
				myPanel.setLayout(new GridBagLayout());
				myPanel.add(new JLabel(
						"Potência do sinal (rssi) a 100cm (1 metro): "), right);
				myPanel.add(rssid0, right);
				myPanel.add(new JLabel("Distância em cm: "), right);
				myPanel.add(d, right);
				myPanel.add(new JLabel(
						"Potência do sinal (rssi) à distância introduzida:  "),
						right);
				myPanel.add(rssid, right);
				panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				int result = JOptionPane.showConfirmDialog(frame, myPanel,
						"Variaveis do ponto de acesso",
						JOptionPane.WARNING_MESSAGE);

				int prssid0 = 0, distancia = 0, prssid = 0;

				if (result == JOptionPane.OK_OPTION) {
					boolean b = true;
					try {
						prssid0 = Integer.parseInt(rssid0.getText());
						distancia = Integer.parseInt(d.getText());
						prssid = Integer.parseInt(rssid.getText());
					} catch (NumberFormatException nFE) {
						b = false;
						System.out.println("ERRO");
					}
					if (rssid0.getText().equals("")
							|| rssid.getText().equals("")
							|| d.getText().equals("") || b == false) {
						JOptionPane
								.showMessageDialog(
										frame,
										"Tem que preencher corretamente todas as variaveis do ponto de acesso",
										"Erro nos dados introduzidos",
										JOptionPane.ERROR_MESSAGE);
						return;
					}
				} else {
					JOptionPane
							.showMessageDialog(
									frame,
									"Opção cancelada, o ponto não vai ser adicionado à lista",
									"Adicionar ponto de acesso cancelado",
									JOptionPane.CANCEL_OPTION);
					return;
				}
				double n = sistemaLocalizacao.caracterizacaoAmbiente(prssid0, prssid, distancia);
				
				*/
				
				String BSSID = BSSID1.getText() + ":" + BSSID2.getText() + ":"
						+ BSSID3.getText() + ":" + BSSID4.getText() + ":"
						+ BSSID5.getText() + ":" + BSSID6.getText();


				try {
					adicionarPontoAcesso(SSID.getText().toString(),
							BSSID.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				SSID.setText("");
				BSSID1.setText("");
				BSSID2.setText("");
				BSSID3.setText("");
				BSSID4.setText("");
				BSSID5.setText("");
				BSSID6.setText("");
				return;
			}

		});
		GridBagConstraints gbc_adicionaPontoAcesso = new GridBagConstraints();
		gbc_adicionaPontoAcesso.anchor = GridBagConstraints.NORTHEAST;
		gbc_adicionaPontoAcesso.insets = new Insets(0, 0, 0, 5);
		gbc_adicionaPontoAcesso.gridx = 3;
		gbc_adicionaPontoAcesso.gridy = 0;
		panel_1.add(adicionaPontoAcesso, gbc_adicionaPontoAcesso);

		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		pontosAcessoPanel.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 376, 0 };
		gbl_panel.rowHeights = new int[] { 83, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JScrollPane scrollPane = new JScrollPane(tablePontosAcesso);
		GridBagConstraints gbc_table_1 = new GridBagConstraints();
		gbc_table_1.gridy = 0;
		gbc_table_1.gridx = 0;
		gbc_table_1.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane, gbc_table_1);

		return pontosAcessoPanel;
	}

	/** Aba 3 - Planta */
	protected JComponent Planta() {
		JPanel planta = new JPanel(false);
		GridBagLayout gbl_posicionarPontosAcesso = new GridBagLayout();
		gbl_posicionarPontosAcesso.columnWidths = new int[] { 0 };
		gbl_posicionarPontosAcesso.rowHeights = new int[] { 95, 183, 0 };
		gbl_posicionarPontosAcesso.columnWeights = new double[] { 1.0 };
		gbl_posicionarPontosAcesso.rowWeights = new double[] { 0.0, 1.0,
				Double.MIN_VALUE };
		planta.setLayout(gbl_posicionarPontosAcesso);

		panel_10 = new JPanel();
		GridBagConstraints gbc_panel_10 = new GridBagConstraints();
		gbc_panel_10.anchor = GridBagConstraints.WEST;
		gbc_panel_10.insets = new Insets(0, 0, 5, 0);
		gbc_panel_10.fill = GridBagConstraints.VERTICAL;
		gbc_panel_10.gridx = 0;
		gbc_panel_10.gridy = 0;
		planta.add(panel_10, gbc_panel_10);

		ImageIcon upload = createImageIcon("../imagens/upload.png");
		GridBagLayout gbl_panel_10 = new GridBagLayout();
		gbl_panel_10.columnWidths = new int[] { 321, 297, 297, 0 };
		gbl_panel_10.rowHeights = new int[] { 44, 0 };
		gbl_panel_10.columnWeights = new double[] { 0.0, 1.0, 1.0,
				Double.MIN_VALUE };
		gbl_panel_10.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel_10.setLayout(gbl_panel_10);

		panel_14 = new JPanel();
		GridBagConstraints gbc_panel_14 = new GridBagConstraints();
		gbc_panel_14.insets = new Insets(0, 0, 0, 5);
		gbc_panel_14.fill = GridBagConstraints.BOTH;
		gbc_panel_14.gridx = 0;
		gbc_panel_14.gridy = 0;
		panel_10.add(panel_14, gbc_panel_14);
		GridBagLayout gbl_panel_14 = new GridBagLayout();
		gbl_panel_14.columnWidths = new int[] { 70, 73, 0 };
		gbl_panel_14.rowHeights = new int[] { 0, 0 };
		gbl_panel_14.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_14.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel_14.setLayout(gbl_panel_14);

		panel_15 = new JPanel();
		GridBagConstraints gbc_panel_15 = new GridBagConstraints();
		gbc_panel_15.anchor = GridBagConstraints.WEST;
		gbc_panel_15.insets = new Insets(0, 0, 0, 5);
		gbc_panel_15.fill = GridBagConstraints.VERTICAL;
		gbc_panel_15.gridx = 0;
		gbc_panel_15.gridy = 0;
		panel_14.add(panel_15, gbc_panel_15);
		GridBagLayout gbl_panel_15 = new GridBagLayout();
		gbl_panel_15.columnWidths = new int[] { 155, 0 };
		gbl_panel_15.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panel_15.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel_15.rowWeights = new double[] { 1.0, 1.0, 1.0,
				Double.MIN_VALUE };
		panel_15.setLayout(gbl_panel_15);

		textField_1 = new JTextField();
		textField_1.setText("Nome da planta");
		textField_1.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBorder(null);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.fill = GridBagConstraints.BOTH;
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.gridx = 0;
		gbc_textField_1.gridy = 0;
		panel_15.add(textField_1, gbc_textField_1);

		txtLarguraReal = new JTextField();
		txtLarguraReal.setText("Largura real do edif\u00EDcio (m)");
		txtLarguraReal.setToolTipText("");
		txtLarguraReal.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLarguraReal.setEditable(false);
		txtLarguraReal.setColumns(10);
		txtLarguraReal.setBorder(null);
		GridBagConstraints gbc_txtLarguraReal = new GridBagConstraints();
		gbc_txtLarguraReal.insets = new Insets(0, 0, 5, 0);
		gbc_txtLarguraReal.fill = GridBagConstraints.BOTH;
		gbc_txtLarguraReal.gridx = 0;
		gbc_txtLarguraReal.gridy = 1;
		panel_15.add(txtLarguraReal, gbc_txtLarguraReal);

		txtAlturaRealDo = new JTextField();
		GridBagConstraints gbc_txtAlturaRealDo = new GridBagConstraints();
		gbc_txtAlturaRealDo.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAlturaRealDo.gridx = 0;
		gbc_txtAlturaRealDo.gridy = 2;
		panel_15.add(txtAlturaRealDo, gbc_txtAlturaRealDo);
		txtAlturaRealDo.setToolTipText("");
		txtAlturaRealDo.setText("Altura real do edif\u00EDcio (m)");
		txtAlturaRealDo.setHorizontalAlignment(SwingConstants.RIGHT);
		txtAlturaRealDo.setEditable(false);
		txtAlturaRealDo.setColumns(10);
		txtAlturaRealDo.setBorder(null);

		panel_16 = new JPanel();
		GridBagConstraints gbc_panel_16 = new GridBagConstraints();
		gbc_panel_16.anchor = GridBagConstraints.WEST;
		gbc_panel_16.fill = GridBagConstraints.VERTICAL;
		gbc_panel_16.gridx = 1;
		gbc_panel_16.gridy = 0;
		panel_14.add(panel_16, gbc_panel_16);
		GridBagLayout gbl_panel_16 = new GridBagLayout();
		gbl_panel_16.columnWidths = new int[] { 66, 61, 0 };
		gbl_panel_16.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panel_16.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel_16.rowWeights = new double[] { 1.0, 1.0, 1.0,
				Double.MIN_VALUE };
		panel_16.setLayout(gbl_panel_16);

		txtNomePlanta.setColumns(10);
		GridBagConstraints gbc_txtNomePlanta = new GridBagConstraints();
		gbc_txtNomePlanta.gridwidth = 2;
		gbc_txtNomePlanta.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNomePlanta.insets = new Insets(0, 0, 5, 0);
		gbc_txtNomePlanta.gridx = 0;
		gbc_txtNomePlanta.gridy = 0;
		panel_16.add(txtNomePlanta, gbc_txtNomePlanta);

		larguraR.setColumns(10);
		GridBagConstraints gbc_larguraR = new GridBagConstraints();
		gbc_larguraR.fill = GridBagConstraints.HORIZONTAL;
		gbc_larguraR.insets = new Insets(0, 0, 5, 5);
		gbc_larguraR.gridx = 0;
		gbc_larguraR.gridy = 1;
		panel_16.add(larguraR, gbc_larguraR);
		numCoordenasX.setEditable(false);
		numCoordenasX.setColumns(10);
		GridBagConstraints gbc_numCoordenasX = new GridBagConstraints();
		gbc_numCoordenasX.insets = new Insets(0, 0, 5, 0);
		gbc_numCoordenasX.fill = GridBagConstraints.HORIZONTAL;
		gbc_numCoordenasX.gridx = 1;
		gbc_numCoordenasX.gridy = 1;
		panel_16.add(numCoordenasX, gbc_numCoordenasX);

		GridBagConstraints gbc_alturaR = new GridBagConstraints();
		gbc_alturaR.insets = new Insets(0, 0, 0, 5);
		gbc_alturaR.fill = GridBagConstraints.HORIZONTAL;
		gbc_alturaR.gridx = 0;
		gbc_alturaR.gridy = 2;
		panel_16.add(alturaR, gbc_alturaR);
		alturaR.setColumns(10);

		numCoordenasY.setEditable(false);
		numCoordenasY.setColumns(10);
		GridBagConstraints gbc_numCoordenasY = new GridBagConstraints();
		gbc_numCoordenasY.fill = GridBagConstraints.HORIZONTAL;
		gbc_numCoordenasY.gridx = 1;
		gbc_numCoordenasY.gridy = 2;
		panel_16.add(numCoordenasY, gbc_numCoordenasY);

		panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.gridwidth = 2;
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 0;
		panel_10.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] { 132, 0 };
		gbl_panel_2.rowHeights = new int[] { 44, 44, 0 };
		gbl_panel_2.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel_2.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		panel_2.setLayout(gbl_panel_2);
		carregarPlanta = new JButton("Carregar Planta");
		GridBagConstraints gbc_carregarPlanta = new GridBagConstraints();
		gbc_carregarPlanta.insets = new Insets(0, 0, 5, 0);
		gbc_carregarPlanta.fill = GridBagConstraints.VERTICAL;
		gbc_carregarPlanta.gridx = 0;
		gbc_carregarPlanta.gridy = 0;
		panel_2.add(carregarPlanta, gbc_carregarPlanta);
		carregarPlanta.setIcon(upload);

		panel_9 = new JPanel();
		GridBagConstraints gbc_panel_9 = new GridBagConstraints();
		gbc_panel_9.anchor = GridBagConstraints.WEST;
		gbc_panel_9.fill = GridBagConstraints.VERTICAL;
		gbc_panel_9.gridx = 0;
		gbc_panel_9.gridy = 1;
		panel_2.add(panel_9, gbc_panel_9);
		GridBagLayout gbl_panel_9 = new GridBagLayout();
		gbl_panel_9.columnWidths = new int[] { 150, 150, 0 };
		gbl_panel_9.rowHeights = new int[] { 44, 0 };
		gbl_panel_9.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel_9.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel_9.setLayout(gbl_panel_9);

		txtDimenses = new JTextField();
		GridBagConstraints gbc_txtDimenses = new GridBagConstraints();
		gbc_txtDimenses.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDimenses.insets = new Insets(0, 0, 0, 5);
		gbc_txtDimenses.gridx = 0;
		gbc_txtDimenses.gridy = 0;
		panel_9.add(txtDimenses, gbc_txtDimenses);
		txtDimenses.setText("Dimens\u00F5es");
		txtDimenses.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDimenses.setEditable(false);
		txtDimenses.setColumns(10);
		txtDimenses.setBorder(null);

		GridBagConstraints gbc_dimensoes = new GridBagConstraints();
		gbc_dimensoes.fill = GridBagConstraints.HORIZONTAL;
		gbc_dimensoes.gridx = 1;
		gbc_dimensoes.gridy = 0;
		panel_9.add(dimensoes, gbc_dimensoes);
		dimensoes.setHorizontalAlignment(SwingConstants.CENTER);
		dimensoes.setEditable(false);
		dimensoes.setColumns(9);

		GridBagConstraints gbc_panel_11 = new GridBagConstraints();
		gbc_panel_11.fill = GridBagConstraints.BOTH;
		gbc_panel_11.gridx = 0;
		gbc_panel_11.gridy = 1;
		planta.add(panel_11, gbc_panel_11);
		panel_11.setLayout(new BoxLayout(panel_11, BoxLayout.X_AXIS));

		carregarPlanta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtNomePlanta.getText().equals("")
						|| alturaR.getText().equals("")
						|| larguraR.getText().equals("")) {
					JOptionPane
							.showMessageDialog(
									frame,
									"Tem que preencher corretamente todos os campos antes de carregar a planta!",
									"Erro nos dados introduzidos",
									JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					Double.parseDouble(alturaR.getText());
					Double.parseDouble(larguraR.getText());
				} catch (NumberFormatException n) {
					JOptionPane
							.showMessageDialog(
									frame,
									"As medidas reais (altura e/ou largura) do edifício foram mal introduzidas.\n"
											+ "Apenas são aceites números com duas casas decimas separadas por um ponto.\n"
											+ "Por exemplo: 60, 50.6, etc...",
									"Erro nos dados introduzidos",
									JOptionPane.ERROR_MESSAGE);
					return;
				}
				// Selecionar o ficeiro
				if (fc == null) {
					fc = new JFileChooser();

					// Adicionar um filtro
					fc.addChoosableFileFilter(new ImageFilter());
					fc.setAcceptAllFileFilterUsed(false);

					// Adicionar o icons personalizados aos tipos de ficheiros
					fc.setFileView(new ImageFileView());

					// Adicionar um painel com a imagem
					fc.setAccessory(new ImagePreview(fc));
				}

				// Mostrar
				int returnVal = fc.showDialog(MenuPrincipal.this,
						"Carregar Planta");
				// Processar resultados
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					try {
						boolean first = false;
						if (imagem == null)
							first = true;
						imagem = ImageIO.read(file);
						mostrarImagem(first);
						enviarPlanta();

						plant.setNome(txtNomePlanta.getText());
						plant.setAltura(imagem.getHeight(null));
						plant.setLargura(imagem.getWidth(null));
						plant.setLarguraReal(Double.parseDouble(larguraR
								.getText()));
						plant.setAlturaReal(Double.parseDouble(alturaR
								.getText()));
						plant.setImagemPlanta(file);

						dimensoes.setText(plant.getLargura() + "x"
								+ plant.getAltura());

						numCoordenasX.setText(escala.numCoordenadasX()
								+ " coord.");
						numCoordenasY.setText(escala.numCoordenadasY()
								+ " coord.");

						bd.atualizarPlanta(plant);

					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}
		});
		return planta;
	}

	/** Aba 4 - Posicionar Pontos de Acesso */
	protected JComponent PosicionarPontosAcesso() {
		JPanel pPontosAcesso = new JPanel(false);
		GridBagLayout gbl_pPontosAcesso = new GridBagLayout();
		gbl_pPontosAcesso.columnWidths = new int[] { 445 };
		gbl_pPontosAcesso.rowHeights = new int[] { 67, 99, 0 };
		gbl_pPontosAcesso.columnWeights = new double[] { 1.0 };
		gbl_pPontosAcesso.rowWeights = new double[] { 1.0, 1.0,
				Double.MIN_VALUE };
		pPontosAcesso.setLayout(gbl_pPontosAcesso);

		panel_19 = new JPanel();
		GridBagConstraints gbc_panel_19 = new GridBagConstraints();
		gbc_panel_19.insets = new Insets(0, 0, 5, 0);
		gbc_panel_19.fill = GridBagConstraints.BOTH;
		gbc_panel_19.gridx = 0;
		gbc_panel_19.gridy = 0;

		GridBagLayout gbl_panel_19 = new GridBagLayout();
		gbl_panel_19.columnWidths = new int[] { 445, 0 };
		gbl_panel_19.rowHeights = new int[] { 100, 0 };
		gbl_panel_19.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel_19.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel_19.setLayout(gbl_panel_19);

		JScrollPane scrollPane = new JScrollPane(tablePosicaoPA);
		scrollPane.setEnabled(false);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel_19.add(scrollPane, gbc_scrollPane);

		GridBagConstraints gbc_panel_21 = new GridBagConstraints();
		gbc_panel_21.insets = new Insets(0, 0, 5, 0);
		gbc_panel_21.fill = GridBagConstraints.BOTH;
		gbc_panel_21.gridx = 0;
		gbc_panel_21.gridy = 1;

		panel_21.setLayout(new BoxLayout(panel_21, BoxLayout.X_AXIS));

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(panel_19);
		splitPane.setRightComponent(panel_21);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.gridheight = 2;
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
		pPontosAcesso.add(splitPane, gbc_splitPane);

		return pPontosAcesso;
	}

	/** Aba 5 - Fase de Treino */
	protected JComponent FaseTreino() {
		JPanel faseTreino = new JPanel(false);
		GridBagLayout gbl_faseTreino = new GridBagLayout();
		gbl_faseTreino.columnWidths = new int[] { 445 };
		gbl_faseTreino.rowHeights = new int[] { 112, 161, 0 };
		gbl_faseTreino.columnWeights = new double[] { 1.0 };
		gbl_faseTreino.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		faseTreino.setLayout(gbl_faseTreino);

		panel_14 = new JPanel();
		panel_14.setLayout(new BoxLayout(panel_14, BoxLayout.X_AXIS));

		JScrollPane scrollPane = new JScrollPane(tableTreino);
		scrollPane.setEnabled(false);
		panel_14.add(scrollPane);

		panel_17.setLayout(new BoxLayout(panel_17, BoxLayout.X_AXIS));

		JSplitPane splitPane_1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane_1.setLeftComponent(panel_14);
		splitPane_1.setRightComponent(panel_17);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.gridheight = 2;
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
		faseTreino.add(splitPane_1, gbc_splitPane);
		return faseTreino;
	}
	
	/** Aba 6 - Localização */
	protected JComponent Localizacao() {
		JPanel localizacao = new JPanel(false);
		GridBagLayout gbl_faseTreino = new GridBagLayout();
		gbl_faseTreino.columnWidths = new int[] { 445 };
		gbl_faseTreino.rowHeights = new int[] { 242, 161, 0 };
		gbl_faseTreino.columnWeights = new double[] { 1.0 };
		gbl_faseTreino.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		localizacao.setLayout(gbl_faseTreino);
		
		panel_13 = new JPanel();

		panel_30.setLayout(new BoxLayout(panel_30, BoxLayout.X_AXIS));

		JSplitPane splitPane_2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane_2.setLeftComponent(panel_13);
		splitPane_2.setRightComponent(panel_30);
		panel_13.setLayout(new BorderLayout(0, 0));
		
		localizar = new JButton("Localizar");
		localizar.setFont(new Font("Tahoma", Font.PLAIN, 30));
		
		ImageIcon iconOn = createImageIcon("../imagens/sinal.png");
		localizar.setIcon(iconOn);
		localizar
				.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
		iniciarServidor.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		iniciarServidor
				.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
		
		localizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<Sinal> s = mServer.pedidoSinal();
				imagemLocalizacao.setPosicaoCalculada(null);
				if (!s.isEmpty()) {
					
					imagemLocalizacao.setPosicaoCalculada(sistemaLocalizacao
							.triangulacaoDistancias3(s));
					
					System.out.println(sistemaLocalizacao
							.triangulacaoDistancias3(s).toString());
				}
			}
		});
		panel_13.add(localizar);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.gridheight = 2;
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
		localizacao.add(splitPane_2, gbc_splitPane);
		
		return localizacao;
	}

	// Funcções Auxiliares

	/** Construir Tabelas */
	private void construirTabelas() {

		Object[][] data = {};

		String[] columnNames = { "ID", "Nome da rede (SSID)",
				"Endereço MAC (BSSID)", "Remover" };
		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}
		};
		tablePontosAcesso = new JTable(model);

		String[] columnNames1 = { "ID", "Nome da rede (SSID)",
				"Endereço MAC (BSSID)", "Posição" };
		DefaultTableModel model1 = new DefaultTableModel(data, columnNames1) {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}
		};

		tablePosicaoPA = new JTable(model1);
		tablePosicaoPA.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				// Determinhar linha selecionada
				
				if (imagemPlanta != null && imagemPosicionarPA != null
						&& imagemPosicionarPA.getPX() != 0.0
						&& imagemPosicionarPA.getPY() != 0.0) {
					Coordenadas c = new Coordenadas(imagemPosicionarPA.getPX(),
							imagemPosicionarPA.getPY());
					
					//c =
					// escala.pixelToCoordenadas(imagemPosicionarPA.getPX(),imagemPosicionarPA.getPY());

					// c = escala.coordenadasToPixel(c);

					int id = bd.novasCoordenadas(c);
					c.setID(id);
					
					ListSelectionModel model = tablePosicaoPA
							.getSelectionModel();
					int linha = model.getLeadSelectionIndex();
					pontosAcesso.get(linha + 1).setCoordenadas(c);
					bd.atualizarPontoAcesso(pontosAcesso.get(linha + 1));
					imagemPosicionarPA.setPontosAcesso(pontosAcesso);
					try {
						atualizarPontosAcesso();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		String[] columnNames2 = { "Posição", "Nome da rede (SSID)",
				"Endereço MAC (BSSID)", "Frequência", "Nível", "Erro (m)",
				"Remover" };

		Object[][] dataTreino = {};
		DefaultTableModel model2 = new DefaultTableModel(dataTreino,
				columnNames2) {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}
		};
		tableTreino = new JTable(model2);
		tableTreino.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (tableTreino.getSelectedColumn() != 6
						&& imagemTreino != null && imagemTreino.getPX() != 0.0
						&& imagemTreino.getPY() != 0.0) {
					
					Coordenadas c = new Coordenadas(imagemTreino.getPX(),
							imagemTreino.getPY());
					//Coordenadas c = escala.pixelToCoordenadas(imagemTreino.getPX(),imagemTreino.getPY());

					int id = bd.novasCoordenadas(c);

					
					c.setID(id);


					ArrayList<Sinal> s = mServer.pedidoSinal();
					imagemTreino.setPosicaoCalculada(null);
					if (!s.isEmpty()) {
						imagemTreino.setPosicaoCalculada(sistemaLocalizacao
								.triangulacaoDistancias3(s));

						sinais.put(c, s);
						try {
							atualizarSinais();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		int pHeight = 40;
		tablePosicaoPA.setRowHeight(pHeight);
		tablePontosAcesso.setRowHeight(pHeight);
		tableTreino.setRowHeight(pHeight);

		@SuppressWarnings("unused")
		ButtonColumn buttonColumnPontosAcesso = new ButtonColumn(3,
				tablePontosAcesso);
		@SuppressWarnings("unused")
		ButtonColumn buttonColumnTreino = new ButtonColumn(6, tableTreino);
	}

	/**
	 * Aceder à BD para importar todos os dados sobre: Pontos de Acesso Planta
	 * Sinais etc
	 */
	public void iniciarBD() {
		pontosAcesso = new TreeMap<Integer, PontoAcesso>();
		sinais = new TreeMap<Coordenadas, ArrayList<Sinal>>();

		bd = new BaseDados();
		bd.connectar();
		pontosAcesso = bd.getPontosAcesso();
		plant = bd.getPlanta();
		escala = new Escala(0.5, plant);
		coordenadas = bd.getALLCoordenadas();
		sinais = bd.getAllSinais();

		if (plant.getImagemPlanta() != null) {
			try {

				imagem = ImageIO.read(plant.getImagemPlanta());
				dimensoes.setText(plant.getLargura() + "x" + plant.getAltura());
				txtNomePlanta.setText(plant.getNome().toString());
				larguraR.setText(Double.toString(plant.getLarguraReal())
						.toString());
				alturaR.setText(Double.toString(plant.getAlturaReal())
						.toString());

				numCoordenasX.setText(escala.numCoordenadasX() + " coord.");
				numCoordenasY.setText(escala.numCoordenadasY() + " coord.");
			} catch (IOException e1) {
			}
		}
	}

	/** Desenhar Imagem aba Planta e Posicionar Pontos de Acesso */
	public void mostrarImagem(boolean first) {
		if (!first) {
			imagemPlanta.setPlanta(plant);
			imagemPosicionarPA.setPlanta(plant);
			imagemTreino.setPlanta(plant);
			imagemLocalizacao.setPlanta(plant);
			return;
		}

		imagemPlanta = new PanZoom(plant, 1, pontosAcesso);
		imagemPosicionarPA = new PanZoom(plant, 2, pontosAcesso);
		imagemTreino = new PanZoom(plant, 3, pontosAcesso, sinais);
		imagemLocalizacao = new PanZoom(plant, 4, pontosAcesso, sinais);

		JScrollPane jscrollpane = new JScrollPane(imagemPlanta,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JScrollPane jscrollpane1 = new JScrollPane(imagemPosicionarPA,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JScrollPane jscrollpane2 = new JScrollPane(imagemTreino,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JScrollPane jscrollpane3 = new JScrollPane(imagemLocalizacao,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		GridBagConstraints gbc_jscrollpane = new GridBagConstraints();
		gbc_jscrollpane.fill = GridBagConstraints.BOTH;
		gbc_jscrollpane.gridx = 0;
		gbc_jscrollpane.gridy = 0;
		GridBagConstraints gbc_jscrollpane1 = new GridBagConstraints();
		gbc_jscrollpane.fill = GridBagConstraints.BOTH;
		gbc_jscrollpane.gridx = 0;
		gbc_jscrollpane.gridy = 0;
		GridBagConstraints gbc_jscrollpane2 = new GridBagConstraints();
		gbc_jscrollpane.fill = GridBagConstraints.BOTH;
		gbc_jscrollpane.gridx = 0;
		gbc_jscrollpane.gridy = 0;
		GridBagConstraints gbc_jscrollpane3 = new GridBagConstraints();
		gbc_jscrollpane.fill = GridBagConstraints.BOTH;
		gbc_jscrollpane.gridx = 0;
		gbc_jscrollpane.gridy = 0;

		panel_11.add(jscrollpane, gbc_jscrollpane);
		panel_21.add(jscrollpane1, gbc_jscrollpane1);
		panel_17.add(jscrollpane2, gbc_jscrollpane2);
		panel_30.add(jscrollpane3, gbc_jscrollpane3);
		repaint();
	}

	/** Retorna uma ImageIcon ou null se o caminho for inválido */
	protected static ImageIcon createImageIcon(String path) {
		if (MenuPrincipal.class.getResource(path) != null) {
			return new ImageIcon(MenuPrincipal.class.getResource(path));
		} else {
			System.err.println("Não foi possível encontrar o ficheiro: "
					+ MenuPrincipal.class.getResource(path).getPath());
			return null;
		}
	}

	/** Adicionar o novo Ponto de Acesso às tabelas */
	private void adicionarPontoAcesso(String SSID, String BSSID) throws IOException {

		PontoAcesso p = new PontoAcesso();
		p.setSSID(SSID);
		p.setBSSID(BSSID);

		if (pontosAcesso.containsValue(p)) {
			JOptionPane.showMessageDialog(frame,
					"Este ponto de acesso já existe!",
					"Erro nos dados introduzidos", JOptionPane.ERROR_MESSAGE);
			return;

		}
		Integer i = new Integer(pontosAcesso.size());
		int id = bd.novoPontoAcesso(p);
		p.setID(id);
		pontosAcesso.put(i + 1, p);
		atualizarPontosAcesso();
	}

	/** Enviar Pontos de Acesso no ClienteTCP */
	public void enviarPontosAcesso() throws IOException {
		if (mServer != null) {
			mServer.sendPontosAcesso(pontosAcesso);
			messagesArea.append("\nCliente: Atualizar pontos de Acesso");
		}
	}

	/** Enviar Planta ao ClienteTCP */
	public void enviarPlanta() throws IOException {
		if (mServer != null && file != null) {
			if (mServer != null) {
				mServer.sendFile(file);
				messagesArea.append("\nServidor: Planta enviada com sucesso");
			}
		}
	}

	/** Retorna ImageIcon com número para a tabela */
	public ImageIcon icon(int num) {
		ImageIcon icon = createImageIcon("../imagens/white/" + num
				+ "_white.png");
		return icon;
	}

	/** Atualizar tabelas e Map com os Pontos de Acesso */
	private void atualizarPontosAcesso() throws IOException {
		DefaultTableModel model = (DefaultTableModel) tablePontosAcesso
				.getModel();
		DefaultTableModel model1 = (DefaultTableModel) tablePosicaoPA
				.getModel();

		model.setNumRows(0);
		model1.setNumRows(0);

		TreeMap<Integer, PontoAcesso> aux = new TreeMap<Integer, PontoAcesso>();
		java.util.Iterator<Entry<Integer, PontoAcesso>> it = pontosAcesso
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, PontoAcesso> pontosAcesso = (Entry<Integer, PontoAcesso>) it
					.next();
			Integer i = pontosAcesso.getKey();
			PontoAcesso p = pontosAcesso.getValue();

			ImageIcon icon = icon(i);
			model.addRow(new Object[] { icon, p.getSSID(), p.getBSSID(), "" });
			if (p.getCoordenadas() != null && p.getCoordenadas().getPosX() >= 0
					&& p.getCoordenadas().getPosY() >= 0) {
				model1.addRow(new Object[] {
						icon,
						p.getSSID(),
						p.getBSSID(),
						"(" + p.getCoordenadas().getPosX() + " , "
								+ p.getCoordenadas().getPosY() + ")" });
			} else {
				model1.addRow(new Object[] { icon, p.getSSID(), p.getBSSID(),
						"" });
			}
			aux.put(new Integer(i), p);
		}
		pontosAcesso.clear();
		pontosAcesso
				.putAll((Map<? extends Integer, ? extends PontoAcesso>) aux);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tablePontosAcesso.setDefaultRenderer(String.class, centerRenderer);
		tablePontosAcesso.setModel(model);

		tablePosicaoPA.setDefaultRenderer(String.class, centerRenderer);
		tablePosicaoPA.setModel(model1);

		enviarPontosAcesso();
		atualizarImagens();
	}

	/** Atualizar tabela de Treino com os sinais */
	private void atualizarSinais() throws IOException {
		DefaultTableModel model = (DefaultTableModel) tableTreino.getModel();

		model.setNumRows(0);

		if (!sinais.isEmpty()) {

			java.util.Iterator<Entry<Coordenadas, ArrayList<Sinal>>> it = sinais
					.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Coordenadas, ArrayList<Sinal>> aux = (Entry<Coordenadas, ArrayList<Sinal>>) it
						.next();

				Coordenadas coord = aux.getKey();
				ArrayList<Sinal> sinaisArray = aux.getValue();

				String stringCoord = "(" + coord.getPosX() + " , "
						+ coord.getPosY() + ")";

				Coordenadas coordenadaCalculada = new Coordenadas();
				coordenadaCalculada = sistemaLocalizacao
						.triangulacaoDistancias3(sinaisArray);

				for (Sinal sinal : sinaisArray) {
					//String distanciaReal = "Desconhecido";
					//String distanciaCalculada = "Desconhecido";
					String erro = "Desconhecido";
					if (sinal.getPontoAcesso().getCoordenadas() != null
							&& coordenadaCalculada != null) {
						DecimalFormat df = new DecimalFormat("#.##");
						
						/*
						double distanciaC = sistemaLocalizacao.ditancia(coord,
								coordenadaCalculada, escala);
						double distanciaR = sistemaLocalizacao.ditancia(sinal
								.getPontoAcesso().getCoordenadas(), coord,
								escala);

						distanciaCalculada = df.format(distanciaC) + "";
						distanciaReal = df.format(distanciaR) + "";
						*/
						erro = df.format(Math.abs(sistemaLocalizacao.ditancia(coord,
								coordenadaCalculada, escala)))
								+ "";
					}

					PontoAcesso p = sinal.getPontoAcesso();
					model.addRow(new Object[] { stringCoord, p.getSSID(),
							p.getBSSID(), sinal.getFrequencia(),
							sinal.getNivel(), erro, "" });
				}
			}
		} else {
			model.addRow(new Object[] { "", "", "", "", "", "", "" });
		}
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tableTreino.setDefaultRenderer(String.class, centerRenderer);
		tableTreino.setDefaultRenderer(Integer.class, centerRenderer);
		tableTreino.setModel(model);

		bd.atualizarSinais(sinais);
		atualizarImagens();
	}

	public void atualizarImagens() {
		imagemPlanta.setPontosAcesso(pontosAcesso);
		imagemPosicionarPA.setPontosAcesso(pontosAcesso);
		imagemTreino.setPontosAcesso(pontosAcesso);

		imagemPlanta.setSinais(sinais);
		imagemPosicionarPA.setSinais(sinais);
		imagemTreino.setSinais(sinais);
	}

	/** Classe Auxiliar que permite ter um butão numa coluna da tabela */
	class ButtonColumn extends AbstractCellEditor implements TableCellRenderer,
			TableCellEditor, ActionListener {
		JButton renderButton;
		JButton editButton;
		JTable table;
		String text;

		public ButtonColumn(int column, JTable table) {
			super();
			this.table = table;
			ImageIcon iconX = createImageIcon("../imagens/x.png");
			renderButton = new JButton();
			renderButton.setIcon(iconX);

			editButton = new JButton();
			editButton.setIcon(iconX);
			editButton.setFocusPainted(false);
			editButton.addActionListener(this);

			TableColumnModel columnModel = this.table.getColumnModel();
			columnModel.getColumn(column).setCellRenderer(this);
			columnModel.getColumn(column).setCellEditor(this);
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (hasFocus) {
				renderButton.setForeground(this.table.getForeground());
				renderButton.setBackground(UIManager
						.getColor("Button.background"));
			} else if (isSelected) {
				renderButton.setForeground(this.table.getSelectionForeground());
				renderButton.setBackground(this.table.getSelectionBackground());
			} else {
				renderButton.setForeground(this.table.getForeground());
				renderButton.setBackground(UIManager
						.getColor("Button.background"));
			}

			renderButton.setText((value == null) ? "" : value.toString());
			return renderButton;
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			return editButton;
		}

		public Object getCellEditorValue() {
			return text;
		}

		public void actionPerformed(ActionEvent e) {
			int linha = this.table.getSelectedRow();

			if (this.table.equals(tablePontosAcesso)) {

				fireEditingStopped();
				Integer i = new Integer(linha);
				bd.removerPontoAcesso(pontosAcesso.get(i+1).getID());
				pontosAcesso.remove(i+1);
				try {
					atualizarPontosAcesso();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (this.table.equals(tableTreino)) {

				fireEditingStopped();
				Integer i = new Integer(linha + 1);
				sinais = bd.removerSinal(i, sinais);
				try {
					atualizarSinais();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/** Criar e mostrar a Interface */
	private static void createAndShowGUI() {
		// Criar uma Janela
		frame = new JFrame("Servidor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Adicionar Conteudo à janela
		frame.getContentPane().add(new MenuPrincipal(), BorderLayout.CENTER);

		// Mostrar Janela
		frame.pack();
		frame.setSize(710, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/** Main */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}
}

package interfaceServidor;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import classes.Coordenadas;
import classes.Planta;
import classes.PontoAcesso;
import classes.Sinal;

@SuppressWarnings("serial")
public class PanZoom extends JComponent {

	/** Variáveis de Insância */
	private double currentX;
	private double currentY;
	private double previousX;
	private double previousY;
	private double zoom = 1;
	private Graphics2D g2d;
	private Double centerX;
	private Double centerY;
	private int testeX = -1;
	private int testeY = -1;
	private Planta planta;
	private Coordenadas posicaoCalculada = null;
	private BufferedImage icon = null;
	private TreeMap<Integer, PontoAcesso> pontosAcesso;
	private TreeMap<Coordenadas, ArrayList<Sinal>> sinais;

	/** Construtores */
	public PanZoom(Planta planta, int pos,
			TreeMap<Integer, PontoAcesso> pontosAcesso) {
		this.planta = planta;
		this.pontosAcesso = pontosAcesso;
		this.sinais = new TreeMap<Coordenadas, ArrayList<Sinal>>();

		adicionarListerners(pos);
	}

	public PanZoom(Planta planta, int pos,
			TreeMap<Integer, PontoAcesso> pontosAcesso,
			TreeMap<Coordenadas, ArrayList<Sinal>> sinais) {
		this.planta = planta;
		this.pontosAcesso = pontosAcesso;
		this.sinais = sinais;

		adicionarListerners(pos);
	}

	/** Gets */
	public int getPX() {
		return testeX;
	}

	public int getPY() {
		return testeY;
	}

	/** Sets */
	public void setPlanta(Planta planta) {
		this.planta = planta;
		repaint();
	}

	public void setIcon(int linha) {
		try {
			icon = ImageIO.read(MenuPrincipal.class
					.getResource("../imagens/white/" + (linha + 1)
							+ "_white.png"));
			repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setPontosAcesso(TreeMap<Integer, PontoAcesso> pontosAcesso) {
		this.pontosAcesso = pontosAcesso;
		testeX = -1;
		testeY = -1;
		repaint();
	}

	public void setSinais(TreeMap<Coordenadas, ArrayList<Sinal>> sinais) {
		this.sinais = sinais;
		testeX = -1;
		testeY = -1;
		repaint();
	}

	public void setPosicaoCalculada(Coordenadas posicaoCalculada) {
		if (posicaoCalculada != null)
			this.posicaoCalculada = posicaoCalculada;
		repaint();
	}

	/** Funções Auxiliares */
	public void adicionarListerners(int pos) {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				previousX = e.getX();
				previousY = e.getY();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {

				Point2D adjPreviousPoint = getTranslatedPoint(previousX,
						previousY);
				Point2D adjNewPoint = getTranslatedPoint(e.getX(), e.getY());

				double newX = adjNewPoint.getX() - adjPreviousPoint.getX();
				double newY = adjNewPoint.getY() - adjPreviousPoint.getY();

				previousX = e.getX();
				previousY = e.getY();

				currentX += newX;
				currentY += newY;

				repaint();
			}
		});

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					zoom = 1;
					repaint();
				}
			}
		});

		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
					incrementZoom(.1 * -(double) e.getWheelRotation());
				}
			}
		});

		if (pos == 2 || pos == 3) {
			addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (e.isMetaDown()) {
						Point2D adjNewPoint = getTranslatedPoint(e.getX(),
								e.getY());
						if (adjNewPoint.getX() >= 0 && adjNewPoint.getY() >= 0
								&& adjNewPoint.getX() <= planta.getLargura()
								&& adjNewPoint.getY() <= planta.getAltura()) {
							testeX = (int) adjNewPoint.getX();
							testeY = (int) adjNewPoint.getY();
							repaint();
						}
					}
				}
			});
		}
		setOpaque(false);
	}

	private void incrementZoom(double amount) {
		double test = zoom + amount;
		if (test >= 0.2 && test <= 3) {
			zoom += amount;
			zoom = Math.max(0.00001, zoom);
			repaint();
		}

	}

	public void paintComponent(Graphics g) {

		g2d = (Graphics2D) g.create();
		AffineTransform tx = g2d.getTransform();

		double centerX = (double) getWidth() / 2;
		double centerY = (double) getHeight() / 2;
		tx.translate(centerX, centerY);
		tx.scale(zoom, zoom);
		tx.translate(currentX, currentY);

		g2d.setTransform(tx);

		try {
			g2d.drawImage(ImageIO.read(planta.getImagemPlanta()), 0, 0, this);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (testeX >= 0 && testeY >= 0) {
			desenharPin();
		}
		if (!pontosAcesso.isEmpty()) {
			desenharPontosAcesso();
		}
		if (sinais != null || !sinais.isEmpty()) {
			desenharSinais();
		}
		if (posicaoCalculada != null) {
			desenharPosicaoCalculada();
		}
		g2d.dispose();
	}

	/** Desenhar Sinais nas plantas */
	public void desenharSinais() {
		int iconLarg = 0, iconAlt = 0;
		Iterator<Entry<Coordenadas, ArrayList<Sinal>>> it = sinais.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<Coordenadas, ArrayList<Sinal>> sinaisAux = (Entry<Coordenadas, ArrayList<Sinal>>) it
					.next();

			Coordenadas coord = sinaisAux.getKey();
			if (coord != null && coord.getPosX() != 0 && coord.getPosY() != 0) {
				BufferedImage ic;
				try {
					ic = ImageIO.read(MenuPrincipal.class
							.getResource("../imagens/sinal.png"));

					int pX = coord.getPosX();
					int pY = coord.getPosY();

					iconLarg = ic.getWidth(null);
					iconAlt = ic.getHeight(null);
					g2d.drawImage(ic, pX - (iconLarg / 2), pY - iconAlt, this);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	/** Desenhar Pontos Acesso nas plantas */
	public void desenharPontosAcesso() {
		int iconLarg = 0, iconAlt = 0;
		for (Integer i : pontosAcesso.keySet()) {
			PontoAcesso p = pontosAcesso.get(i);
			if (p.getCoordenadas() != null) {
				BufferedImage ic;
				try {
					ic = ImageIO
							.read(MenuPrincipal.class
									.getResource("../imagens/white/" + i
											+ "_white.png"));
					if (p.getCoordenadas().getPosX() >= 0
							&& p.getCoordenadas().getPosY() >= 0) {
						int pX = p.getCoordenadas().getPosX();
						int pY = p.getCoordenadas().getPosY();

						iconLarg = ic.getWidth(null);
						iconAlt = ic.getHeight(null);
						g2d.drawImage(ic, pX - (iconLarg / 2), pY
								- (iconAlt / 2), this);
					}
					i++;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** Desenhar Pin nas plantas */
	public void desenharPin() {
		int iconLarg = 0, iconAlt = 0;
		try {
			icon = ImageIO.read(MenuPrincipal.class
					.getResource("../imagens/pin2.png"));
			iconLarg = icon.getWidth(null);
			iconAlt = icon.getHeight(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g2d.drawImage(icon, (testeX - (iconLarg / 2)), testeY - iconAlt, this);
	}

	/** Desenhar Posicao Calculada */
	public void desenharPosicaoCalculada() {
		int iconLarg = 0, iconAlt = 0;
		try {
			icon = ImageIO.read(MenuPrincipal.class
					.getResource("../imagens/x.png"));
			iconLarg = icon.getWidth(null);
			iconAlt = icon.getHeight(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g2d.drawImage(icon, (posicaoCalculada.getPosX() - (iconLarg / 2)),
				(posicaoCalculada.getPosY() - (iconAlt / 2)), this);
	}

	private AffineTransform getCurrentTransform() {

		AffineTransform tx = new AffineTransform();

		centerX = (double) getWidth() / 2;
		centerY = (double) getHeight() / 2;

		tx.translate(centerX, centerY);
		tx.scale(zoom, zoom);
		tx.translate(currentX, currentY);

		return tx;

	}

	private Point2D getTranslatedPoint(double panelX, double panelY) {

		AffineTransform tx = getCurrentTransform();
		Point2D point2d = new Point2D.Double(panelX, panelY);
		try {
			return tx.inverseTransform(point2d, null);
		} catch (NoninvertibleTransformException ex) {
			ex.printStackTrace();
			return null;
		}

	}
}
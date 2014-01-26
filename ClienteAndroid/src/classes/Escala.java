package classes;

public class Escala {

	/** Variáveis de Instância */
	private double escala;
	private Planta planta;

	/** Construtores */
	public Escala() {
		this.escala = 0.5;
		this.planta = null;
	}

	public Escala(double escala, Planta planta) {
		this.escala = escala;
		this.planta = planta;
	}

	/** Get */
	public double getEscala() {
		return escala;
	}

	public Planta getPlanta() {
		return planta;
	}

	/** Set */
	public void setEscala(double escala) {
		this.escala = escala;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	/** Funções */
	public double escala() {
		double escalaX = planta.getLarguraReal() / planta.getLargura();
		double escalaY = planta.getAlturaReal() / planta.getAltura();
		double e = (escalaX + escalaY) / 2;
		return e;
	}

	// Escala X e Y
	public int escalaX() {
		double escalaX = (escala * planta.getLargura())
				/ planta.getLarguraReal();

		if (((escala * planta.getLargura()) / planta.getLarguraReal()) != 0)
			escalaX += 1;

		return (int) escalaX;
	}

	public int escalaY() {
		double escalaY = (escala * planta.getAltura()) / planta.getAlturaReal();

		if (((escala * planta.getAltura()) % planta.getAlturaReal()) != 0)
			escalaY += 1;
		return (int) escalaY + 1;
	}

	// Número de coordenasas X e Y
	public int numCoordenadasX() {
		double numCoordenadaX = planta.getLargura() / escalaX();
		if ((planta.getAltura() % escalaX()) != 0)
			numCoordenadaX += 1;
		return (int) numCoordenadaX;
	}

	public int numCoordenadasY() {

		double numCoordenadaY = planta.getAltura() / escalaY();
		if ((planta.getAltura() % escalaY()) != 0)
			numCoordenadaY += 1;
		return (int) numCoordenadaY;
	}

	// Conversões
	// Pixel -> Coordenadas
	public Coordenadas pixelToCoordenadas(int x_pixeis, int y_pixeis) {

		double coordenadaX = x_pixeis / escalaX();
		double coordenadaY = y_pixeis / escalaY();

		if ((x_pixeis % escalaX()) != 0)
			coordenadaX += 1;
		if ((y_pixeis % escalaY()) != 0)
			coordenadaY += 1;

		Coordenadas c = new Coordenadas();
		c.setPosX((int) coordenadaX);
		c.setPosY((int) coordenadaY);
		return coordenadasToPixel(c);
	}

	// Coordenadas -> Pixel
	public Coordenadas coordenadasToPixel(Coordenadas c) {
		Coordenadas pixeis = new Coordenadas();
		int x_pixeis, y_pixeis;
		System.out.println(c.getPosX());
		System.out.println(numCoordenadasX());
		if (c.getPosX() == numCoordenadasX())
			x_pixeis = (escalaX() * (numCoordenadasX() - 1))
					+ (planta.getLargura() - (escalaX() * (numCoordenadasX() - 1)))
					/ 2;
		else
			x_pixeis = (c.getPosX() * escalaX()) - (escalaX() / 2);

		if (c.getPosY() == numCoordenadasY())
			y_pixeis = escalaY()
					* (numCoordenadasY() - 1)
					+ (planta.getAltura() - (escalaY() * (numCoordenadasY() - 1)))
					/ 2;
		else
			y_pixeis = (c.getPosY() * escalaY()) - (escalaY() / 2);

		System.out.println(x_pixeis);

		pixeis.setPosX(x_pixeis);
		pixeis.setPosY(y_pixeis);
		return pixeis;
	}
}

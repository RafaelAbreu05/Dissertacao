package classes;

import java.io.File;

public class Planta {

	/** Variáveis de Instância */
	private int _id;
	private String nome;
	private int altura;
	private int largura;
	private double alturaReal;
	private double larguraReal;
	private File imagemPlanta;

	/** Construtores */
	public Planta() {
		this._id = 0;
		this.nome = null;
		this.altura = 0;
		this.largura = 0;
		this.alturaReal = 0.0;
		this.larguraReal = 0.0;
		this.imagemPlanta = null;
	}

	public Planta(String nome, int altura, int largura, double alturaReal,
			double larguraReal, File imagemPlanta) {
		this._id = 0;
		this.nome = nome;
		this.altura = altura;
		this.largura = largura;
		this.alturaReal = alturaReal;
		this.larguraReal = larguraReal;
		this.imagemPlanta = imagemPlanta;
	}

	public Planta(int _id, String nome, int altura, int largura,
			double alturaReal, double larguraReal, File imagemPlanta) {
		this._id = _id;
		this.nome = nome;
		this.altura = altura;
		this.largura = largura;
		this.alturaReal = alturaReal;
		this.larguraReal = larguraReal;
		this.imagemPlanta = imagemPlanta;
	}

	/** Gets */
	public int getID() {
		return _id;
	}

	public String getNome() {
		return nome;
	}

	public int getAltura() {
		return altura;
	}

	public int getLargura() {
		return largura;
	}

	public double getAlturaReal() {
		return alturaReal;
	}

	public double getLarguraReal() {
		return larguraReal;
	}

	public File getImagemPlanta() {
		return imagemPlanta;
	}

	/** Sets */
	public void setID(int _id) {
		this._id = _id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}

	public void setLargura(int largura) {
		this.largura = largura;
	}

	public void setAlturaReal(double alturaReal) {
		this.alturaReal = alturaReal;
	}

	public void setLarguraReal(double larguraReal) {
		this.larguraReal = larguraReal;
	}

	public void setImagemPlanta(File imagemPlanta) {
		this.imagemPlanta = imagemPlanta;
	}

	/** Equals */
	public boolean equals(Object o) {
		Planta planta = (Planta) o;
		if (planta.getNome().equals(this.nome)
				&& planta.getImagemPlanta().equals(this.imagemPlanta))
			return true;
		return false;
	}
}

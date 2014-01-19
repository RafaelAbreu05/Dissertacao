package classes;

import java.io.Serializable;

public class Coordenadas implements Serializable, Comparable<Coordenadas> {

	private static final long serialVersionUID = 1L;

	/** Variáveis de Instância */
	private int _id;
	private int posX;
	private int posY;

	/** Construtores */
	public Coordenadas() {
		_id = 0;
		posX = -1;
		posY = -1;
	}

	public Coordenadas(int x, int y) {
		_id = 0;
		posX = x;
		posY = y;
	}

	public Coordenadas(int id, int x, int y) {
		_id = id;
		posX = x;
		posY = y;
	}

	/** Gets */
	public int getID() {
		return _id;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	/** Sets */
	public void setID(int id) {
		this._id = id;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	/** Equals */
	public boolean equals(Object o) {
		Coordenadas x = (Coordenadas) o;
		if((x.getPosX() == this.posX) && (x.getPosY() == this.posY))
			return true;
		return false;
	}

	/** ToString */
	public String toString() {
		if (posX != 0 && posX != 0 && _id != 0 )
			return ("Coordenada " + _id + " - ( " + posX + " , "
					+ posY + " )");
		return "Coordenada - Desconhecida";
	}

	/** Comparable */
	public int compareTo(Coordenadas c) {
		return c.getID() - this._id;
	}
}

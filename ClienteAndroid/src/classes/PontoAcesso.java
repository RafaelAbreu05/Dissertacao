package classes;

import java.io.Serializable;

public class PontoAcesso implements Serializable, Comparable<PontoAcesso>{

	private static final long serialVersionUID = 1L;

	/** Variáveis de Instância */
	private int _id;
	private String BSSID;
	private String SSID;
	private Coordenadas local;

	/** Construtores */
	public PontoAcesso() {
		this._id = 0;
		this.BSSID = null;
		this.SSID = null;
		this.local = null;
	}

	public PontoAcesso(String BSSID, String SSID) {
		this._id = 0;
		this.BSSID = BSSID;
		this.SSID = SSID;
		this.local = null;
	}

	public PontoAcesso(String BSSID, String SSID, Coordenadas coordenada) {
		this._id = 0;
		this.BSSID = BSSID;
		this.SSID = SSID;
		this.local = coordenada;
	}

	public PontoAcesso(int id, String BSSID, String SSID, Coordenadas coordenada) {
		this._id = id;
		this.BSSID = BSSID;
		this.SSID = SSID;
		this.local = coordenada;
	}

	/** Gets */
	public int getID() {
		return _id;
	}

	public String getBSSID() {
		return BSSID;
	}

	public String getSSID() {
		return SSID;
	}

	public Coordenadas getCoordenadas() {
		return local;
	}

	/** Sets */
	public void setID(int id) {
		_id = id;
	}

	public void setBSSID(String bSSID) {
		BSSID = bSSID;
	}

	public void setSSID(String sSID) {
		SSID = sSID;
	}
	
	public void setCoordenadas(Coordenadas coordenada) {
		this.local = coordenada;
	}

	/** Equals */
	public boolean equals(Object o) {
		PontoAcesso pontoAcesso = (PontoAcesso) o;
		if (pontoAcesso.getBSSID().equals(this.BSSID))
			return true;
		return false;
	}

	/** ToString */
	public String toString() {
		if (SSID != null && BSSID != null && _id != 0)
			return SSID + " - " + BSSID + " - " + "\n\t" + local.toString();
		return "Ponto Acesso - Desconhecido";
	}
	
	/** Comparable */
	public int compareTo(PontoAcesso p) {
		return p.getID() - this._id;
	}
}

package classes;

import java.io.Serializable;

public class Sinal implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Variáveis de instância */
	private PontoAcesso pontoAcesso;
	private int frequencia;
	private int nivel;

	/** Construtores */
	public Sinal() {
		this.pontoAcesso = new PontoAcesso();
		this.frequencia = 0;
		this.nivel = 0;
	}

	public Sinal(int frequencia, int nivel) {
		this.pontoAcesso = null;
		this.frequencia = frequencia;
		this.nivel = nivel;
	}

	public Sinal(PontoAcesso pa, int frequencia, int nivel) {
		this.pontoAcesso = pa;
		this.frequencia = frequencia;
		this.nivel = nivel;
	}

	/** Gets */
	public PontoAcesso getPontoAcesso() {
		return pontoAcesso;
	}

	public int getFrequencia() {
		return frequencia;
	}

	public int getNivel() {
		return nivel;
	}

	/** Sets */
	public void setPontoAcesso(PontoAcesso pontoAcesso) {
		this.pontoAcesso = pontoAcesso;
	}

	public void setFrequencia(int frequencia) {
		this.frequencia = frequencia;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	/** Equals */
	public boolean equals(Object o) {
		Sinal sinal = (Sinal) o;
		if (sinal.getFrequencia() == this.frequencia
				&& sinal.getNivel() == this.nivel)
			return true;
		return false;
	}

	/** ToString */
	public String toString() {
		if (this.frequencia != 0 && this.nivel != 0)
			return ("Frequência: " + frequencia + "\n" + "Nível do Sinal: " + nivel);
		return ("Frequência: desconhecida\n" + "Nível do Sinal: desconhecido");
	}
}

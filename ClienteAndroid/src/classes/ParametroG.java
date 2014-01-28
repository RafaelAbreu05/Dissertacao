package classes;

import java.text.DecimalFormat;

public class ParametroG {

	/** Variáveis de Instância */
	private double g;
	private double erroMin;
	private double erroMax;
	private double erroSum;
	private int numMedicoes;

	/** Construtores */
	public ParametroG() {
		g = -1;
		erroMin = Double.MAX_VALUE;
		erroMax = Double.MIN_VALUE;
		erroSum = 0;
		numMedicoes = 0;
	}

	public ParametroG(double g) {
		this.g = g;
		erroMin = Double.MAX_VALUE;
		erroMax = Double.MIN_VALUE;
		erroSum = 0;
		numMedicoes = 0;
	}

	public ParametroG(double g, double erroMin, double erroMax, double erroSum,
			int numMedicoes) {
		this.g = g;
		this.erroMin = erroMin;
		this.erroMax = erroMax;
		this.erroSum = erroSum;
		this.numMedicoes = numMedicoes;
	}

	/** Gets */
	public double getG() {
		return g;
	}

	public double getErroMin() {
		return erroMin;
	}

	public double getErroMax() {
		return erroMax;
	}

	public double getErroSum() {
		return erroSum;
	}

	public int getNumMedicoes() {
		return numMedicoes;
	}

	/** Sets */
	public void setG(double g) {
		this.g = g;
	}

	public void setErroMin(double erroMin) {
		this.erroMin = erroMin;
	}

	public void setErroMax(double erroMax) {
		this.erroMax = erroMax;
	}

	public void setErroSum(double erroSum) {
		this.erroSum = erroSum;
	}

	public void setNumMedicoes(int numMedicoes) {
		this.numMedicoes = numMedicoes;
	}

	public void atualizarErro_Medicoes(double erro) {
		this.erroSum += erro;
		this.numMedicoes += 1;
	}

	/** ToString */
	public String toString() {
		DecimalFormat df = new DecimalFormat("#.##");
		if (g != -1) {
			double media = erroSum / numMedicoes;
			return ("|\t" + g + "\t\t|\t" + df.format(erroMin) + "\t\t|\t"
					+ df.format(erroMax) + "\t\t|\t" + df.format(erroSum)
					+ "\t\t|\t" + df.format(media) + "\t\t|");
		}
		return "Parametro g desconhecido";
	}
}

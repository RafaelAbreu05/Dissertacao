package sistemaLocalizacao;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import classes.Coordenadas;
import classes.DataSinais;
import classes.Escala;
import classes.ParametroG;
import classes.Sinal;

public class SistemaLocalizacao {
	/*
	 * protected float g = 1.3f;
	 * 
	 * 
	 * public void setG(float g) { this.g = g; }
	 */

	private Escala escala;

	public SistemaLocalizacao(Escala escala) {
		this.escala = escala;
	}

	/** PARAMETRO G */
	// Método para iniciar o conjunto de parametros utilizados nos testes [0-5]
	// em intervalos de 0.2
	public ArrayList<ParametroG> popularParametroG() {
		ArrayList<ParametroG> parametrosG = new ArrayList<ParametroG>();

		BigDecimal step = new BigDecimal("0.1");
		for (BigDecimal value = BigDecimal.ZERO; value.compareTo(BigDecimal
				.valueOf(3)) <= 0; value = value.add(step)) {
			parametrosG.add(new ParametroG(value.doubleValue()));
		}

		return parametrosG;
	}

	// Atualizar parametros g para cada conjunto de sinais da base de dados
	public ArrayList<ParametroG> atualizarParametrosG(Coordenadas coordReal,
			ArrayList<Sinal> sinaisArray, ArrayList<ParametroG> parametrosG,
			int tipo) {
		// Calcular o erro das distâncias (Real e Calculada) para todos os
		// parametro de g
		for (ParametroG parametroG : parametrosG) {

			// Calcular posição através do algoritmo para um g específico
			Coordenadas coordCalculada = algoritmoLocalizacao(sinaisArray,
					parametroG.getG(), tipo);
			// Calcular erro entre a distância real e a calculada para o g
			// específico
			if (coordReal != null && coordCalculada != null) {
				double erro = distancia(coordReal, coordCalculada);
				// double sum = parametroG.getErroSum()+erro;
				// Atualizar variaveis do parametro (erro mínimo, erro máximo,
				// sumatório dos erro e número de medições (estes últimos
				// utilizados para calcular a média do erro para cada g))
				if (erro < parametroG.getErroMin())
					parametroG.setErroMin(erro);
				if (erro > parametroG.getErroMax())
					parametroG.setErroMax(erro);
				parametroG.atualizarErro_Medicoes(erro);
			}
		}

		return parametrosG;
	}

	// Calcular o parametro de g otimo do conjunto de parametos g

	// Descobrir o melhor parametro g (menor média de erros) double
	public double parametroG_otimo(ArrayList<ParametroG> parametrosG) {
		double g_otimo = 0;
		double menor_erro = Double.MAX_VALUE;
		for (ParametroG parametroG : parametrosG) {
			double media_erro = parametroG.getErroSum()
					/ parametroG.getNumMedicoes();
			if (media_erro < menor_erro) {
				menor_erro = media_erro;
				g_otimo = parametroG.getG();
			}
		}
		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println("Parâmetro G otimo: " + g_otimo + " (Erro: "
				+ df.format(menor_erro) + " metros)");
		return g_otimo;
	}

	// Imprimir estatisticas do parametro g
	public void estatisticas(ArrayList<ParametroG> parametrosG) {
		System.out
				.println("|\tParametro g\t|\tErro Mín.\t|\tErro Máx.\t|\tSumatório\t|\tErro Médio\t|");
		for (ParametroG parametroG : parametrosG) {
			System.out.println(parametroG.toString());
		}
		System.out.println("Número de treinos: "
				+ parametrosG.get(0).getNumMedicoes());
	}

	/** DISTÂNCIAS */
	// Distancia entre dois pontos (Coordenadas)
	public double distancia(Coordenadas p1, Coordenadas p2) {
		double dx = (double) (p2.getPosX() - p1.getPosX());
		double dy = (double) (p2.getPosY() - p1.getPosY());

		double distancia = (double) Math
				.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		return distancia * escala.escala();
	}

	/** Algoritmo */
	// Algoritmo de localização Weighed Centroid
	public Coordenadas algoritmoLocalizacao(ArrayList<Sinal> sinaisArray,
			double g, int tipo) {
		ArrayList<DataSinais> dataSinaisArray = new ArrayList<DataSinais>();

		if (sinaisArray.size() > 3) {
			double sumRssi = 0;

			for (Sinal sinal : sinaisArray) {
				if (sinal.getPontoAcesso().getCoordenadas() != null) {
					double newRssi = (double) Math.pow(
							Math.pow(10, sinal.getNivel() / 20), g);
					sumRssi += newRssi;
					dataSinaisArray.add(new DataSinais(sinal.getPontoAcesso()
							.getCoordenadas().getPosX(), sinal.getPontoAcesso()
							.getCoordenadas().getPosY(), newRssi));
				}
			}

			int x = 0;
			int y = 0;

			for (DataSinais sinal : dataSinaisArray) {
				double weight = sinal.getRssi() / sumRssi;
				x += sinal.getX() * weight;
				y += sinal.getY() * weight;
			}
			if (tipo == 1)
				return new Coordenadas(x, y);
			else
				return escala.pixelToCoordenadas(x, y);

		} else {
			return null;
		}
	}

	public double melhorParametro(Coordenadas c, ArrayList<Sinal> sinaisArray,
			ArrayList<ParametroG> parametrosG) {
		double g_otimo = 0;
		Coordenadas posCalculada;
		double erro, menor_erro = Double.MAX_VALUE;
		for (ParametroG parametroG : parametrosG) {
			posCalculada = algoritmoLocalizacao(sinaisArray, parametroG.getG(),
					2);
			erro = distancia(c, posCalculada);
			if (erro < menor_erro) {
				menor_erro = erro;
				g_otimo = parametroG.getG();
			}
		}
		return g_otimo;
	}
}
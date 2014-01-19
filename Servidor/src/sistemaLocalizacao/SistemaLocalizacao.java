package sistemaLocalizacao;

import java.util.ArrayList;

import classes.Coordenadas;
import classes.DataSinais;
import classes.Escala;
import classes.Sinal;

public class SistemaLocalizacao {

	protected static float g = 1.3f;

	public double caracterizacaoAmbiente(int rssiD0, int rssiD, int d) {
		double n = (double) (rssiD0 - rssiD) / (10 * Math.log10(d / 100));
		return n;
	}

	/** Conversão do RSSi em Distância em cm -> d e d0 = 100 também em cm
	public double conversaoRSSiToDistancia(Sinal s) {
		double distancia = (double) Math
				.exp((s.getPontoAcesso().getNivelD0() - s.getNivel())
						/ (10 * s.getPontoAcesso().getN()));
		return distancia;
	}
	*/

	// Distancia entre dois pontos (Coordenadas)
	public double ditancia(Coordenadas p1, Coordenadas p2, Escala escala) {
		double dx = (double) (p2.getPosX() - p1.getPosX()) / escala.escalaX();
		double dy = (double) (p2.getPosY() - p1.getPosY()) / escala.escalaY();

		double distancia = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		return distancia;
	}
	
	/*
	public Coordenadas triangulacaoDistancias(ArrayList<Sinal> sinaisArray,
			Escala escala) {
		Coordenadas c = new Coordenadas();
		int xmax = Integer.MIN_VALUE;
		int xmin = Integer.MAX_VALUE;
		int ymax = Integer.MIN_VALUE;
		int ymin = Integer.MAX_VALUE;
		double d_xmax = 0, d_xmin = 0, d_ymax = 0, d_ymin = 0;
		for (Sinal sinal : sinaisArray) {
			Coordenadas coordenadas = sinal.getPontoAcesso().getCoordenadas();
			if (coordenadas != null) {
				if (coordenadas.getPosX() > xmax) {
					xmax = coordenadas.getPosX();
					d_xmax = conversaoRSSiToDistancia(sinal) * escala.escalaX();
				}
				if (coordenadas.getPosX() < xmin) {
					xmin = coordenadas.getPosX();
					d_xmin = conversaoRSSiToDistancia(sinal) * escala.escalaX();
				}
				if (coordenadas.getPosY() > ymax) {
					ymax = coordenadas.getPosY();
					d_ymax = conversaoRSSiToDistancia(sinal) * escala.escalaY();
				}
				if (coordenadas.getPosY() < ymin) {
					ymin = coordenadas.getPosY();
					d_ymin = conversaoRSSiToDistancia(sinal) * escala.escalaY();
				}
			}
		}

		int u = xmax - xmin;
		int v = ymax - ymin;
		System.out.println("Tamanho X - " + u + "; Tamanho Y - " + v);
		System.out.println("XMin - " + xmin + "; Xmax - " + xmax);
		System.out.println("D_XMin - " + d_xmin + "; DXmax - " + d_xmax);
		System.out.println("YMin - " + ymin + "; Ymax - " + ymax);
		System.out.println("D_YMin - " + d_ymin + "; DYmax - " + d_ymax);
		double x = (double) ((Math.pow(u, 2) + (Math.pow(d_xmin, 2) - Math.pow(
				d_xmax, 2))) / (2 * u));

		double y = (double) ((Math.pow(v, 2) + (Math.pow(d_ymin, 2) - Math.pow(
				d_ymax, 2))) / (2 * v));

		c.setPosX((int) Math.round(x));
		c.setPosY((int) Math.round(y));
		System.out.println("( " + c.getPosX() + "," + c.getPosY() + " )");
		return c;
	}
	
	
	public Coordenadas triangulacaoDistancias2(ArrayList<Sinal> sinaisArray,
			Escala escala) {
		Coordenadas c = new Coordenadas();
		double ponto[] = new double[sinaisArray.size()];
		Integer x[] = new Integer[sinaisArray.size()];
		Integer y[] = new Integer[sinaisArray.size()];
		int i = 1;
		for (Sinal sinal : sinaisArray) {
			PontoAcesso aux = sinal.getPontoAcesso();
			if (aux.getCoordenadas() != null) {
				x[i] = aux.getCoordenadas().getPosX();
				y[i] = aux.getCoordenadas().getPosY();
				double distancia = conversaoRSSiToDistancia(sinal)
						* escala.escalaY();
				ponto[i] = (double) Math.pow(x[i], 2) + Math.pow(y[i], 2)
						- Math.pow(distancia, 2);
				i++;
			}
		}
		int x32 = x[3] - x[2];
		int x13 = x[2] - x[3];
		int x21 = x[2] - x[1];
		int y32 = y[3] - y[2];
		int y13 = y[1] - y[3];
		int y21 = y[2] - y[1];

		double posX = (double) ((ponto[1] * y32) + (ponto[2] * y13) + (ponto[3] * y21))
				/ (2 * ((x[1] * y32) + (x[2] * y13) + (x[3] * y21)));

		double posY = (double) ((ponto[1] * x32) + (ponto[2] * x13) + (ponto[3] * x21))
				/ (2 * ((y[1] * x32) + (y[2] * x13) + (y[3] * x21)));

		c.setPosX((int) Math.round(posX));
		c.setPosY((int) Math.round(posY));
		System.out.println("( " + c.getPosX() + "," + c.getPosY() + " )");
		return c;
	}
	 */
	public Coordenadas triangulacaoDistancias3(ArrayList<Sinal> sinaisArray) {

		ArrayList<DataSinais> dataSinaisArray = new ArrayList<DataSinais>();

		if (sinaisArray.size() > 3) {
			float sumRssi = 0;

			for (Sinal sinal : sinaisArray) {
				if (sinal.getPontoAcesso().getCoordenadas() != null) {
					float newRssi = (float) Math.pow(
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
				float weight = sinal.getRssi() / sumRssi;
				x += sinal.getX() * weight;
				y += sinal.getY() * weight;
			}
			return new Coordenadas(x, y);

		} else {
			return null;
		}
	}
}
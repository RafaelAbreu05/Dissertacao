package baseDados;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import classes.Coordenadas;
import classes.Planta;
import classes.PontoAcesso;
import classes.Sinal;

public class BaseDados {
	private String userName = "root";
	private String password = "";
	private String url = "jdbc:mysql://localhost/localizacaoindoor_casa";
	private Connection conn = null;

	public Connection getConn() {
		return conn;
	}

	public void connectar() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, userName, password);
			System.out.println("Conexão com a BD estabelecida!");
		} catch (Exception e) {
			System.err.println("Não foi possível estabelecer conexão com a BD");
		}
	}

	public void terminar() {
		try {
			if (conn != null) {
				conn.close();
			}

			System.out.println("Conexão à BD finalizada");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// PONTOS ACESSO

	/** Adicionar Novo ponto de acesso à BD */
	public int novoPontoAcesso(PontoAcesso p) {
		int id_coodenadas = 0;
		int id_pontoAcesso = 0;
		try {
			if (p.getCoordenadas() != null && p.getCoordenadas().getPosX() >= 0
					&& p.getCoordenadas().getPosY() >= 0) {
				id_coodenadas = novasCoordenadas(p.getCoordenadas());
			}

			// Se o Ponto de Acesso não tiver ID
			if (p.getID() == 0) {
				// Então vamos ver se já existe um ID para esse ponto na BD
				id_pontoAcesso = getIDPontoAcesso(p);
				// Se não existir um Ponto de Acesso com essa informação
				if (id_pontoAcesso == 0) {
					// Então vamos criar um Ponto de Acesso Novo
					PreparedStatement statement = conn.prepareStatement(
							Querys.novoPontoAcesso,
							PreparedStatement.RETURN_GENERATED_KEYS);
					if (id_coodenadas != 0)
						statement.setInt(1, id_coodenadas);
					else
						statement.setNull(1, Types.INTEGER);
					statement.setString(2, p.getBSSID());
					statement.setString(3, p.getSSID());
					statement.executeUpdate();
					ResultSet idGerado = statement.getGeneratedKeys();
					if (idGerado.next() && idGerado != null) {
						id_pontoAcesso = idGerado.getInt(1);
					}
				}
			}
			// Senão temos de atualizar o Ponto de Acesso
			else {
				atualizarPontoAcesso(p);
				id_pontoAcesso = p.getID();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id_pontoAcesso;
	}

	/** GET lista de todos os pontos de acesso da BD */
	public TreeMap<Integer, PontoAcesso> getPontosAcesso() {
		TreeMap<Integer, PontoAcesso> pontosAcesso = new TreeMap<Integer, PontoAcesso>();

		try {
			int i = 1;
			PreparedStatement statement = conn
					.prepareStatement(Querys.getPontosAcesso);
			ResultSet resultado = statement.executeQuery();
			while (resultado.next()) {
				PontoAcesso p = new PontoAcesso();
				int id_coordenadas = resultado.getInt("id_coordenadas");
				if (!resultado.wasNull()) {
					p.setID(resultado.getInt("id_pontoAcesso"));
					p.setBSSID(resultado.getString("bssid"));
					p.setSSID(resultado.getString("ssid"));

					// Get Coordenadas
					Coordenadas c = new Coordenadas();
					c = getCoordenadas(id_coordenadas);
					if (c.getPosX() >= 0 && c.getPosY() >= 0) {
						p.setCoordenadas(c);
					}
					pontosAcesso.put(i, p);
					i++;
				}
			}

		} catch (SQLException e) {
		}
		return pontosAcesso;
	}

	/** Get ID PontoAcesso */
	public int getIDPontoAcesso(PontoAcesso p) {
		int id_pontoAcesso = 0;
		try {
			PreparedStatement statement = conn
					.prepareStatement(Querys.getIDPontoAcesso);
			statement.setString(1, p.getBSSID());

			ResultSet idGerado = statement.executeQuery();
			if (idGerado.next()) {
				id_pontoAcesso = idGerado.getInt(1);
			}
		} catch (SQLException e) {
		}
		return id_pontoAcesso;
	}

	/** Get PontoAcesso */
	public PontoAcesso getPontoAcesso(int id_pontoAcesso) {
		PontoAcesso p = null;
		try {
			PreparedStatement statement = conn
					.prepareStatement(Querys.getPontoAcesso);
			statement.setInt(1, id_pontoAcesso);
			ResultSet resultado = statement.executeQuery();
			if (resultado.next() && resultado != null) {
				p = new PontoAcesso();
				p.setID(resultado.getInt("id_pontoAcesso"));
				p.setBSSID(resultado.getString("bssid"));
				p.setSSID(resultado.getString("ssid"));
				int id_coordenadas = resultado.getInt("id_coordenadas");
				if (!resultado.wasNull()) {
					p.setCoordenadas(getCoordenadas(id_coordenadas));
				}
			}
		} catch (SQLException e) {
		}

		return p;
	}

	/** Remover um ponto de acesso da BD */
	public void removerPontoAcesso(int id_pontoAcesso) {
		try {
			PontoAcesso p = getPontoAcesso(id_pontoAcesso);
			if (p.getID() != 0 && p.getCoordenadas() != null) {
				// Remover Coordenadas
				removerCoordenadas(p.getCoordenadas().getID());
			}

			PreparedStatement statement = conn
					.prepareStatement(Querys.removerPontoAcesso);
			statement.setInt(1, id_pontoAcesso);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** Atualizar um ponto de acesso na BD */
	public void atualizarPontoAcesso(PontoAcesso pontoAcesso) {
		try {
			// Se existir coordenadas no ponto de acesso
			if (pontoAcesso.getCoordenadas() != null) {
				Coordenadas c = new Coordenadas();
				c = getCoordenadas(pontoAcesso.getCoordenadas().getID());
				// E já existir na BD
				if (c != null) {
					// Então tempos que atualizar as coordenadas
					atualizarCoordenadas(pontoAcesso.getCoordenadas());
				}
			}

			PreparedStatement statement = conn
					.prepareStatement(Querys.atualizarPontoAcesso);
			statement.setString(1, pontoAcesso.getBSSID());
			statement.setString(2, pontoAcesso.getSSID());
			if (pontoAcesso.getCoordenadas() != null
					&& pontoAcesso.getCoordenadas().getID() != 0)
				statement.setInt(3, pontoAcesso.getCoordenadas().getID());
			else
				statement.setNull(3, Types.INTEGER);
			statement.setInt(4, pontoAcesso.getID());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Coordenadas

	/** Novas Coordenadas */
	public int novasCoordenadas(Coordenadas coordenadas) {
		int id = 0;

		try {
			PreparedStatement statement = conn
					.prepareStatement(Querys.getIDCoordenadas);
			statement.setDouble(1, coordenadas.getPosX());
			statement.setDouble(2, coordenadas.getPosY());
			ResultSet resultado = statement.executeQuery();
			if (resultado.next()) {
				id = resultado.getInt("id_coordenadas");
				if (!resultado.wasNull()) {
					coordenadas.setID(id);
					atualizarCoordenadas(coordenadas);
				}
			} else {
				statement = conn.prepareStatement(Querys.novaCoodenadas,
						PreparedStatement.RETURN_GENERATED_KEYS);
				statement.setInt(1, coordenadas.getPosX());
				statement.setInt(2, coordenadas.getPosY());
				statement.executeUpdate();
				ResultSet idGerado = statement.getGeneratedKeys();
				if (idGerado.next() && idGerado != null) {
					id = idGerado.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	/** Get ArrayList de Coordenadas */
	public ArrayList<Coordenadas> getALLCoordenadas() {
		ArrayList<Coordenadas> coordenadas = new ArrayList<Coordenadas>();

		try {
			PreparedStatement statement = conn
					.prepareStatement(Querys.getALLCoordenadas);
			ResultSet resultado = statement.executeQuery();
			while (resultado.next()) {
				Coordenadas c = new Coordenadas();
				c.setID(resultado.getInt("id_coordenadas"));
				c.setPosX(resultado.getInt("coordX"));
				c.setPosY(resultado.getInt("coordY"));
				coordenadas.add(c);
			}
		} catch (SQLException e) {
		}
		return coordenadas;
	}

	/** Get Coordenadas */
	public Coordenadas getCoordenadas(int id_coordenadas) {

		Coordenadas c = null;
		try {
			PreparedStatement statement = conn
					.prepareStatement(Querys.getCoordenadas);
			statement.setInt(1, id_coordenadas);
			ResultSet coordenadasR = statement.executeQuery();
			if (coordenadasR.next() && coordenadasR != null) {
				c = new Coordenadas();
				c.setID(coordenadasR.getInt("id_coordenadas"));
				c.setPosX(coordenadasR.getInt("coordX"));
				c.setPosY(coordenadasR.getInt("coordY"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return c;
	}

	/** Atualizar Coordenadas */
	private void atualizarCoordenadas(Coordenadas coordenadas) {
		try {
			PreparedStatement statement = conn
					.prepareStatement(Querys.atualizarCoordenadas);
			statement.setInt(1, coordenadas.getPosX());
			statement.setInt(2, coordenadas.getPosY());
			statement.setInt(3, coordenadas.getID());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/** Remover Coordenadas */
	private void removerCoordenadas(int id_coordenadas) {
		try {
			PreparedStatement statement = conn
					.prepareStatement(Querys.removerCoordenada);
			statement.setInt(1, id_coordenadas);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Planta

	/** Nova Planta */
	public int novaPlanta(Planta planta) {
		InputStream fis = null;
		int id = 0;
		try {
			fis = new FileInputStream(planta.getImagemPlanta());
			PreparedStatement statement = conn.prepareStatement(
					Querys.novaPlanta, PreparedStatement.RETURN_GENERATED_KEYS);
			statement.setString(1, planta.getNome());
			statement.setInt(2, planta.getAltura());
			statement.setInt(3, planta.getLargura());
			statement.setDouble(4, planta.getAlturaReal());
			statement.setDouble(5, planta.getLarguraReal());
			statement
					.setBinaryStream(6, fis, planta.getImagemPlanta().length());
			statement.executeUpdate();
			ResultSet idGerado = statement.getGeneratedKeys();
			if (idGerado.next() && idGerado != null) {
				id = idGerado.getInt(1);
			}
		} catch (SQLException | FileNotFoundException e) {
			e.printStackTrace();
		}
		return id;
	}

	/** Atualizar Planta */
	public void atualizarPlanta(Planta planta) {
		InputStream fis = null;
		try {
			fis = new FileInputStream(planta.getImagemPlanta());
			PreparedStatement statement = conn
					.prepareStatement(Querys.atualizarPlanta);
			statement.setString(1, planta.getNome());
			statement.setInt(2, planta.getAltura());
			statement.setInt(3, planta.getLargura());
			statement.setDouble(4, planta.getAlturaReal());
			statement.setDouble(5, planta.getLarguraReal());
			statement
					.setBinaryStream(6, fis, planta.getImagemPlanta().length());
			statement.executeUpdate();
		} catch (SQLException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/** Get todos os dados da planta */
	public Planta getPlanta() {
		BufferedImage buffimg = null;
		Planta planta = new Planta();
		try {
			PreparedStatement statement = conn
					.prepareStatement(Querys.getPlanta);
			ResultSet resultado = statement.executeQuery();
			resultado.next();

			planta.setID(resultado.getInt("id_planta"));
			planta.setNome(resultado.getString("nome"));
			planta.setAltura(resultado.getInt("altura"));
			planta.setLargura(resultado.getInt("largura"));
			planta.setLarguraReal(resultado.getDouble("larguraReal"));
			planta.setAlturaReal(resultado.getDouble("alturaReal"));
			InputStream img = resultado.getBinaryStream("imagemPlanta");
			buffimg = ImageIO.read(img);
			File outputfile = new File(
					"C:/RaFaeL/Dropbox/Git/Dissertacao/Servidor/src/imagens/planta/planta.png");
			outputfile.getParentFile().mkdirs();
			ImageIO.write(buffimg, "jpg", outputfile);
			planta.setImagemPlanta(outputfile);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return planta;
	}

	// Sinal

	/** Atualizr TreeMap de sinais */
	public void atualizarSinais(TreeMap<Coordenadas, ArrayList<Sinal>> sinais) {
		Iterator<Entry<Coordenadas, ArrayList<Sinal>>> it = sinais.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<Coordenadas, ArrayList<Sinal>> sinaisAux = (Entry<Coordenadas, ArrayList<Sinal>>) it
					.next();

			Coordenadas coord = sinaisAux.getKey();
			ArrayList<Sinal> sinaisArray = sinaisAux.getValue();

			if (coord != null) {
				for (Sinal sinal : sinaisArray) {
					PontoAcesso p = sinal.getPontoAcesso();
					int id = novoPontoAcesso(p);
					p.setID(id);
					sinal.setPontoAcesso(p);

					if (getSinal(sinal, coord)) {
						atualizarSinal(sinal, coord);
					} else {
						novoSinal(sinal, coord);
					}
				}
			}
		}

	}

	/** Novo Sinal */
	private void novoSinal(Sinal sinal, Coordenadas coord) {
		try {
			PreparedStatement statement = conn
					.prepareStatement(Querys.novoSinal);
			statement.setInt(1, coord.getID());
			statement.setInt(2, sinal.getPontoAcesso().getID());
			statement.setInt(3, sinal.getFrequencia());
			statement.setInt(4, sinal.getNivel());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** Atualizar Sinal */
	private void atualizarSinal(Sinal sinal, Coordenadas coord) {
		try {
			PreparedStatement statement = conn
					.prepareStatement(Querys.atualizarSinal);
			statement.setInt(1, sinal.getFrequencia());
			statement.setInt(2, sinal.getNivel());
			statement.setInt(3, coord.getID());
			statement.setDouble(4, sinal.getPontoAcesso().getID());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/** Get Sinal */
	private boolean getSinal(Sinal sinal, Coordenadas coordenadas) {
		try {
			PreparedStatement statement = conn
					.prepareStatement(Querys.getSinal);
			statement.setInt(1, coordenadas.getID());
			statement.setInt(2, sinal.getPontoAcesso().getID());
			ResultSet sinalR = statement.executeQuery();
			if (sinalR.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	/** Remover Sinal */
	public TreeMap<Coordenadas, ArrayList<Sinal>> removerSinal(int i,
			TreeMap<Coordenadas, ArrayList<Sinal>> sinais) {

		int count = 1;
		Iterator<Entry<Coordenadas, ArrayList<Sinal>>> it = sinais.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<Coordenadas, ArrayList<Sinal>> sinaisAux = (Entry<Coordenadas, ArrayList<Sinal>>) it
					.next();

			Coordenadas coord = sinaisAux.getKey();

			ArrayList<Sinal> sinaisArray = sinaisAux.getValue();
			for (Sinal sinal : sinaisArray) {
				if (count == i) {
					PontoAcesso p = sinal.getPontoAcesso();
					try {
						PreparedStatement statement = conn
								.prepareStatement(Querys.removerSinal);
						statement.setInt(1, coord.getID());
						statement.setInt(2, p.getID());
						statement.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					sinais = getAllSinais();
					return sinais;
				}
				count++;
			}
			removerCoordenadas(coord.getID());
		}
		return sinais;
	}

	/** Get ALL Sinais */
	public TreeMap<Coordenadas, ArrayList<Sinal>> getAllSinais() {
		TreeMap<Coordenadas, ArrayList<Sinal>> sinais = new TreeMap<Coordenadas, ArrayList<Sinal>>();
		ArrayList<Sinal> aux = new ArrayList<>();
		try {
			PreparedStatement statement = conn
					.prepareStatement(Querys.getAllSinais);
			ResultSet resultado = statement.executeQuery();
			while (resultado.next()) {

				Sinal s = new Sinal();
				s.setFrequencia(resultado.getInt("frequencia"));
				s.setNivel(resultado.getInt("nivel"));

				Coordenadas c = new Coordenadas();
				c = getCoordenadas(resultado.getInt("id_coordenadas"));
				PontoAcesso p = new PontoAcesso();
				p = getPontoAcesso(resultado.getInt("id_pontoAcesso"));
				s.setPontoAcesso(p);

				aux = sinais.get(c);
				if (aux == null) {
					aux = new ArrayList<>();
				}
				if (!aux.contains(s)) {
					aux.add(s);
					sinais.put(c, aux);
				}
			}
		} catch (SQLException e) {
		}
		return sinais;
	}
}
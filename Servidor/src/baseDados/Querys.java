package baseDados;

public class Querys {
	/** Pontos Acesso */

	// Select
	public static final String getPontosAcesso = "SELECT * from ponto_acesso";
	public static final String getPontoAcesso = "SELECT * from ponto_acesso WHERE id_pontoAcesso = ?";
	public static final String getIDPontoAcesso = "SELECT id_pontoAcesso FROM ponto_acesso WHERE bssid = ?";
	// Insert
	public static final String novoPontoAcesso = "INSERT INTO ponto_acesso (id_coordenadas, bssid, ssid) VALUES (?,?,?);";
	// Update
	public static final String atualizarPontoAcesso = "UPDATE ponto_acesso SET bssid = ?, ssid = ?, id_coordenadas = ? WHERE id_pontoAcesso = ?";
	// Delete
	public static final String removerPontoAcesso = "DELETE FROM ponto_acesso WHERE id_pontoAcesso = ?";

	/** Coordenadas */

	// Select
	public static final String getCoordenadas = "SELECT * from coordenadas WHERE id_coordenadas = ?";
	public static final String getALLCoordenadas = "SELECT * from coordenadas";
	public static final String getIDCoordenadas = "SELECT id_coordenadas FROM coordenadas WHERE coordX = ? AND coordY = ?";
	// Insert
	public static final String novaCoodenadas = "INSERT INTO coordenadas (coordX, coordY) VALUES (?,?)";
	// Update
	public static final String atualizarCoordenadas = "UPDATE coordenadas SET coordX = ?, coordY = ? WHERE id_coordenadas = ?";
	// Delete
	public static final String removerCoordenada = "DELETE FROM coordenadas WHERE id_coordenadas = ?";

	/** Planta */

	// Select
	public static final String getPlanta = "SELECT * FROM planta WHERE id_planta = 1";
	// Insert
	public static final String novaPlanta = "INSERT INTO planta(nome, altura, largura, alturaReal, larguraReal, imagemPlanta) values (?, ?, ?, ?, ?, ?)";
	// Update
	public static final String atualizarPlanta = "UPDATE planta SET nome = ?, altura = ?, largura = ?, alturaReal = ?, larguraReal = ?, imagemPlanta = ? WHERE id_planta = 1";

	/** Sinal */

	// Select
	public static final String getSinal = "SELECT * FROM sinal WHERE id_coordenadas = ? AND id_pontoAcesso = ?";
	public static final String getAllSinais = "SELECT * FROM sinal";
	// Insert
	public static final String novoSinal = "INSERT INTO sinal (id_coordenadas, id_pontoAcesso, frequencia, nivel) VALUES (?, ?, ?, ?)";
	// Update
	public static final String atualizarSinal = "UPDATE sinal SET frequencia = ?, nivel = ? WHERE id_coordenadas = ? AND id_pontoAcesso = ?";
	// Delete
	public static final String removerSinal = "DELETE FROM sinal WHERE id_coordenadas = ? AND id_pontoAcesso = ?";

}

package clienteTCP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;

import android.graphics.Bitmap;
import android.util.Log;
import classes.PontoAcesso;
import classes.Sinal;

public class ClienteTCP {

	public Bitmap bitmap;

	// message to send to the server
	private String mServerMessage;
	// sends message received notifications
	private OnMessageReceived mMessageListener = null;
	// while this is true, the server will continue running
	private boolean mRun = false;
	// used to send messages
	private PrintWriter mBufferOut;
	// used to read messages from the server
	private BufferedReader mBufferIn;
	// Client Socket
	private Socket socket;

	// Ficheiro transferido
	private boolean successFile;
	private String nomeFicheiro;

	// Pontos de Acesso
	TreeMap<Integer, PontoAcesso> pontosAcesso;

	// Sinais
	ArrayList<Sinal> sinais = new ArrayList<Sinal>();

	public void setSinais(ArrayList<Sinal> s) {
		this.sinais = s;
	}

	public ClienteTCP() {
		mMessageListener = null;
		socket = null;
		successFile = false;
		nomeFicheiro = null;
		pontosAcesso = null;
	}

	public boolean getSuccessFile() {
		return successFile;
	}

	public String getNomeFicheiro() {
		return nomeFicheiro;
	}

	public TreeMap<Integer, PontoAcesso> getPontosAcesso() {
		return (TreeMap<Integer, PontoAcesso>) pontosAcesso;
	}

	/**
	 * Constructor of the class. OnMessagedReceived listens for the messages
	 * received from server
	 */
	public ClienteTCP(OnMessageReceived listener) {
		mMessageListener = listener;
	}

	/**
	 * Sends the message entered by client to the server
	 * 
	 * @param message
	 *            text entered by client
	 */
	public void sendMessage(String message) {
		if (mBufferOut != null && !mBufferOut.checkError()) {
			Log.e("TCP Cliente", "Enviar: " + message.toString());
			mBufferOut.println(message);
			mBufferOut.flush();
		}
	}

	/**
	 * Close the connection and release the members
	 * 
	 * @throws IOException
	 */
	public void stopClient() throws IOException {

		// send mesage that we are closing the connection
		sendMessage(Constantes.CLOSED_CONNECTION + "Rafael");

		mRun = false;

		if (mBufferOut != null) {
			mBufferOut.flush();
			mBufferOut.close();
		}

		mMessageListener = null;
		mBufferIn = null;
		mBufferOut = null;
		mServerMessage = null;
		if (socket != null) {
			socket.close();
		}
	}

	public void run() {
		mRun = true;
		
		try {
			// here you must put your computer's IP address.
			InetAddress serverAddr = InetAddress
					.getByName(Constantes.SERVIDOR_IP);

			Log.e("TCP Cliente", "Conectado com o servidor");

			// create a socket to make the connection with the server
			socket = new Socket(serverAddr, Constantes.SERVIDOR_PORTA);

			try {

				// sends the message to the server
				mBufferOut = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream(),
								"UTF-8")), true);

				// receives the message which the server sends back
				mBufferIn = new BufferedReader(new InputStreamReader(
						socket.getInputStream(), "UTF-8"));

				// send login name
				sendMessage(Constantes.LOGIN_NAME + "Rafael");

				// in this while the client listens for the messages sent by the
				// server
				while (mRun) {
					mServerMessage = mBufferIn.readLine();
					if (mServerMessage.contains("SENDING_FILE")) {
						String[] tokens = mServerMessage.split("/");
						String fileName = tokens[1];
						int fileSize = Integer.parseInt(tokens[2]);
						TranferirPlanta downloadFile = new TranferirPlanta();
						successFile = downloadFile.download(fileSize, fileName);
						if (successFile) {
							Log.e("TCP Cliente", "Planta recebida com sucesso.");
							successFile = true;
							nomeFicheiro = downloadFile.getNomeFicheiro();
							sendMessage("Planta recebida com sucesso.");
							mMessageListener.messageReceived(mServerMessage);

						} else {
							Log.e("TCP Cliente", "Imagem não enviada.");
							sendMessage("Planta não recebida com sucesso.");
						}
					} else if (mServerMessage.contains("PontosAcesso")) {
						TransferirPontosAcesso dpa = new TransferirPontosAcesso();
						pontosAcesso = dpa.transferirPontosAcesso();
						sendMessage("Ponto de Acesso recebido com sucesso.");
						mMessageListener.messageReceived(mServerMessage);
						Log.e("TCP Cliente",
								"Ponto de Acesso recebido com sucesso.");
					} else if (mServerMessage.contains("Sinais")) {
						mMessageListener.messageReceived(mServerMessage);
						EnviarSinais enviarS = new EnviarSinais();
						System.out.println(sinais.toString());
						enviarS.enviarSinais(sinais);
						sendMessage("Sinal enviado com sucesso.");
						Log.e("TCP Cliente", "Sinal enviado com sucesso.");

					} else if (mServerMessage != null) {
						mMessageListener.messageReceived(mServerMessage);
					}

				}
				Log.e("Resposta do Servidor", "Mensagem Recebida: '"
						+ mServerMessage.toString() + "'");

			} catch (Exception e) {

				Log.e("TCP", "Servidor: Erro", e);
				stopClient();

			} finally {
				// the socket must be closed. It is not possible to reconnect to
				// this socket
				// after it is closed, which means a new socket instance has to
				// be created.
				stopClient();
			}

		} catch (Exception e) {

			Log.e("TCP", "Cliente: Erro", e);

		}
	}

	// Declare the interface. The method messageReceived(String message) will
	// must be implemented in the MyActivity
	// class at on asynckTask doInBackground
	public interface OnMessageReceived {
		public void messageReceived(String message);
	}
}
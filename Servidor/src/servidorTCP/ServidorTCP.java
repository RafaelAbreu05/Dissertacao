package servidorTCP;

import interfaceServidor.MenuPrincipal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;

import classes.PontoAcesso;
import classes.Sinal;
import clienteTCP.Constantes;

/**
 * The class extends the Thread class so we can receive and send messages at the
 * same time
 */
public class ServidorTCP extends Thread {

	// while this is true the server will run
	private boolean running = false;
	// used to send messages
	private PrintWriter bufferSender;
	// callback used to notify new messages received
	private OnMessageReceived messageListener;
	private ServerSocket serverSocket;
	private Socket client;

	/**
	 * Constructor of the class
	 * 
	 * @param messageListener
	 *            listens for the messages
	 */
	public ServidorTCP(OnMessageReceived messageListener) {
		this.messageListener = messageListener;
	}

	/**
	 * Close the server
	 */
	public void close() {
		running = false;

		if (bufferSender != null) {
			bufferSender.flush();
			bufferSender.close();
			bufferSender = null;
		}

		try {
			if (client != null) {
				client.close();
			}
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Servidor: Desligado");
		serverSocket = null;
		client = null;

	}

	/**
	 * Method to send the messages from server to client
	 * 
	 * @param message
	 *            the message sent by the server
	 */
	public void sendMessage(String message) {
		if (bufferSender != null && !bufferSender.checkError()) {
			bufferSender.println(message);
			bufferSender.flush();
			System.out.println("Servidor: Mensagem enviada - \""
					+ message.toString() + "\"");
		}
	}

	// Enviar ficheiro
	public void sendFile(File file) throws IOException {
		if (client != null && serverSocket != null) {
			// sending a message before streaming the file
			sendMessage("SENDING_FILE/" + file.getName() + "/" + file.length()
					+ "/");
			EnviarPlanta enviarPlanta = new EnviarPlanta();
			enviarPlanta.enviarPlanta(file);
		}
	}

	// Enviar Pedido de Sinal
	public ArrayList<Sinal> pedidoSinal() {
		ArrayList<Sinal> sinais = null;
		if (client != null && serverSocket != null) {
			// Enviar mensagem
			sendMessage("Sinais");
			TranferirSinais ts = new TranferirSinais();
			sinais = ts.tranferirSinais();
		}
		return sinais;
	}

	// Enviar Pontos Acesso
	public void sendPontosAcesso(TreeMap<Integer, PontoAcesso> pontosAcesso)
			throws IOException {

		if (client != null && serverSocket != null) {
			sendMessage("PontosAcesso");
			EnviarPontosAcesso spa = new EnviarPontosAcesso();
			spa.enviarPontosAcesso(pontosAcesso);
		}
	}

	public boolean hasCommand(String message) {
		if (message != null) {
			if (message.contains(Constantes.CLOSED_CONNECTION)) {
				messageListener.messageReceived(message.replaceAll(
						Constantes.CLOSED_CONNECTION, "")
						+ " desconectou do servidor.");
				// close the server connection if we have this command and
				// rebuild a new one
				close();
				runServer();
				return true;
			} else if (message.contains(Constantes.LOGIN_NAME)) {
				messageListener.messageReceived(message.replaceAll(
						Constantes.LOGIN_NAME, "") + " conectou ao servidor.");
				try {
					sendFile(MenuPrincipal.plant.getImagemPlanta());
					sendPontosAcesso(MenuPrincipal.pontosAcesso);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}
		}

		return false;
	}

	/**
	 * Builds a new server connection
	 */
	private void runServer() {
		running = true;

		try {

			System.out.println("Servidor: Estabelecer Conecção...");

			serverSocket = new ServerSocket(Constantes.SERVIDOR_PORTA);
			client = serverSocket.accept();

			System.out.println("Servidor: Conectado...");

			try {

				// sends the message to the client
				bufferSender = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(client.getOutputStream(),
								"UTF-8")), true);

				// read the message received from client
				BufferedReader in = new BufferedReader(new InputStreamReader(
						client.getInputStream(), "UTF-8"));

				// in this while we wait to receive messages from client (it's
				// an infinite loop)
				// this while it's like a listener for messages
				while (running) {

					String message = null;
					try {
						message = in.readLine();
						if (message != null) {
							System.out
									.println("Servidor: Recebeu a seguinte mensagem - \""
											+ message.toString()+"\"");
						}
					} catch (IOException e) {
						System.out.println("Erro ao ler a seguinte mensagem: "
								+ e.getMessage());
					}

					if (hasCommand(message)) {
						continue;
					}

					if (message != null && messageListener != null) {
						// call the method messageReceived from ServerBoard
						// class
						messageListener.messageReceived(message);
					}
				}

			} catch (Exception e) {
				System.out.println("Servidor: Erro");
				e.printStackTrace();
			}

		} catch (Exception e) {
			System.out.println("Servidor: Erro");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		super.run();
		runServer();
	}

	// Declare the interface. The method messageReceived(String message) will
	// must be implemented in the ServerBoard
	// class at on startServer button click
	public interface OnMessageReceived {
		public void messageReceived(String message);
	}

}
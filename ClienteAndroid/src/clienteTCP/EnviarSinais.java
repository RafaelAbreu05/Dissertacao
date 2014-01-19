package clienteTCP;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import classes.Sinal;

public class EnviarSinais {

	private Socket socket;

	public void enviarSinais(ArrayList<Sinal> sinais) {
		try {
			System.out.println(sinais.toString());
			socket = new Socket(Constantes.SERVIDOR_IP,
					Constantes.SERVIDOR_PORTA_SINAIS);
			OutputStream os = socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(sinais);
			oos.close();
			os.close();
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

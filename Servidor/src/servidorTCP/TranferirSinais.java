package servidorTCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import classes.Sinal;
import clienteTCP.Constantes;

public class TranferirSinais {

	private ServerSocket ss;

	@SuppressWarnings("unchecked")
	public ArrayList<Sinal> tranferirSinais() {

		ArrayList<Sinal> sinais = new ArrayList<Sinal>();
		try {
			ss = new ServerSocket(Constantes.SERVIDOR_PORTA_SINAIS);

			Socket s = ss.accept();
			InputStream is = s.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			sinais = (ArrayList<Sinal>) ois.readObject();
			s.close();
			ss.close();
			ois.close();
			is.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return sinais;
	}
}

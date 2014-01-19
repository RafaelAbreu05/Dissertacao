package clienteTCP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import classes.PontoAcesso;

@SuppressLint("UseValueOf")
public class TransferirPontosAcesso {
	private static Socket socket;
	private TreeMap<Integer, PontoAcesso> pontosAcesso = new TreeMap<Integer, PontoAcesso>();

	@SuppressWarnings("unchecked")
	public TreeMap<Integer, PontoAcesso> transferirPontosAcesso()
			throws IOException, ClassNotFoundException {

		socket = new Socket(Constantes.SERVIDOR_IP,
				Constantes.SERVIDOR_PORTA_PONTOS_ACESSO);

		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		int numPontosAcessso = in.readInt();
				
		for (int i = 1; i <= numPontosAcessso; i++) {
			Object o = in.readObject();
			assert o instanceof PontoAcesso;
			PontoAcesso p = (PontoAcesso) o;
			pontosAcesso.put(new Integer(i), p);
		}
		in.close();
		socket.close();
		return (TreeMap<Integer, PontoAcesso>) pontosAcesso.clone();
	}
}

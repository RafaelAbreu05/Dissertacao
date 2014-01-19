package servidorTCP;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;

import classes.PontoAcesso;
import clienteTCP.Constantes;

public class EnviarPontosAcesso {
	
	private ServerSocket ss;

	public void enviarPontosAcesso(TreeMap<Integer, PontoAcesso> pontosAcesso) throws IOException {
		ss = new ServerSocket(Constantes.SERVIDOR_PORTA_PONTOS_ACESSO);
		Socket socket = ss.accept();
		ObjectOutputStream out = new ObjectOutputStream(
				socket.getOutputStream());
		
		out.writeInt(pontosAcesso.size());
		for(PontoAcesso p: pontosAcesso.values()){
			out.writeObject(p);
		}
		out.close();
		socket.close();
		ss.close();
	}
}

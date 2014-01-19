package servidorTCP;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import clienteTCP.Constantes;

public class EnviarPlanta {
	
	private static BufferedInputStream bis;
	private static ServerSocket servsock;
	
	public void enviarPlanta(File myFile) throws IOException{
		servsock = new ServerSocket(Constantes.SERVIDOR_PORTA_PLANTA);
		Socket sock = servsock.accept();
		
		System.out.println("Aceitar Conecção : " + sock);
		
		byte[] mybytearray = new byte[(int) myFile.length()];
		FileInputStream fis = new FileInputStream(myFile);
		bis = new BufferedInputStream(fis);
		bis.read(mybytearray, 0, mybytearray.length);
		OutputStream os = sock.getOutputStream();
		System.out.println("Enviar...");
		os.write(mybytearray, 0, mybytearray.length);
		os.flush();
		sock.close();
		servsock.close();
		System.out.println("Planta Enviada.");
	}
}

package clienteTCP;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TranferirPlanta {

	private static Socket sock;
	private String nomeFicheiro;

	public boolean download(int fileSize, String fileName) throws UnknownHostException, IOException {
		int filesize = 6022386;
		
		int bytesRead;
		int current = 0;
		sock = new Socket(Constantes.SERVIDOR_IP, Constantes.SERVIDOR_PORTA_PLANTA);
		System.out.println("Conectar...");

		// receive file
		byte[] mybytearray = new byte[filesize];
		InputStream is = sock.getInputStream();
		
		File f = new File("/storage/sdcard1/Imagens/" + fileName);
		if(f.exists()){
			f.delete();
		}
		
		FileOutputStream fos = new FileOutputStream("/storage/sdcard1/Imagens/" + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		bytesRead = is.read(mybytearray, 0, mybytearray.length);
		current = bytesRead;

		do {
			bytesRead = is.read(mybytearray, current,
					(mybytearray.length - current));
			if (bytesRead >= 0)
				current += bytesRead;
		} while (bytesRead > -1);

		bos.write(mybytearray, 0, current);
		bos.flush();
		bos.close();
		sock.close();
		
		if(f.length() == fileSize){
			setNomeFicheiro(fileName);
			return true;
		}
		return false;
	}

	public String getNomeFicheiro() {
		return nomeFicheiro;
	}

	public void setNomeFicheiro(String nomeFicheiro) {
		this.nomeFicheiro = nomeFicheiro;
	}
}

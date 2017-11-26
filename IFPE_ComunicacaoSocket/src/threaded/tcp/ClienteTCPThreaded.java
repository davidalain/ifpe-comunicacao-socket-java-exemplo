package threaded.tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import threaded.Config;

public class ClienteTCPThreaded extends Thread{

	private int id; 
	private String host;
	private int port;

	public ClienteTCPThreaded(int id, String host, int porta) {
		this.id = id;
		this.host = host;
		this.port = porta;
	}

	@Override
	public void run() {

		Socket target = null;
		
		try {

			target = new Socket(host, port);
			
			System.out.println("C# Cliente conectado a " + host+":"+port);
			
			ObjectInputStream entrada = new ObjectInputStream(target.getInputStream());
			OutputStream saida = new ObjectOutputStream(target.getOutputStream());

			byte[] buf = new byte[Config.BUFFER_SIZE];

			final byte[] req = new byte[]{Config.KEY};

			while(true){

				saida.write(req);

				if(entrada.available() >= buf.length){
					entrada.read(buf);	
//					System.out.println("C# id="+id + ", array=" +Arrays.toString(buf));
				}else{
					Thread.sleep(10);
				}

			}


		} catch(Exception e) {
//			e.printStackTrace();
			try {
				target.close();
			} catch (IOException e1) {
//				e1.printStackTrace();
			}
		}finally {
			System.err.println("C# Conexão fechada do cliente TCP " + id);
		}

	}


}

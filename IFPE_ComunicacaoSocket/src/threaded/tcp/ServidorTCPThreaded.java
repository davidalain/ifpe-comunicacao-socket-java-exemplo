package threaded.tcp;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import threaded.Config;

public class ServidorTCPThreaded extends Thread{

	private int idConexao;
	private Socket client;
	private Integer contadorPacotesRespondidos;
	private long millisStart;
	
	
	public ServidorTCPThreaded(int idConexao, Socket cliente, Integer contadorPacotesRespondidos, long millisStart) {
		this.idConexao = idConexao;
		this.client = cliente;
		this.contadorPacotesRespondidos = contadorPacotesRespondidos;
		this.millisStart = millisStart;
	}
	
	
	@Override
	public void run() {
		
		try{
			
			System.out.println("S#: Cliente " + idConexao + " conectado. IP:porta={" + client.getInetAddress().getHostAddress() + ":" + client.getPort()+ "}");
			
			byte[] buf = new byte[Config.BUFFER_SIZE];
			Random r = new Random();

			ObjectOutputStream saida = new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream entrada = new ObjectInputStream(client.getInputStream());
			
			while(true){
				
				int pacoteNum = 0;
				synchronized (contadorPacotesRespondidos) {
					pacoteNum = contadorPacotesRespondidos.intValue();
					
					contadorPacotesRespondidos++;
				}
				
				if(entrada.available() > 0 && entrada.readByte() == Config.KEY){
					r.nextBytes(buf);
					saida.write(buf);
					
					System.out.println("seg: "+((double)(System.currentTimeMillis()-millisStart))/1000.0+", pacoteNum: "+pacoteNum);
				}else{
					Thread.sleep(10);
				}
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			System.err.println("S# Conexão fechada do cliente " + idConexao);
		}

	}
	
}

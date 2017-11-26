package threaded.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import threaded.Config;

public class MainServidorTCPMultiThreaded {

	public static void main(String[] args) throws IOException {

		ServerSocket servidor = null;
		int contadorConexao = 0;
		Integer contadorPacotesRespondidos = new Integer(0);
		long millisStart = System.currentTimeMillis();

		try {

			//Instancia o ServerSocket ouvindo a porta
			servidor = new ServerSocket(Config.PORT);
			System.out.println("S#: Servidor TCP ouvindo a porta " + Config.PORT);

			while(true) {

				// o método accept() bloqueia a execução até que o
				//servidor receba um pedido de conexão

				Socket cliente = servidor.accept();
				
				new ServidorTCPThreaded(contadorConexao++, cliente, contadorPacotesRespondidos, millisStart).start();
			}

		} catch(Exception e) {
			System.out.println("S#: Erro: " + e.getMessage());
			servidor.close();
		} finally {

		} 
	}

}

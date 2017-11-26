package simple;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;

public class ServidorTCPBasico {

	public static void main(String[] args) {
		
		try {
			//Instancia o ServerSocket ouvindo a porta
			int porta = 12345;
			ServerSocket servidor = new ServerSocket(porta);
			System.out.println("S#: Servidor ouvindo a porta " + porta);

			int contador = 0;

			while(true) {
				// o método accept() bloqueia a execução do código até que o
				//servidor receba um pedido de conexão de um cliente

				Socket cliente = servidor.accept();
				System.out.println("S#: Cliente conectado: " + cliente.getInetAddress().getHostAddress()+":"+cliente.getPort());

				ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
				ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
				saida.flush();
				
				switch(contador++ % 4){

				case 0:
					//Servidor enviando um Date
					saida.writeObject(new Date());					
					break;
					
				case 1:
					//Servidor enviando String
					saida.writeObject("Tretas infinitas");
//					System.out.println(entrada.readObject().toString());
					break;
					
				case 2:
					//Servidor enviando LinkedList
					LinkedList<String> lista = new LinkedList<String>();
					lista.add("A");
					lista.add("B");
					lista.add("C");
					saida.writeObject(lista);
					break;
					
				case 3:
					//Servidor enviando array
					int[] array = {1,2,3,4,5};
					saida.writeObject(array);
					break;
				}
				
				entrada.close();
				saida.close();

				System.out.println("S#: Cliente desconectado: " + cliente.getInetAddress().getHostAddress()+":"+cliente.getPort());
				cliente.close();
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {

		} 
	} 
}

//Leia mais em: Java Sockets: Criando comunicações em Java http://www.devmedia.com.br/java-sockets-criando-comunicacoes-em-java/9465#ixzz43FfIbrSZ
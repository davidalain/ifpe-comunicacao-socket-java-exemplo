package simple;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class BatePapoCliente {

	public static void main(String[] args) {

		try {
			Scanner teclado = new Scanner(System.in);

			System.out.println("Digite seu nome: ");
			String nome = teclado.nextLine();

			String texto = "";

			System.out.println("Conectando ao servidor...");
			Socket cliente = new Socket("127.0.0.1",12345);
			System.out.println("Conectado!");
			
			System.out.println("Para enviar uma mensagem, escreva no console e pressione ENTER");

			ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
			ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());

			while(true){

				if(entrada.available() > 0){

					String str = (String) entrada.readObject();
					System.out.println(str);
				}

				texto = teclado.nextLine();
				saida.writeObject((nome + texto));
			}

		} catch(Exception e) {
			System.out.println("C#: Erro: " + e.getMessage());
		}
	}

}

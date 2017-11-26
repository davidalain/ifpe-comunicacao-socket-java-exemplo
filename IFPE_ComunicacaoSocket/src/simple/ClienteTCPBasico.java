package simple;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class ClienteTCPBasico {
	
	public static void main(String[] args) {
		
		try {
			Socket servidor = new Socket("127.0.0.1",12345);
			System.out.println("C#: Conexão aberta com o servidor " + servidor.getInetAddress().getHostAddress() + " na porta " + servidor.getPort());
			
			ObjectInputStream entrada = new ObjectInputStream(servidor.getInputStream());
			ObjectOutputStream saida = new ObjectOutputStream(servidor.getOutputStream()); //Envia o cabeçalho do OutputStream através da rede para desbloquear o getInputStream() no lado do servidor.
			saida.flush(); //força o envio do cabeçalho do OutputStream através da rede
			
			Object obj = entrada.readObject();
			
			if(obj instanceof Date){
				Date dataAtual = (Date) obj;
				JOptionPane.showMessageDialog(null,"Data recebida do servidor: " + dataAtual.toString());
				
			} else if(obj instanceof String){
				String str = (String) obj;
				JOptionPane.showMessageDialog(null,"String recebida do servidor: \"" + str + "\"");
				
			} else if(obj instanceof LinkedList){
				LinkedList list = (LinkedList) obj;
				JOptionPane.showMessageDialog(null,"Lista recebida do servidor: " + list.toString());
				
			} else if(obj instanceof int[]){
				int[] array = (int[]) obj;
				JOptionPane.showMessageDialog(null,"Array recebido do servidor: " + Arrays.toString(array));
				
			}
			
			entrada.close();
			saida.close();
			
			servidor.close();
			System.out.println("C#: Conexão encerrada");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}


//Leia mais em: Java Sockets: Criando comunicações em Java http://www.devmedia.com.br/java-sockets-criando-comunicacoes-em-java/9465#ixzz43FgH7Gj2
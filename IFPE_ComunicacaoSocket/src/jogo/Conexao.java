package jogo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import jogo.mensagem.Mensagem;

public class Conexao{

	private Socket socket;
	private Recepcao entrada;
	private Transmissao saida;
	private int id = 0;

	public Conexao(Socket socket) throws IOException {
		this.socket = socket;

		this.saida = new Transmissao(socket);
		this.entrada = new Recepcao(socket);
		
		this.saida.start();
		this.entrada.start();
	}

	public Conexao(Socket socket, int id) throws IOException {
		this(socket);
		this.id = id;
	}

	public Socket getSocket() {
		return socket;
	}

	public int getId() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return "socket("+id+"){"+ socket.getInetAddress().getHostAddress() + ":" + socket.getPort()+ "}";
	}

	public synchronized Mensagem lerMensagem(){
		return this.entrada.lerMensagem();
	}

	public synchronized void enviarMensagem(Mensagem mensagem){
		this.saida.enviarMensagem(mensagem);
	}
	
	public synchronized void limparSaida() throws IOException {
		this.saida.limparSaida();
	}
	
	public synchronized void limparEntrada() {
		this.entrada.limparEntrada();
	}
	
	public synchronized void limpar() throws IOException {
		this.limparEntrada();
		this.limparSaida();
	}


	public class Recepcao extends Thread{

		private ObjectInputStream entrada;
		private Queue<Mensagem> fila;

		public Recepcao(Socket socket) throws IOException {
			this.entrada = new ObjectInputStream(socket.getInputStream());
			this.fila = new ArrayBlockingQueue<>(10);
		}

		@Override
		public void run() {

			try {

				while(true) {

					Mensagem m = (Mensagem) this.entrada.readObject();
//					System.out.println("Recebeu mensagem:" + m);
					this.fila.offer(m);

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public Mensagem lerMensagem() {
			return this.fila.poll();
		}
		
		public void limparEntrada() {
			this.fila.clear();
		}

	}

	public class Transmissao extends Thread{

		private ObjectOutputStream saida;
		private Queue<Mensagem> fila;

		public Transmissao(Socket socket) throws IOException {
			this.saida = new ObjectOutputStream(socket.getOutputStream());
			this.fila = new ArrayBlockingQueue<>(10);
			
			this.saida.flush();
		}

		@Override
		public void run() {

			try {

				while(true) {

					if(this.fila.size() > 0) {
						Mensagem m = this.fila.remove();
						this.saida.writeObject(m);
//						System.out.println("Enviou a mensagem: " + m);
					}else {
						Thread.sleep(100);
					}

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public void enviarMensagem(Mensagem m) {
			this.fila.offer(m);	
		}
		
		public void limparSaida() throws IOException {
			this.fila.clear();
			this.saida.flush();
		}

	}




	//	public synchronized Mensagem lerMensagem() throws IOException{
	//		//Se tem mensagem pra receber, então lê a mensagem.
	//		if(this.entrada.available() >= Mensagem.MSG_SIZE) {
	//
	//			byte[] campos = new byte[Mensagem.MSG_SIZE];
	//			this.entrada.read(campos, 0, campos.length);
	//
	//			return Mensagem.decodifica(campos);
	//		}
	//
	//		return null;
	//	}
	//
	//	public synchronized void enviarMensagem(Mensagem mensagem) throws IOException {
	//		if(mensagem.campos() == null || mensagem.campos().length != Mensagem.MSG_SIZE)
	//			throw new InvalidParameterException("Mensagem inválida");
	//		
	//		this.saida.write(mensagem.campos(), 0, Mensagem.MSG_SIZE);
	//	}

}

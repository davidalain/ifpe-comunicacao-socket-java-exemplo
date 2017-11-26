package threaded.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import threaded.Config;

public class ClienteUDPThreaded extends Thread{

	private int id;
	private String host;
	private int port;
	
	public ClienteUDPThreaded(int id, String host, int port) {
		this.id = id;
		this.host = host;
		this.port = port;
	}
	
	@Override
	public void run() {

		DatagramSocket clientSocket = null;
		
		try {
			
			InetAddress destinationAddress = InetAddress.getByName(host);
			int destinationPort = port;
			byte[] sendData = new byte[]{Config.KEY};
			byte[] receiveData = new byte[Config.BUFFER_SIZE];
			
			clientSocket = new DatagramSocket();

			while(true){
				
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, destinationAddress, destinationPort);
				clientSocket.send(sendPacket);
				
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				
//				System.out.println("C# id="+id + ", array=" +Arrays.toString(receiveData));
			}


		} catch(Exception e) {
			clientSocket.close();
			
		}finally {
			System.err.println("C# Conexão fechada do cliente UDP " + id);
		}

	}


//	public static void main(String args[]) throws Exception {
//		
//		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
//		DatagramSocket clientSocket = new DatagramSocket();
//		
//		InetAddress IPAddress = InetAddress.getByName("localhost");
//		byte[] sendData = new byte[1024];
//		byte[] receiveData = new byte[1024];
//		
//		String sentence = inFromUser.readLine();
//		sendData = sentence.getBytes();
//		
//		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
//		clientSocket.send(sendPacket);
//		
//		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//		clientSocket.receive(receivePacket);
//		String modifiedSentence = new String(receivePacket.getData());
//		
//		System.out.println("FROM SERVER:" + modifiedSentence);
//		clientSocket.close();
//	}
}

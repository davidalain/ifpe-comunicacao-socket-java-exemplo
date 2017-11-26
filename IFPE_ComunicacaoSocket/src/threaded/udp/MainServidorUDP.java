package threaded.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import threaded.Config;

public class MainServidorUDP{

	public static void main(String args[]){

		DatagramSocket serverSocket = null;
		int pacoteNum = 0;
		Random r = new Random();
		long millisStart = System.currentTimeMillis();
		
		try{

			serverSocket = new DatagramSocket(Config.PORT);
			System.out.println("S#: Servidor UDP ouvindo a porta " + Config.PORT);

			byte[] receiveData = new byte[1];
			byte[] sendData = new byte[Config.BUFFER_SIZE];

			while(true){
				
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);

				if(receivePacket.getLength() > 0){
					if(receiveData[0] == Config.KEY){

						InetAddress destinationAddress = receivePacket.getAddress();
						int destinationPort = receivePacket.getPort();

						r.nextBytes(sendData);

						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, destinationAddress, destinationPort);
						serverSocket.send(sendPacket);

						receiveData[0] = 0;

						System.out.println("seg: "+((double)(System.currentTimeMillis()-millisStart))/1000.0+", pacoteNum: "+pacoteNum++);
					}
				}

			}
			
		}catch (Exception e) {
			e.printStackTrace();
			
			serverSocket.close();
		}

	}

}
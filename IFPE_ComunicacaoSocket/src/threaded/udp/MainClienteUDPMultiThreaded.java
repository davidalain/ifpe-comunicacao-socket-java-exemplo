package threaded.udp;

import threaded.Config;

public class MainClienteUDPMultiThreaded {
	
	public static void main(String[] args) {

		try {
			
			for(int i = 0 ; i < Config.CONNECTIONS ; i++){
				new ClienteUDPThreaded(i, Config.HOST, Config.PORT).start();
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}

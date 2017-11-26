package threaded.tcp;

import threaded.Config;

public class MainClienteTCPMultiThreaded {
	
	public static void main(String[] args) {

		try {
			
			for(int i = 0 ; i < Config.CONNECTIONS ; i++){
				new ClienteTCPThreaded(i, Config.HOST, Config.PORT).start();
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}

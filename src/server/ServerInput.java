package server;

import java.io.IOException;

public class ServerInput extends Thread{
	
	private Traker user;
	
	public ServerInput(Traker user){
		this.user = user;
	}
	
	@Override
	public void run(){
		while(user.isConnected()){
			serve();
			try {
				sleep(600);
			} catch (InterruptedException e) {
			}
		}
	}

	private void serve() {
		try {
			System.out.println("In: Preparing to read");
			Msg temp = (Msg) user.getIn().readObject();
			System.out.println("In: Received");
			MuffinServer.getInstanciaUnica().recive(temp);
		} catch (IOException e3) {
			MuffinServer.getInstanciaUnica().disconnecUser(user);
		} catch (ClassNotFoundException e) {
			System.out.println("In: The class recived does not correspond to the expected");
		}
	}

}

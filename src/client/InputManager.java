package client;

import java.io.IOException;
import java.io.ObjectInputStream;

import server.Msg;

public class InputManager extends Thread{
	
	private ObjectInputStream in;
	
	public InputManager(ObjectInputStream in){
		this.in = in;
	}
	
	@Override
	public void run(){
		while (true) {
			if (QuequeApp.getInstanciaUnica().isConnected()) {
				serve();
			}
			try {
				sleep(600);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private void serve(){
		try {
			System.out.println("In: Preparing to read");
			Msg temp = (Msg) in.readObject();
			System.out.println("In: Received");
			QuequeApp.getInstanciaUnica().recive(temp);
		} catch (IOException e3) {
			QuequeApp.getInstanciaUnica().disconnect();
		} catch (ClassNotFoundException e) {
			System.out.println("In: The class recived does not correspond to the expected");
		}
	}

}

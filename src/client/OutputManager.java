package client;

import java.io.IOException;
import java.io.ObjectOutputStream;

import server.Msg;

public class OutputManager extends Thread{
	
	private ObjectOutputStream out;
	
	public OutputManager(ObjectOutputStream out){
		this.out = out;
	}
	
	@Override
	public void run(){
		while(QuequeApp.getInstanciaUnica().isConnected()){
			serve();
			try {
				sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private synchronized void serve(){
		while(!QuequeApp.getInstanciaUnica().hasMsg()){
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		Msg temp = QuequeApp.getInstanciaUnica().send();
		try{
			System.out.println("Out: Message on memory");
			out.writeObject(temp);
			System.out.println("Out: Sended");
			out.flush();
			System.out.println("Out: Flush");
			QuequeApp.getInstanciaUnica().msgSent(temp);
		}catch(IOException e){
			QuequeApp.getInstanciaUnica().addMsg(temp);
		}
	}
	
	public synchronized void notifyOut(){
		notifyAll();
	}
	
	

}

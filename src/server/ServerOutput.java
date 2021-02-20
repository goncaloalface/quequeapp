package server;

import java.io.IOException;

public class ServerOutput extends Thread{

	public ServerOutput(){
	}
	
	public void run(){
		while(true){
			serve();
			try {
				sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}

	private synchronized void serve() {
		while(MuffinServer.getInstanciaUnica().getTotalMsgWaiting()==0){
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		Msg temp = MuffinServer.getInstanciaUnica().nextMsg();
		if(MuffinServer.getInstanciaUnica().searchUser(temp.getTo())==null){
			MuffinServer.getInstanciaUnica().errorMsg(temp);
		}
		else{
			Traker to = MuffinServer.getInstanciaUnica().searchUser(temp.getTo());
			Traker from = MuffinServer.getInstanciaUnica().searchUser(temp.getFrom());
			try {		
				to.getOut().writeObject(temp);
				to.getOut().flush();
				from.getOut().writeObject(new Msg(temp.getTo(),"","Server"));
				from.getOut().flush();
			} catch (IOException e) {
				System.out.println("Server Out: Could not Send");
			}
		}
			
	}
	
	public synchronized void notifyOut(){
		notifyAll();
	}
}

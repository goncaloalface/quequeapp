package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

@SuppressWarnings("serial")
public class Traker implements Serializable {

	private String user;
	private Socket socket;

	private ServerInput Input;

	private ObjectInputStream in;
	private ObjectOutputStream out;

	private boolean connected;

	public boolean isConnected() {
		return connected;
	}

	public void connect() {
		connected = true;
	}
	
	public void disconnect() {
		connected = false;
	}

	public Traker(String user) {
		this.user = user;
	}

	public String getUser() {
		return this.user;
	}

	public Socket getSocket() {
		return this.socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public ServerInput getInputManager(){
		return Input;
	}

	public void setInputManager(ServerInput Input) {
		this.Input = Input;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public void setIn(ObjectInputStream in) {
		this.in = in;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}


}

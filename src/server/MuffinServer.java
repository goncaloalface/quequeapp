package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

public class MuffinServer {
	
	private static MuffinServer instance;
	public static MuffinServer getInstanciaUnica(){
		if(instance==null)
			instance = new MuffinServer();
		return instance;
	}

	public static final int PORT = 8080;
	
	private ServerSocket server;
	
	private ArrayList<Traker> users = new ArrayList<Traker>();
	private ArrayList<Msg> msgQueue = new ArrayList<Msg>();
	
	private Thread ConnectionManager;
	private ServerOutput Output;
	
	private boolean running = false;
	
	private int SentMsg;

	private MuffinServer(){
		SentMsg=0;
		
		ConnectionManager = new Thread(){
			@Override
			public void run(){
				while(running){
					ObjectInputStream in;
					try {
						Socket socket = server.accept();
						in = prepareI(socket);
						Traker temp = (Traker) in.readObject();
						if((searchUser(temp.getUser()))==null){
							temp.connect();
							temp.setSocket(socket);
							temp.setIn(in);
							temp.setOut(prepareO(socket));
							temp.setInputManager(new ServerInput(temp));
							temp.getInputManager().start();
							users.add(temp);
						}
					} 
					catch (IOException e1) {
						System.out.println("Connection timed out");
					}catch (ClassNotFoundException e2) {
						System.out.println("The class recived does not correspond to the expected");
					}
					}
			}
		};
		
		Output = new ServerOutput();
	}

	private ObjectInputStream prepareI(Socket socket) throws IOException{
		return new ObjectInputStream(socket.getInputStream());
	}
	private ObjectOutputStream prepareO(Socket socket) throws IOException{
		return new ObjectOutputStream(socket.getOutputStream());
	}
	
	public Traker searchUser(String name){
		for(int i=0; i<users.size();i++){
			if(users.get(i).getUser().equals(name)){
				return users.get(i);
			}
		}
		return null;
	}
	
	
	public boolean isRunning() {
		return running;
	}
	
	public void startServer(){
		try {
			server = new ServerSocket(PORT);
			running = true;
		} catch (IOException e) {
			System.out.println("Already Running");
			System.exit(0);
		}
		ConnectionManager.start();
		Output.start();
	}

	public void stopServer() {
		running = false;
		for(int i=0; i<users.size(); i++){
			users.get(i).disconnect();
		}
	}
	
	public void closeServerSocket(){
		try{
			server.close();
			server=null;
		}
		catch(IOException e){
			System.out.println("Could not close server");
		}
	}

	public int getTotalMsgSent(){
		return SentMsg;
	}
	
	public int getTotalMsgWaiting(){
		return msgQueue.size();
	}
	
	public DefaultListModel<String> getConnectedUsers(){
		DefaultListModel<String> model  = new DefaultListModel<String>();
		for(Traker u: users){
				model.addElement(u.getUser());
		}
		return model;
	}

	public void recive(Msg temp) {
		msgQueue.add(temp);
		Output.notifyOut();
	}
	
	public void disconnecUser(Traker user){
		user.disconnect();
		users.remove(user);
	}

	public Msg nextMsg() {
		SentMsg++;
		Msg temp = msgQueue.get(0);
		msgQueue.remove(0);
		return temp;
	}
	
	public void errorMsg(Msg temp){
		SentMsg--;
		msgQueue.add(temp);
		Output.notifyOut();
	}
	
}

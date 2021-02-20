package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import client.Chat;
import server.Msg;
import server.MuffinServer;
import server.Traker;
import utils.GetMiddleScreen;
import utils.Write;

public class QuequeApp {
	
	private static QuequeApp instance;
	public static void setInstanciaUnica(String user, File dir){
		instance = new QuequeApp(user, dir);
	}
	public static QuequeApp getInstanciaUnica(){
		return instance;
	}

	private String user;
	private File dir;

	private JFrame window;
	private JButton add;
	private JButton remove;
	private JList<String> listContacts;
	private DefaultListModel<String> model;
	
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private ArrayList<Msg> msgQueue = new ArrayList<Msg>();
	private ArrayList<Chat> chats = new ArrayList<Chat>();
	
	private boolean connected = false;
	
	private Thread UpdateGUI;
	private Thread ConnectToServer;
	
	private InputManager Input;
	private OutputManager Output;

	private QuequeApp(String user, File dir) {
		this.user = user;
		this.dir = dir;

		window= new JFrame("QuequeApp");
		window.setIconImage(new ImageIcon(System.getProperty("user.dir") + "\\Resources\\Queque app.png").getImage());
		window.setSize(300, 400);
		window.setResizable(false);
		window.setLocation(GetMiddleScreen.getPoint(window));
		
		// ___________________________________________Button Panel_________________________________________
		
		JPanel buttonPanel = new JPanel();
		add = new JButton("Add");
		remove = new JButton("Remove");
		buttonPanel.add(add);
		buttonPanel.add(remove);

		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String novoCont = JOptionPane.showInputDialog("Add Contact");
				if (novoCont != null) {
					if (novoCont.equals(user))
						JOptionPane.showMessageDialog(window, "Unable do add yourself!");
					else {
						if (model.contains(novoCont) == false) {
							model.addElement(novoCont);
							chats.add(new Chat(user,novoCont,dir));
						}
					}
				}
			}
		});
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = listContacts.getSelectedIndex();
				if (index >= 0) {
					model.removeElementAt(index);
					chats.remove(index);
				} else {
					JOptionPane.showMessageDialog(window, "You need to select a contact!");
				}
			}
		});
		
		// ___________________________________________Contact List_________________________________________
		
		listContacts = new JList<String>();
		JScrollPane scrollLista = new JScrollPane(listContacts, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		model = Write.loadContacts(user, dir);
		listContacts.setModel(model);
		listContacts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listContacts.setLayoutOrientation(JList.VERTICAL);
		listContacts.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					search(listContacts.getSelectedValue().toString()).setVisible();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

		});
		
		// ___________________________________________Label Panel__________________________________________
		
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BorderLayout());
		JLabel lWell = new JLabel("Welcome, " + user + "!");
		JLabel lCont = new JLabel("Contacts: ");
		JLabel lStatus = new JLabel("Offline");
		JLabel lYouAre = new JLabel("You are: ");
		lStatus.setForeground(Color.RED);
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new GridLayout(1, 2));
		statusPanel.add(lYouAre);
		statusPanel.add(lStatus);

		labelPanel.add(lWell, BorderLayout.WEST);
		labelPanel.add(lCont, BorderLayout.SOUTH);
		labelPanel.add(statusPanel, BorderLayout.EAST);
		
		// __________________________________________Final GUI Config________________________________________
		
		window.add(labelPanel, BorderLayout.NORTH);
		window.add(scrollLista);
		window.add(buttonPanel, BorderLayout.SOUTH);
		window.addWindowListener(new WindowListener() {

			public void windowActivated(WindowEvent e) {
			}

			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				for (Chat c : chats) {
					c.saveChat();
				}
				Write.saveContacts(user, dir, model);
				System.exit(0);
			}

			public void windowDeactivated(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowOpened(WindowEvent e) {
			}

		});
		
		//______________________________________________________Threads_________________________________________
		
		ConnectToServer = new Thread() {
			public void run() {
				connected = false;
				while (connected == false) {
					try {
						connectToServer();
						connect();
					} catch (IOException e1) {

					}
					if (connected == true) {
						UpdateGUI.start();
						Output = new OutputManager(out);
						Input= new InputManager(in);
						Output.start();
						Input.start();
					}
					try {
						sleep(5000);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		ConnectToServer.start();
		
		UpdateGUI = new Thread() {
			public void run() {
				while(true){
					if (connected == true) {
						lStatus.setForeground(Color.GREEN);
						lStatus.setText("Online");
					}
					else{
						lStatus.setForeground(Color.RED);
						lStatus.setText("Offline");
					}
					try {
						sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		loadChats();
		
	}
	
	private void loadChats(){
		for (Object o : model.toArray()) {
			String s = (String) o;
			chats.add(new Chat(user, s , dir));
		}
	}
	
	
	private void prepareI(Socket socket) throws IOException {
		in = new ObjectInputStream(socket.getInputStream());
	}

	private void prepareO(Socket socket) throws IOException {
		out = new ObjectOutputStream(socket.getOutputStream());
	}
	
	private Chat search(String user) {
		for (int i = 0; i < chats.size(); i++) {
			if (chats.get(i).getChatTo().equals(user))
				return chats.get(i);
		}
		return null;
	}
	
	public boolean isConnected(){
		return connected;
	}
	
	public void connect(){
		connected = true;
	}
	
	public void disconnect(){
		connected = false;
	}
	
	public void connectToServer() throws IOException {
		InetAddress endereco = InetAddress.getByName(null);
		socket = new Socket(endereco, MuffinServer.PORT);
		prepareO(socket);
		out.writeObject(new Traker(user));
		out.flush();
		prepareI(socket);
	}
	
	public void addMsg(Msg temp){
		msgQueue.add(temp);
		Output.notifyOut();
	}
	
	public void recive(Msg temp){
		if (search(temp.getFrom()) == null) {
			if (model.contains(temp.getFrom()) == false) {
				model.addElement(temp.getFrom());
				Chat chat = new Chat(user,temp.getFrom(),dir);
				chats.add(chat);
				chat.addMsgToChat(temp);
				chat.setVisible();
			}
		} else {
			search(temp.getFrom()).addMsgToChat(temp);
		}
	}
	
	public Msg send(){
		Msg temp = msgQueue.get(0);
		msgQueue.remove(0);
		return temp;
	}
	
	public void msgSent(Msg temp) {
		search(temp.getTo()).addMsgToChat(temp);
	}
	
	public boolean hasMsg(){
		return msgQueue.size() != 0 ;
	}
	

	public void run() {
		window.setVisible(true);
	}
}

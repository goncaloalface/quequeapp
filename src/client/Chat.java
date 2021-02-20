package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import server.Msg;
import utils.GetMiddleScreen;
import utils.Write;

public class Chat {
	
	String User;
	String To;
	File Dir;
	
	JFrame wChat;
	JTextField tEnvio;
	JButton bEnviar;
	JTextArea tChat;

	public Chat(String User, String To, File Dir) {
		
		this.User = User;
		this.To = To;
		this.Dir = Dir;

		wChat = new JFrame(To);
		wChat.setIconImage(new ImageIcon(System.getProperty("user.dir") + "\\Resources\\Queque app.png").getImage());
		wChat.setSize(300, 400);
		wChat.setLocation(GetMiddleScreen.getPoint(wChat));
		wChat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel bChat = new JPanel();
		bChat.setLayout(new GridLayout(1, 2));

		tEnvio = new JTextField();
		bEnviar = new JButton("send");
		tChat = new JTextArea();
		JScrollPane scrollTextArea = new JScrollPane(tChat, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		Write.loadChat(tChat, User + "-" + To, Dir);

		bEnviar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				QuequeApp.getInstanciaUnica().addMsg(new Msg(User, To, tEnvio.getText()));
				tEnvio.setText("");
			}
		});
		tEnvio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				QuequeApp.getInstanciaUnica().addMsg(new Msg(User, To, tEnvio.getText()));
				tEnvio.setText("");
			}
		});

		tChat.setEditable(false);
		tEnvio.setSize(2000, 2000);
		bChat.add(tEnvio);
		bChat.add(bEnviar);
		wChat.add(bChat, BorderLayout.SOUTH);
		wChat.add(scrollTextArea);
	}
	
	public String getChatTo(){
		return To;
	}
	
	public void addMsgToChat(Msg msg) {
		if(msg.getText().equals("Server")){
			tChat.append("Mensagem enviada" + '\n');
		}
		else{
			tChat.append(msg.getFrom() + ": " + msg.getText() + '\n');
		}
	}
	
	public void saveChat(){
		Write.saveChat(tChat, User + "-" + To, Dir);
	}
	
	public void setVisible(){
		wChat.setVisible(true);
	}
}

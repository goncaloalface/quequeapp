package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import utils.GetMiddleScreen;

import server.MuffinServer;

public class MuffinServerGUI {

	
	private JFrame serverWindow;
	private JLabel ServerStatus;
	private JLabel ServerIn;
	private JLabel ServerOut;
	private JLabel TotalOn;
	private JLabel Espera;
	private JLabel Enviadas;
	
	private Thread UpdateGUI;

	
	public MuffinServerGUI(){
		
		serverWindow = new JFrame("Muffin Server");
		serverWindow.setSize(500, 300);
		serverWindow.setLocation(GetMiddleScreen.getPoint(serverWindow));
		serverWindow.setResizable(false);
		serverWindow.setIconImage(new ImageIcon(System.getProperty("user.dir") + "\\Resources\\Server app.png").getImage());
		serverWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JList<String> listUsers = new JList<String>();
		JScrollPane scrollUsers = new JScrollPane(listUsers, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		//________________________________________Port and Running Status___________________________________________
		
		JPanel statusPanel = new JPanel();
		JLabel l0 = new JLabel("Server is: ");
		JLabel l1 = new JLabel("Server In Manager: ");
		JLabel l2 = new JLabel("Server Out Manager: ");
		JLabel l3 = new JLabel("Total users online:");
		JLabel l4 = new JLabel("Total mensanges send:");
		JLabel l5 = new JLabel("Total mensanges waiting:");
		
		ServerStatus = new JLabel("Offline");
		ServerStatus.setForeground(Color.RED);
		ServerIn = new JLabel("Not Running");
		ServerIn.setForeground(Color.RED);
		ServerOut = new JLabel("Not Running");
		ServerOut.setForeground(Color.RED);
		TotalOn = new JLabel("0");
		TotalOn.setForeground(Color.GRAY);
		Espera = new JLabel("0");
		Espera.setForeground(Color.GRAY);
		Enviadas = new JLabel("0");
		Enviadas.setForeground(Color.GRAY);
		
		statusPanel.add(l0);
		statusPanel.add(ServerStatus);
		statusPanel.add(l1);
		statusPanel.add(ServerIn);
		statusPanel.add(l2);
		statusPanel.add(ServerOut);
		statusPanel.add(l3);
		statusPanel.add(TotalOn);
		statusPanel.add(l5);
		statusPanel.add(Espera);
		statusPanel.add(l4);
		statusPanel.add(Enviadas);
		
		//________________________________________Port and Running Status___________________________________________
		JLabel l7 = new JLabel("Connected Users");
		JPanel portPanel = new JPanel();
		portPanel.setLayout(new GridLayout(1,2));
		JLabel portText = new JLabel("Server Port is: "+MuffinServer.PORT);
		

		portPanel.add(l7);
		portPanel.add(portText);
		
		//___________________________________________Button Panel___________________________________________
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		
		JButton Kick = new JButton("Kick");
		buttonPanel.add(Kick);
		//TODO KICK Button
		
		JPanel statusButtonPanel = new JPanel();
		statusButtonPanel.setLayout(new GridLayout(1,2));
		JButton Start = new JButton("Start");
		Start.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(MuffinServer.getInstanciaUnica().isRunning()==true){
					JOptionPane.showMessageDialog(serverWindow, "Server Already On");
				}
				else{
					MuffinServer.getInstanciaUnica().startServer();
					ServerIn.setForeground(Color.GREEN);
					ServerIn.setText("Running");
					ServerOut.setForeground(Color.GREEN);
					ServerOut.setText("Running");
					ServerStatus.setForeground(Color.GREEN);
					ServerStatus.setText("Online");
					serverWindow.repaint();
					serverWindow.revalidate();
					UpdateGUI.start();
				}
			}
		});
		JButton Stop = new JButton ("Stop and Exit");
		Stop.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(MuffinServer.getInstanciaUnica().isRunning()==false){
					System.exit(0);
				}
				else{
					MuffinServer.getInstanciaUnica().stopServer();
					if(MuffinServer.getInstanciaUnica().getTotalMsgWaiting()!=0){
						if(JOptionPane.showConfirmDialog(serverWindow, "There are still Msg waiting to be sent,"+'\n'+"Exit any way?")==JOptionPane.YES_OPTION){
							System.exit(0);
						}
						else{
							ServerIn.setForeground(Color.RED);
							ServerIn.setText("Stoped");
							ServerStatus.setForeground(Color.YELLOW);
							ServerStatus.setText("Clossing");
						}
					}
					else{
						System.exit(0);
					}
				}
			}
		});	
		statusButtonPanel.add(Start);
		statusButtonPanel.add(Stop);
		buttonPanel.add(statusButtonPanel);
		
		//________________________________________Final GUI Configs___________________________________________
		JPanel middle = new JPanel();
		middle.setLayout(new GridLayout(1,2));
		middle.add(scrollUsers);
		middle.add(statusPanel);
		
		serverWindow.add(middle);
		serverWindow.add(portPanel, BorderLayout.NORTH);
		serverWindow.add(buttonPanel, BorderLayout.SOUTH);
		
		//_________________________________________Thread UpdateGUI___________________________________________
		
		UpdateGUI = new Thread(){
			public void run(){
				while(true){
					listUsers.setModel(MuffinServer.getInstanciaUnica().getConnectedUsers());
					TotalOn.setText(listUsers.getModel().getSize()+"");
					Espera.setText(MuffinServer.getInstanciaUnica().getTotalMsgWaiting()+"");
					Enviadas.setText(MuffinServer.getInstanciaUnica().getTotalMsgSent()+"");
					serverWindow.repaint();
					serverWindow.revalidate();
					if(MuffinServer.getInstanciaUnica().getTotalMsgWaiting()==0 && MuffinServer.getInstanciaUnica().isRunning()==false){
						ServerStatus.setText("Offline");
						ServerStatus.setForeground(Color.RED);
						ServerOut.setText("Stoped");
						ServerOut.setForeground(Color.RED);
						MuffinServer.getInstanciaUnica().closeServerSocket();
						System.exit(0);
					}
					try {
						sleep(500);
					} catch (InterruptedException e) {
					}
				}
			}
		};
	}
	
	public void run(){
		MuffinServer.getInstanciaUnica();
		serverWindow.setVisible(true);
	}
}

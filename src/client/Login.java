package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.GetMiddleScreen;
import utils.Write;

public class Login {

	private JFrame LoginWin;
	private JButton LoginBut;
	private JTextField Username;

	public Login() {
		LoginWin = new JFrame("Login");
		LoginWin.setSize(250, 85);
		LoginWin.setIconImage(new ImageIcon(System.getProperty("user.dir") + "\\Resources\\Queque app.png").getImage());
		LoginWin.setLocation(GetMiddleScreen.getPoint(LoginWin));
		LoginWin.setResizable(false);
		LoginWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel LoginArea = new JPanel();
		LoginArea.setLayout(new FlowLayout());
		JLabel label = new JLabel("Username:");
		Username = new JTextField(15);

		Username.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Username.getText().equals("")) {
					JOptionPane.showMessageDialog(LoginWin, "Username not accepted");
				} else {
					File Dir = Write.isThereLog(Username.getText());
					if (Dir == null) {
						Dir = new AskPath(LoginWin, Username.getText()).getUserDirectory();
					}
					if (Dir != null) {
						Write.saveLog(Username.getText(), Dir);
						LoginWin.setVisible(false);
						QuequeApp.setInstanciaUnica(Username.getText(), Dir);
						QuequeApp.getInstanciaUnica().run();
						LoginWin.dispose();
					}
				}
			}
		});

		LoginBut = new JButton("Login");
		LoginBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Username.getText().equals("")) {
					JOptionPane.showMessageDialog(LoginWin, "Username not accepted");
				} else {
					File Dir = Write.isThereLog(Username.getText());
					if (Dir == null) {
						Dir = new AskPath(LoginWin, Username.getText()).getUserDirectory();
						Write.saveLog(Username.getText(), Dir);
					}
					if (Dir != null) {
						LoginWin.setVisible(false);
						QuequeApp.setInstanciaUnica(Username.getText(), Dir);
						QuequeApp.getInstanciaUnica().run();
						LoginWin.dispose();
					}
				}
			}
		});

		LoginArea.add(label);
		LoginArea.add(Username);

		LoginWin.add(LoginArea);
		LoginWin.add(LoginBut, BorderLayout.SOUTH);

		LoginWin.setVisible(true);
	}
}

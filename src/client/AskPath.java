package client;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import utils.Write;

public class AskPath {

private File path;
	
	public AskPath(JFrame Dad, String user){
		
		JFileChooser path = new JFileChooser();
		path.setCurrentDirectory(new File(""));
		path.setDialogTitle("User Directory");
		path.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		path.setAcceptAllFileFilterUsed(false);
		
		if(path.showOpenDialog(Dad)==JFileChooser.APPROVE_OPTION){
			if(!Write.isThere(user, path.getSelectedFile())){
				if(JOptionPane.showConfirmDialog(Dad, "No User Files detected on selected Directory"+'\n'+"Do you wish to proceed?","Alert" ,JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					this.path =path.getSelectedFile();
			}
			else{
				this.path =path.getSelectedFile();
			}
		}
		else
			JOptionPane.showMessageDialog(Dad, "No Directory Selected");
	}
	
	public File getUserDirectory(){
		return path;
	}
}

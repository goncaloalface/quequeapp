package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

public class Write {
	
	public static void saveContacts(String User, File dir, DefaultListModel<String> cont){
		File file = new File(dir,User+".txt");
		if(!isThere(User,dir)){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedWriter write = new BufferedWriter (new FileWriter(file));
		for(int i = 0; i<cont.getSize(); i++){
				write.write(cont.get(i));;
				write.newLine();
		}
		write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static DefaultListModel<String> loadContacts(String User, File dir){
		DefaultListModel<String> n = new DefaultListModel<String>();
		File file = new File(dir,User+".txt");
		if(isThere(User,dir)){
			try {
				BufferedReader reader = new BufferedReader (new FileReader(file));
				String line;
			while((line = reader.readLine())!=null){
					n.addElement(line);
			}
			reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return n;
	}

	public static void loadChat(JTextArea a, String name, File Dir){
		File file = new File(Dir,name+".txt");
		if(isThere(name,Dir)){
			try {
				BufferedReader reader = new BufferedReader (new FileReader(file));
				String line;
			while((line = reader.readLine())!=null){
					a.append(line+'\n');
			}
			reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void saveChat(JTextArea a, String name, File Dir){
		File file = new File(Dir,name+".txt");
		if(!isThere(name,Dir)){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			String[] lines = a.getText().split("\n");
			BufferedWriter write = new BufferedWriter (new FileWriter(file));
			for(int i=0; i<lines.length; i++){
					write.write(lines[i]);
					write.newLine();
			}
		write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveLog(String name, File Loc){
		File file = new File(System.getProperty("user.dir")+"\\Resources\\Login.txt");
		if(!isThere("Login",new File(System.getProperty("user.dir")+"\\Resources"))){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(isThereLog(name)==null){
		try {
			PrintWriter write = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			write.println(name+"-"+Loc.getPath());
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	}
	
	public static File isThereLog(String name){
		File file = new File(System.getProperty("user.dir")+"\\Resources\\Login.txt");
		File log = null;
		if(isThere("Login",new File(System.getProperty("user.dir")+"\\Resources"))){
			try {
			BufferedReader reader = new BufferedReader (new FileReader(file));
			String line;
			while((line = reader.readLine())!=null){
				if((line.split("-"))[0].equals(name)){
						log = new File((line.split("-"))[1]);
				}
			}
			reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return log;
	}
	
	public static boolean isThere(String file, File dir){
		for(int i = 0; i<dir.list().length; i++){
			if(((String[])dir.list())[i].equals(file+".txt")){
				return true;
			}	
		}
		return false;
	}
}

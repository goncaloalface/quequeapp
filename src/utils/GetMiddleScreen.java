package utils;

import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class GetMiddleScreen {
	public static Point getPoint(JFrame win){
		int w = win.getWidth()/2;
		int h = win.getHeight()/2;
		
		int wScreen = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2;
		int hScreen = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2;		
		
		Point p = new Point(wScreen-w,hScreen-h);
		return p;
	}
}

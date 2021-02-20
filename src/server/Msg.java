package server;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Msg implements Serializable{
	
	private String from;
	private String to;
	private String text;
	
	public Msg(String from, String to, String text) {
		this.from = from;
		this.to = to;
		this.text = text;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getText() {
		return text;
	}
}

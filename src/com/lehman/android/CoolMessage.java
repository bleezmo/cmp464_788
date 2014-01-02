package com.lehman.android;

public class CoolMessage {
	private String id;
	private String message;
	public CoolMessage(String id, String message){
		setId(id);
		setMessage(message);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String toString(){
		return "message with id "+id+" is "+message;
	}
}

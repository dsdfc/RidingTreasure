package com.example.bike.robot;



public class ChatMessage {


	/**
	 * 消息内容
	 */
	private String msg;


	// private Date date;

	// private String dateStr;

	// private String name;

	// public enum Type
	// {
	// INPUT, OUTPUT
	// }

	public ChatMessage() {
	}

	@Override
	public String toString() {
		return "ChatMessage [msg=" + msg + "]";
	}

	public ChatMessage(String msg) {
		super();
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
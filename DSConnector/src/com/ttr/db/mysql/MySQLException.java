package com.ttr.db.mysql;

public class MySQLException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public MySQLException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MySQLException(String message) {
		super(message);
		this.message=message;
		//System.out.println("MySQLException:" + message);
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}

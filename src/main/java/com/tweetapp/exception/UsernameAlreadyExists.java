package com.tweetapp.exception;

/**
 * @author cogjava729
 */
public class UsernameAlreadyExists extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7594046056061103234L;

	public UsernameAlreadyExists(String msg) {
		super(msg);
	}
}

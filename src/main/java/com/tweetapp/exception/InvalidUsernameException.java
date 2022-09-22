package com.tweetapp.exception;

/**
 * @author cogjava729
 */
public class InvalidUsernameException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5966130919267770338L;

	public InvalidUsernameException(String msg) {
		super(msg);
	}

}

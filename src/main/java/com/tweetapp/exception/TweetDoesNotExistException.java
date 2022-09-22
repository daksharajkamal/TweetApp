package com.tweetapp.exception;

/**
 * @author cogjava729
 */
public class TweetDoesNotExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -179246759582276729L;

	public TweetDoesNotExistException(String msg) {
		super(msg);
	}
}

package com.tweetapp.configs.metrics;

/**
 * Responsible to hold Prometheus Counter Metrics.
 * 
 * @author cogjava729
 */

public final class AppMetrics {

	private AppMetrics() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static final String LOGIN = "tweet.app.user.account.login";
	public static final String REGISTER = "tweet.app.user.account.register";
	public static final String POST = "tweet.app.user.tweets.post";
	public static final String UPDATE = "tweet.app.user.tweets.update";
	public static final String DELETE = "tweet.app.user.tweets.delete";
	public static final String LIKE = "tweet.app.user.tweets.like";
	public static final String DISLIKE = "tweet.app.user.tweets.dislike";
}

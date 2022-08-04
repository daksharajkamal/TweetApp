package com.tweetapp.dao;

import java.util.List;

import com.tweetapp.domain.Likes;
import com.tweetapp.domain.Tweets;

public interface TweetsDao {

	public Tweets save(Tweets tweets);

	public void delete(String id);

	public Tweets findTweetById(String id);

	public List<Tweets> getAllTweets();

	public Likes likeTweet(String id);

	public Likes save(Likes like);
}

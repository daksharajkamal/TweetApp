package com.tweetapp.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.dao.TweetsDao;
import com.tweetapp.dao.UsersDao;
import com.tweetapp.domain.Likes;
import com.tweetapp.domain.Tweets;
import com.tweetapp.domain.Users;
import com.tweetapp.payload.request.TweetRequest;
import com.tweetapp.payload.response.TweetResponse;
import com.tweetapp.payload.response.TweetResponseList;
import com.tweetapp.repository.LikeRepository;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.TweetServices;
import com.tweetapp.util.TweetAppConstants;

@Service
public class TweetServicesImpl implements TweetServices {

	private static final Logger LOG = LoggerFactory.getLogger(TweetServices.class);

	@Autowired
	UsersDao usersDao;

	@Autowired
	TweetsDao tweetsDao;

	@Autowired
	LikeRepository likeRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public void createTweet(TweetRequest tweetRequest) {
		LOG.info("inside createTweet()");
		Users user = usersDao.getUserByUsername(tweetRequest.getUsername());
		Tweets tweet = new Tweets(tweetRequest.getTweetString(), user);
		tweet.setCreatedBy(tweetRequest.getUsername());
		tweet.setCreatedAt(new Date());
		tweet = tweetsDao.save(tweet);
		if (tweet.getId() != null) {
			Set<Tweets> tweets = user.getTweets();
			tweets.add(tweet);
			user.setTweets(tweets);
			userRepository.save(user);
		}
	}

	@Override
	public void updateTweet(TweetRequest tweetRequest) {
		LOG.info("inside updateTweet()");
		Tweets tweet = tweetsDao.findTweetById(tweetRequest.getId());
		Users users = usersDao.getUserByUsername(tweetRequest.getUsername());
		LOG.info("User is havin Admin Role: " + usersDao.isUserAdmin(tweetRequest.getUsername()));
		if (tweet.getUsername().getId().equals(users.getId()) || usersDao.isUserAdmin(tweetRequest.getUsername())) {
			tweet.setTweetMessage(tweetRequest.getTweetString());
			tweet.setModifiedAt(new Date());
			tweet.setModifiedBy(tweetRequest.getUsername());
			tweetsDao.save(tweet);
		}
	}

	@Override
	public TweetResponseList getAllTweets() {
		List<Tweets> allTweets = tweetsDao.getAllTweets();
		TweetResponseList tweetList = new TweetResponseList();
		List<TweetResponse> tweets = new ArrayList<>();
		allTweets.forEach(tweet -> {
			TweetResponse tr = new TweetResponse();
			tr.setId(tweet.getId());
			tr.setTweetMessage(tweet.getTweetMessage());
			tr.setUsername(tweet.getUsername().getUsername());
			tr.setCreatedBy(tweet.getCreatedBy());
			tr.setCtearedAt(tweet.getCreatedAt());
			if (tweet.getModifiedBy() != null) {
				tr.setModifiedBy(tweet.getModifiedBy());
			}
			if (tweet.getModifiedAt() != null) {
				tr.setModifiedAt(tweet.getModifiedAt());
			}
			tweets.add(tr);
		});
		tweetList.setTweets(tweets);
		return tweetList;
	}

	@Override
	public Boolean deleteTweet(String username, String id) {
		Tweets tweet = tweetsDao.findTweetById(id);
		if (tweet.getUsername().getUsername().equals(username) || usersDao.isUserAdmin(username)) {
			tweetsDao.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public Boolean likeTweet(String username, String id) {
		Tweets tweet = tweetsDao.findTweetById(id);
		Likes like = null;
		if (!tweet.getLikes().isEmpty()) {
			Set<Likes> likes = tweet.getLikes();
			List<Likes> likedUser = likes.stream().filter(l -> l.getUsername().getUsername().equals(username))
					.collect(Collectors.toList());
			if (!likedUser.isEmpty()) {
				like = likedUser.get(0);
				if (like.getIsActive().equals(TweetAppConstants.CHARACTER_Y)) {
					like.setIsActive(TweetAppConstants.CHARACTER_N);
				} else if (like.getIsActive().equals(TweetAppConstants.CHARACTER_N)) {
					like.setIsActive(TweetAppConstants.CHARACTER_Y);
				}
			} else {
				like = new Likes();
				like.setTweet(tweet);
				like.setUsername(usersDao.getUserByUsername(username));
				like.setCreatedAt(new Date());
				like.setCreatedBy(username);
				like.setIsActive(TweetAppConstants.CHARACTER_Y);
			}
		} else {
			like = new Likes();
			like.setTweet(tweet);
			like.setUsername(usersDao.getUserByUsername(username));
			like.setCreatedAt(new Date());
			like.setCreatedBy(username);
			like.setIsActive(TweetAppConstants.CHARACTER_Y);
		}
		like = likeRepository.save(like);
		Set<Likes> likes = tweet.getLikes();
		likes.add(like);
		tweet.setLikes(likes);
		tweetsDao.save(tweet);
		if (like.getId() != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public TweetResponseList getAllTweetsByUsername(String username) {
		Users user = usersDao.getUserByUsername(username);
		TweetResponseList tweetList = new TweetResponseList();
		List<TweetResponse> tweets = new ArrayList<>();
		Set<Tweets> userTweets = user.getTweets();
		userTweets.forEach(tweet -> {
			TweetResponse tr = new TweetResponse();
			tr.setId(tweet.getId());
			tr.setTweetMessage(tweet.getTweetMessage());
			tr.setUsername(tweet.getUsername().getUsername());
			tr.setCreatedBy(tweet.getCreatedBy());
			tr.setCtearedAt(tweet.getCreatedAt());
			if (tweet.getModifiedBy() != null) {
				tr.setModifiedBy(tweet.getModifiedBy());
			}
			if (tweet.getModifiedAt() != null) {
				tr.setModifiedAt(tweet.getModifiedAt());
			}
			tweets.add(tr);
		});
		tweetList.setTweets(tweets);
		return tweetList;
	}

	@Override
	public void replyToTweet(TweetRequest tweetRequest, String tweetId) {
		LOG.info("inside replyToTweet()");
		Users user = usersDao.getUserByUsername(tweetRequest.getUsername());
		Tweets tweet = tweetsDao.findTweetById(tweetId);
		Tweets reTweet = new Tweets(tweetRequest.getTweetString(), user);
		reTweet.setCreatedBy(tweetRequest.getUsername());
		reTweet.setCreatedAt(new Date());
		reTweet = tweetsDao.save(reTweet);
		if (reTweet.getId() != null) {
			Set<Tweets> tweets = user.getTweets();
			Set<Tweets> tweetSet = tweet.getReTweets();
			tweets.add(reTweet);
			tweetSet.add(reTweet);
			user.setTweets(tweets);
			tweet.setReTweets(tweetSet);
			userRepository.save(user);
			tweetsDao.save(tweet);
		}
	}
}

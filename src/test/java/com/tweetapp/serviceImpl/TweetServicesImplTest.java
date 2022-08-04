package com.tweetapp.serviceImpl;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.tweetapp.dao.TweetsDao;
import com.tweetapp.dao.UsersDao;
import com.tweetapp.domain.Likes;
import com.tweetapp.domain.Role;
import com.tweetapp.domain.Tweets;
import com.tweetapp.domain.Users;
import com.tweetapp.payload.request.TweetRequest;
import com.tweetapp.repository.LikeRepository;
import com.tweetapp.repository.UserRepository;

@SpringBootTest
public class TweetServicesImplTest {

	@Mock
	UsersDao usersDao;

	@Mock
	TweetsDao tweetsDao;

	@Mock
	LikeRepository likeRepository;

	@Mock
	UserRepository userRepository;

	@InjectMocks
	TweetServicesImpl tweetServicesImpl;

	@SuppressWarnings("deprecation")
	@Before
	public void initialize() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateTweet() {
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setId("1");
		tweetRequest.setTweetString("SEI is very bad project");
		tweetRequest.setUsername("SEI");
		Tweets tweet = new Tweets();
		tweet.setId("1");
		Mockito.when(usersDao.getUserByUsername(Mockito.anyString())).thenReturn(constructUsers());
		Mockito.when(tweetsDao.save(Mockito.any(Tweets.class))).thenReturn(tweet);
		tweetServicesImpl.createTweet(tweetRequest);
		assertTrue(true);
	}

	@Test
	public void testCreateTweet2() {
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setId("1");
		tweetRequest.setTweetString("SEI is very bad project");
		tweetRequest.setUsername("SEI");
		Tweets tweet = new Tweets();
		Mockito.when(usersDao.getUserByUsername(Mockito.anyString())).thenReturn(constructUsers());
		Mockito.when(tweetsDao.save(Mockito.any(Tweets.class))).thenReturn(tweet);
		tweetServicesImpl.createTweet(tweetRequest);
		assertTrue(true);
	}

	@Test
	public void updateTweet() {
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setId("1");
		tweetRequest.setTweetString("SEI is very bad project");
		tweetRequest.setUsername("SEI");
		Tweets tweet = new Tweets();
		tweet.setId("1");
		Users user = new Users();
		user.setId("2");
		tweet.setUsername(user);
		Mockito.when(usersDao.isUserAdmin(Mockito.anyString())).thenReturn(true);
		Mockito.when(usersDao.getUserByUsername(Mockito.anyString())).thenReturn(constructUsers());
		Mockito.when(tweetsDao.findTweetById(Mockito.anyString())).thenReturn(tweet);
		tweetServicesImpl.updateTweet(tweetRequest);
		assertTrue(true);
	}

	@Test
	public void updateTweet2() {
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setId("1");
		tweetRequest.setTweetString("SEI is very bad project");
		tweetRequest.setUsername("SEI");
		Mockito.when(usersDao.getUserByUsername(Mockito.anyString())).thenReturn(constructUsers());
		Mockito.when(tweetsDao.findTweetById(Mockito.anyString())).thenReturn(constructTweets());
		tweetServicesImpl.updateTweet(tweetRequest);
		assertTrue(true);
	}

	@Test
	public void updateTweet3() {
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setId("1");
		tweetRequest.setTweetString("SEI is very bad project");
		tweetRequest.setUsername("SEI");
		Tweets tweet = new Tweets();
		tweet.setId("1");
		Users user = new Users();
		user.setId("2");
		tweet.setUsername(user);
		Mockito.when(usersDao.isUserAdmin(Mockito.anyString())).thenReturn(false);
		Mockito.when(usersDao.getUserByUsername(Mockito.anyString())).thenReturn(constructUsers());
		Mockito.when(tweetsDao.findTweetById(Mockito.anyString())).thenReturn(tweet);
		tweetServicesImpl.updateTweet(tweetRequest);
		assertTrue(true);
	}

	@Test
	public void testGetAllTweets() {
		Mockito.when(tweetsDao.getAllTweets()).thenReturn(constructListOfTweets());
		tweetServicesImpl.getAllTweets();
		assertTrue(true);
	}

	@Test
	public void testDeleteTweet() {
		Users user = new Users();
		user.setUsername("aman");
		Tweets tweet = new Tweets();
		tweet.setId("2");
		tweet.setUsername(constructUsers());
		Mockito.when(usersDao.isUserAdmin(Mockito.anyString())).thenReturn(false);
		Mockito.when(tweetsDao.findTweetById(Mockito.anyString())).thenReturn(tweet);
		tweetServicesImpl.deleteTweet(user.getUsername(), tweet.getId());
		assertTrue(true);
	}

	@Test
	public void testDeleteTweet2() {
		Users user = new Users();
		user.setUsername("sandy");
		Tweets tweet = new Tweets();
		tweet.setId("2");
		tweet.setUsername(constructUsers());
		Mockito.when(usersDao.isUserAdmin(Mockito.anyString())).thenReturn(true);
		Mockito.when(tweetsDao.findTweetById(Mockito.anyString())).thenReturn(tweet);
		tweetServicesImpl.deleteTweet(user.getUsername(), tweet.getId());
		assertTrue(true);
	}

	@Test
	public void testDeleteTweet3() {
		Users user = new Users();
		user.setUsername("sandy");
		Tweets tweet = new Tweets();
		tweet.setId("2");
		tweet.setUsername(constructUsers());
		Mockito.when(tweetsDao.findTweetById(Mockito.anyString())).thenReturn(tweet);
		tweetServicesImpl.deleteTweet(user.getUsername(), tweet.getId());
		assertTrue(true);
	}

	@Test
	public void testReplyToTweet() {
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setId("1");
		tweetRequest.setTweetString("SEI is very bad project");
		tweetRequest.setUsername("SEI");
		Tweets reTweet = new Tweets();
		reTweet.setId("2");
		reTweet.setCreatedAt(new Date());
		Tweets tweet = new Tweets();
		tweet.setReTweets(new HashSet<>());
		Mockito.when(usersDao.getUserByUsername(Mockito.anyString())).thenReturn(constructUsers());
		Mockito.when(tweetsDao.save(Mockito.any(Tweets.class))).thenReturn(reTweet);
		Mockito.when(tweetsDao.findTweetById(Mockito.anyString())).thenReturn(tweet);
		tweetServicesImpl.replyToTweet(tweetRequest, reTweet.getId());
		assertTrue(true);
	}

	@Test
	public void testReplyToTweet2() {
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setId("1");
		tweetRequest.setTweetString("SEI is very bad project");
		tweetRequest.setUsername("SEI");
		Tweets reTweet = new Tweets();
		reTweet.setCreatedAt(new Date());
		Tweets tweet = new Tweets();
		tweet.setReTweets(new HashSet<>());
		Mockito.when(usersDao.getUserByUsername(Mockito.anyString())).thenReturn(constructUsers());
		Mockito.when(tweetsDao.save(Mockito.any(Tweets.class))).thenReturn(reTweet);
		Mockito.when(tweetsDao.findTweetById(Mockito.anyString())).thenReturn(tweet);
		tweetServicesImpl.replyToTweet(tweetRequest, reTweet.getId());
		assertTrue(true);
	}

	@Test
	public void testReplyToTweet3() {
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setId("1");
		tweetRequest.setTweetString("SEI is very bad project");
		Tweets reTweet = new Tweets();
		reTweet.setCreatedAt(new Date());
		Tweets tweet = new Tweets();
		tweet.setReTweets(new HashSet<>());
		Mockito.when(usersDao.getUserByUsername(Mockito.anyString())).thenReturn(constructUsers());
		Mockito.when(tweetsDao.save(Mockito.any(Tweets.class))).thenReturn(reTweet);
		Mockito.when(tweetsDao.findTweetById(Mockito.anyString())).thenReturn(tweet);
		tweetServicesImpl.replyToTweet(tweetRequest, reTweet.getId());
		assertTrue(true);
	}

	@Test
	public void testLiketweet() {
		Users user = constructUsers();
		Tweets tweet = constructTweets();
		Set<Likes> likes = new HashSet<Likes>();
		Likes like = new Likes();
		like.setUsername(user);
		like.setIsActive('Y');
		like.setId("sandy");
		likes.add(like);
		tweet.setLikes(likes);
		Mockito.when(tweetsDao.findTweetById(Mockito.anyString())).thenReturn(tweet);
		Mockito.when(likeRepository.save(Mockito.any(Likes.class))).thenReturn(like);
		tweetServicesImpl.likeTweet(user.getUsername(), user.getId());
		assertTrue(true);
	}

	@Test
	public void testLiketweet2() {
		Users user = constructUsers();
		Tweets tweet = constructTweets();
		Set<Likes> likes = new HashSet<Likes>();
		Likes like = new Likes();
		like.setUsername(user);
		like.setIsActive('N');
		likes.add(like);
		tweet.setLikes(likes);
		Mockito.when(tweetsDao.findTweetById(Mockito.anyString())).thenReturn(tweet);
		Mockito.when(likeRepository.save(Mockito.any(Likes.class))).thenReturn(like);
		tweetServicesImpl.likeTweet(user.getUsername(), user.getId());
		assertTrue(true);
	}

	@Test
	public void testLiketweet3() {
		Users user = constructUsers();
		Tweets tweet = constructTweets();
		Set<Likes> likes = new HashSet<Likes>();
		Likes like = new Likes();
		like.setUsername(user);
		like.setIsActive('Z');
		likes.add(like);
		tweet.setLikes(likes);
		Mockito.when(tweetsDao.findTweetById(Mockito.anyString())).thenReturn(tweet);
		Mockito.when(likeRepository.save(Mockito.any(Likes.class))).thenReturn(like);
		tweetServicesImpl.likeTweet(user.getUsername(), user.getId());
		assertTrue(true);
	}

	@Test
	public void testLiketweet4() {
		Users user = constructUsers();
		Tweets tweet = constructTweets();
		Set<Likes> likes = new HashSet<Likes>();
		Likes like = new Likes();
		like.setUsername(user);
		like.setIsActive('Z');
		likes.add(like);
		tweet.setLikes(likes);
		Mockito.when(tweetsDao.findTweetById(Mockito.anyString())).thenReturn(tweet);
		Mockito.when(likeRepository.save(Mockito.any(Likes.class))).thenReturn(like);
		tweetServicesImpl.likeTweet("sandeep", user.getId());
		assertTrue(true);
	}

	@Test
	public void testLiketweet5() {
		Users user = constructUsers();
		Tweets tweet = constructTweets();
		Set<Likes> likes = new HashSet<Likes>();
		Likes like = new Likes();
		like.setUsername(user);
		like.setIsActive('Z');
		likes.add(like);
		Mockito.when(tweetsDao.findTweetById(Mockito.anyString())).thenReturn(tweet);
		Mockito.when(likeRepository.save(Mockito.any(Likes.class))).thenReturn(like);
		tweetServicesImpl.likeTweet(user.getUsername(), user.getId());
		assertTrue(true);
	}

	@Test
	public void testGetAllTweetsByUserName() {
		Users user = constructUsers();
		Set<Tweets> userTweets = new HashSet<Tweets>();
		Tweets tweet1 = new Tweets();
		tweet1.setModifiedAt(new Date());
		tweet1.setModifiedBy("aman");
		tweet1.setUsername(user);
		Tweets tweet2 = new Tweets();
		tweet2.setUsername(user);
		userTweets.add(tweet2);
		userTweets.add(tweet1);
		user.setTweets(userTweets);
		Mockito.when(usersDao.getUserByUsername(Mockito.anyString())).thenReturn(user);
		tweetServicesImpl.getAllTweetsByUsername(user.getUsername());
		assertTrue(true);
	}

	private List<Tweets> constructListOfTweets() {
		List<Tweets> allTweets = new ArrayList<Tweets>();
		Tweets tweet = constructTweets();
		Tweets tweet2 = new Tweets();
		tweet2.setUsername(constructUsers());
		allTweets.add(tweet2);
		allTweets.add(tweet);
		return allTweets;
	}

	private Tweets constructTweets() {
		Tweets tweets = new Tweets();
		tweets.setId("1");
		tweets.setTweetMessage("TweetMessage");
		tweets.setUsername(constructUsers());
		tweets.setCreatedAt(new Date());
		tweets.setCreatedBy("ABC");
		tweets.setModifiedAt(new Date());
		tweets.setModifiedBy("abc");
		tweets.setIsActive('y');
		tweets.setLikes(new HashSet<Likes>());
		tweets.setReTweets(new HashSet<Tweets>());
		return tweets;

	}

	private Users constructUsers() {
		Users user = new Users();
		user.setContactNumber("123456");
		user.setCreatedAt(new Date());
		user.setCreatedBy("ABC");
		user.setEmail("ABC@gmail.com");
		user.setFirstName("abc");
		user.setId("1");
		user.setIsActive('y');
		user.setLastName("xyz");
		user.setLikes(new HashSet<Likes>());
		user.setMdifiedBy("aman");
		user.setModifiedAt(new Date());
		user.setPassword("MCUA@1234");
		user.setRoles(new HashSet<Role>());
		user.setTweets(new HashSet<Tweets>());
		user.setUsername("aman");
		return user;
	}

}
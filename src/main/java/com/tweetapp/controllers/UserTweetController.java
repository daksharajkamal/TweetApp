package com.tweetapp.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.configs.metrics.AppConstants;
import com.tweetapp.dto.ErrorResponse;
import com.tweetapp.dto.Reply;
import com.tweetapp.dto.TweetUpdate;
import com.tweetapp.entities.Tweet;
import com.tweetapp.exception.InvalidUsernameException;
import com.tweetapp.exception.TweetDoesNotExistException;
import com.tweetapp.services.TweetService;

import lombok.extern.log4j.Log4j2;

/**
 * @author cogjava729
 */
@Log4j2
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1.0")
public class UserTweetController {

	private final TweetService tweetService;
	private static final String API_V1_POST_NEW_TWEET = "/tweets/{username}/add";
	private static final String API_V1_TWEETS_ALL = "/tweets/all";
	private static final String API_V1_TWEETS_USERNAME = "/tweets/{username}";
	private static final String API_V1_TWEETS_USERNAME_TWEET_ID = "/tweets/{username}/{tweetId}";
	private static final String API_V1_TWEETS_USERNAME_UPDATE = "/tweets/{username}/update";
	private static final String API_V1_TWEETS_USERNAME_REPLY_TWEET_ID = "/tweets/{username}/reply/{tweetId}";
	private static final String API_V1_TWEETS_USERNAME_DISLIKE_TWEET_ID = "/tweets/{username}/dislike/{tweetId}";
	private static final String API_V1_TWEETS_USERNAME_LIKE_TWEET_ID = "/tweets/{username}/like/{tweetId}";
	private static final String API_V1_TWEETS_USERNAME_DELETE = "/tweets/{username}/delete";

	public UserTweetController(@Qualifier("tweet-service") TweetService tweetService) {
		this.tweetService = tweetService;
	}

	// Method to post a new tweet
	@PostMapping(value = API_V1_POST_NEW_TWEET, consumes = { MediaType.ALL_VALUE }, produces = { MediaType.ALL_VALUE })
	public ResponseEntity<?> postNewTweet(@PathVariable("username") String username, @RequestBody Tweet newTweet) {
		return new ResponseEntity<>(tweetService.postNewTweet(username, newTweet), HttpStatus.CREATED);

	}

	// Method to retrieve all tweets
	@GetMapping(value = API_V1_TWEETS_ALL)
	public ResponseEntity<?> getAllTweets() {

		try {
			log.debug("getting all the tweets..");
			return new ResponseEntity<>(tweetService.getAllTweets(), HttpStatus.OK);
		} catch (TweetDoesNotExistException e) {
			return new ResponseEntity<>(new ErrorResponse(AppConstants.APP_ISSUE), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Method to get a user's tweets
	@GetMapping(value = API_V1_TWEETS_USERNAME, consumes = { MediaType.ALL_VALUE }, produces = { MediaType.ALL_VALUE })
	public ResponseEntity<?> getUserTweets(@PathVariable("username") String username,
			@RequestHeader(value = "loggedInUser") String loggedInUser) {
		try {
			log.debug("getting the tweets for user: {}", username);
			return new ResponseEntity<>(tweetService.getUserTweets(username, loggedInUser), HttpStatus.OK);
		} catch (InvalidUsernameException e) {
			return new ResponseEntity<>(new ErrorResponse(AppConstants.INVALID_PARAM), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponse(AppConstants.APP_ISSUE), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = API_V1_TWEETS_USERNAME_TWEET_ID, consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.ALL_VALUE })
	public ResponseEntity<?> getTweetDetails(@PathVariable("username") String username,
			@PathVariable("tweetId") String tweetId) {
		try {
			log.debug("getting the tweet details for user: {}", username);
			return new ResponseEntity<>(tweetService.getTweet(tweetId, username), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Method to update a tweet
	@PutMapping(value = API_V1_TWEETS_USERNAME_UPDATE, consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.ALL_VALUE })
	public ResponseEntity<?> updateTweet(@PathVariable("username") String userId,
			@RequestBody TweetUpdate tweetUpdate) {
		try {
			log.debug("updating the tweet for user: {}", userId);
			return new ResponseEntity<>(
					tweetService.updateTweet(userId, tweetUpdate.getTweetId(), tweetUpdate.getTweetText()),
					HttpStatus.OK);
		} catch (TweetDoesNotExistException e) {
			return new ResponseEntity<>(new ErrorResponse(AppConstants.TWEET_ISSUE), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponse(AppConstants.APP_ISSUE), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Method to delete a tweet
	@DeleteMapping(value = API_V1_TWEETS_USERNAME_DELETE, consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.ALL_VALUE })
	public ResponseEntity<?> deleteTweet(@PathVariable("username") String userId,
			@RequestHeader(value = "tweetId") String tweetId) {
		try {
			log.debug("deleting tweet for user: {}", userId);
			return new ResponseEntity<>(tweetService.deleteTweet(tweetId), HttpStatus.OK);
		} catch (TweetDoesNotExistException e) {
			return new ResponseEntity<>(new ErrorResponse(AppConstants.TWEET_ISSUE), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponse(AppConstants.APP_ISSUE), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Post tweets Like
	@PostMapping(value = API_V1_TWEETS_USERNAME_LIKE_TWEET_ID, consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.ALL_VALUE })
	public ResponseEntity<?> likeATweet(@PathVariable("username") String username,
			@PathVariable(value = "tweetId") String tweetId) {
		try {
			log.debug("liking tweet for user: {}", username);
			return new ResponseEntity<>(tweetService.likeTweet(username, tweetId), HttpStatus.OK);
		} catch (TweetDoesNotExistException e) {
			return new ResponseEntity<>(new ErrorResponse(AppConstants.TWEET_ISSUE), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponse(AppConstants.APP_ISSUE), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// dislike a tweet
	@PostMapping(value = API_V1_TWEETS_USERNAME_DISLIKE_TWEET_ID, consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.ALL_VALUE })
	public ResponseEntity<?> dislikeATweet(@PathVariable("username") String username,
			@PathVariable(value = "tweetId") String tweetId) {
		try {
			log.debug("disliking tweet for user: {}", username);
			return new ResponseEntity<>(tweetService.dislikeTweet(username, tweetId), HttpStatus.OK);
		} catch (TweetDoesNotExistException e) {
			return new ResponseEntity<>(new ErrorResponse(AppConstants.TWEET_ISSUE), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponse(AppConstants.APP_ISSUE), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Post tweet comment
	@PostMapping(value = API_V1_TWEETS_USERNAME_REPLY_TWEET_ID, consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.ALL_VALUE })
	public ResponseEntity<?> replyToTweet(@PathVariable("username") String userId,
			@PathVariable("tweetId") String tweetId, @RequestBody Reply tweetReply) {
		try {
			log.debug("replying to the tweet for user: {}", userId);
			return new ResponseEntity<>(tweetService.replyTweet(userId, tweetId, tweetReply.getComment()),
					HttpStatus.OK);
		} catch (TweetDoesNotExistException e) {
			return new ResponseEntity<>(new ErrorResponse(AppConstants.TWEET_ISSUE), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponse(AppConstants.APP_ISSUE), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

package com.tweetapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tweetapp.configs.metrics.AppConstants;
import com.tweetapp.configs.metrics.AppMetrics;
import com.tweetapp.dto.Comment;
import com.tweetapp.dto.TweetResponse;
import com.tweetapp.entities.Tweet;
import com.tweetapp.exception.InvalidUsernameException;
import com.tweetapp.exception.TweetDoesNotExistException;
import com.tweetapp.repositories.TweetRepository;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.log4j.Log4j2;

/**
 * @author cogjava729
 */
@Log4j2
@Component("tweet-service")
public class TweetService {

	private final TweetRepository tweetRepository;
	private final MeterRegistry meterRegistry;

	public TweetService(@Qualifier("tweet-repository") final TweetRepository tweetRepository,
			MeterRegistry meterRegistry) {
		this.tweetRepository = tweetRepository;
		this.meterRegistry = meterRegistry;
	}

	// Method to return all tweets
	public List<Tweet> getAllTweets() {
		log.debug("Found total : {} tweets", tweetRepository.findAll().size());
		return tweetRepository.findAll();
	}

	// Method to return all tweets of a user
	public List<TweetResponse> getUserTweets(final String username, final String loggedInUser)
			throws InvalidUsernameException {
		// use username as login id
		if (!StringUtils.isBlank(username)) {
			List<Tweet> tweets = tweetRepository.findByUsername(username);

			log.debug("found the total tweets: {} for user: {}", tweets.size(), username);
			return tweets.stream().map(tweet -> {
				final Integer likesCount = tweet.getLikes().size();
				final Boolean likeStatus = tweet.getLikes().contains(loggedInUser);
				final Integer commentsCount = tweet.getComments().size();
				return new TweetResponse(tweet.getTweetId(), username, tweet.getTweetText(), tweet.getFirstName(),
						tweet.getLastName(), tweet.getTweetDate(), likesCount, commentsCount, likeStatus,
						tweet.getComments());
			}).collect(Collectors.toList());
		} else {
			log.error(String.format("Username %s provided is invalid", username));
			throw new InvalidUsernameException("Username/loginId provided is invalid");
		}

	}

	// Method to post a new tweet
	public Tweet postNewTweet(final String username, final Tweet newTweet) {
		newTweet.setTweetId(UUID.randomUUID().toString().replace("-", "").substring(0, 10));
		meterRegistry.counter(AppMetrics.POST).increment();
		log.debug("Posted new tweet for user : {} ", username);
		log.debug(String.format("Incremented prometheus counter for metric: # %s", AppMetrics.POST));
		return tweetRepository.insert(newTweet);
	}

	// method to get tweet data by id
	public TweetResponse getTweet(final String tweetId, final String username) throws TweetDoesNotExistException {
		Optional<Tweet> tweetFounded = tweetRepository.findById(tweetId);
		if (tweetFounded.isPresent()) {
			final Tweet tweet = tweetFounded.get();
			final Integer likesCount = tweet.getLikes().size();
			final Boolean likeStatus = tweet.getLikes().contains(username);
			final Integer commentsCount = tweet.getComments().size();

			log.debug("found the tweet with Id: {} for user: {}", tweetId, username);
			return new TweetResponse(tweet.getTweetId(), tweet.getUsername(), tweet.getTweetText(),
					tweet.getFirstName(), tweet.getLastName(), tweet.getTweetDate(), likesCount, commentsCount,
					likeStatus, tweet.getComments());
		} else {
			log.error(String.format(AppConstants.TWEET_DOES_NOT_EXISTS + "Current User is: %s", username));
			throw new TweetDoesNotExistException(AppConstants.TWEET_DOES_NOT_EXISTS);
		}

	}

	// Method to update an existing tweet
	public Tweet updateTweet(final String userId, final String tweetId, final String updatedTweetText)
			throws TweetDoesNotExistException {

		Optional<Tweet> originalTweetOptional = tweetRepository.findById(tweetId);
		if (originalTweetOptional.isPresent()) {
			Tweet tweet = originalTweetOptional.get();
			tweet.setTweetText(updatedTweetText);
			meterRegistry.counter(AppMetrics.UPDATE).increment();
			log.debug("updated tweet for user : {} ", userId);
			log.debug(String.format("Incremented prometheus counter for metric: # %s", AppMetrics.UPDATE));
			return tweetRepository.save(tweet);
		} else {
			log.error(String.format(AppConstants.TWEET_DOES_NOT_EXISTS + "Current User is: %s", userId));
			throw new TweetDoesNotExistException(AppConstants.TWEET_DOES_NOT_EXISTS);
		}

	}

	// Method to delete a tweet
	public boolean deleteTweet(final String tweetId) throws TweetDoesNotExistException {
		if (tweetRepository.existsById(tweetId) && !StringUtils.isBlank(tweetId)) {
			tweetRepository.deleteById(tweetId);
			meterRegistry.counter(AppMetrics.DELETE).increment();
			log.debug("deleted tweet with tweetId : {} ", tweetId);
			log.debug(String.format("Incremented prometheus counter for metric: # %s", AppMetrics.DELETE));
			return true;
		} else {
			log.error(String.format(AppConstants.TWEET_ISSUE + "Current tweetId is: %s", tweetId));
			throw new TweetDoesNotExistException(AppConstants.TWEET_DOES_NOT_EXISTS);
		}
	}

	// Method to like a tweet
	public Tweet likeTweet(final String username, final String tweetId) throws TweetDoesNotExistException {
		Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
		if (tweetOptional.isPresent()) {
			Tweet tweet = tweetOptional.get();
			tweet.getLikes().add(username);
			meterRegistry.counter(AppMetrics.LIKE).increment();
			log.debug("liked tweet with tweetId : {} by user : {} ", tweetId, username);
			log.debug(String.format("Incremented prometheus counter for metric: # %s", AppMetrics.LIKE));
			return tweetRepository.save(tweet);
		} else {
			log.error(String.format(AppConstants.TWEET_ISSUE + "Current User is: %s", username));
			throw new TweetDoesNotExistException(AppConstants.TWEET_DOES_NOT_EXISTS);
		}
	}

	// Method to unlike a tweet
	public Tweet dislikeTweet(final String username, final String tweetId) throws TweetDoesNotExistException {
		Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
		if (tweetOptional.isPresent()) {
			Tweet tweet = tweetOptional.get();
			tweet.getLikes().remove(username);
			meterRegistry.counter(AppMetrics.DISLIKE).increment();
			log.debug("disliked tweet with tweetId : {} by user : {} ", tweetId, username);
			log.debug(String.format("Incremented prometheus counter for metric: # %s", AppMetrics.DISLIKE));
			return tweetRepository.save(tweet);
		} else {
			log.error(String.format(AppConstants.TWEET_ISSUE + "Current User is: %s", username));
			throw new TweetDoesNotExistException(AppConstants.TWEET_DOES_NOT_EXISTS);
		}
	}

	// Method to comment on a tweet
	public Tweet replyTweet(final String username, final String tweetId, final String tweetReply)
			throws TweetDoesNotExistException {
		Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
		if (tweetOptional.isPresent()) {
			Tweet tweet = tweetOptional.get();
			tweet.getComments().add(new Comment(username, tweetReply));
			log.debug("replied to the tweet whose tweetId is: {} by user: {}", tweetId, username);
			return tweetRepository.save(tweet);
		} else {
			log.error(String.format(AppConstants.TWEET_ISSUE + "Current User is: %s", username));
			throw new TweetDoesNotExistException(AppConstants.TWEET_DOES_NOT_EXISTS);
		}
	}

}

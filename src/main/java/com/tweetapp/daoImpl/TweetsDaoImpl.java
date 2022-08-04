package com.tweetapp.daoImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.tweetapp.dao.TweetsDao;
import com.tweetapp.domain.Likes;
import com.tweetapp.domain.Tweets;
import com.tweetapp.repository.LikeRepository;
import com.tweetapp.repository.TweetsRepository;

@Component
public class TweetsDaoImpl implements TweetsDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private TweetsRepository tweetsRepository;

	@Autowired
	private LikeRepository likeRepository;

	public Tweets save(Tweets tweets) {
		return tweetsRepository.save(tweets);
	}

	@Override
	public Tweets findTweetById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		List<Tweets> tweets = mongoTemplate.find(query, Tweets.class);
		if (!tweets.isEmpty()) {
			return tweets.get(0);
		}
		return null;
	}

	@Override
	public List<Tweets> getAllTweets() {
		return tweetsRepository.findAll();
	}

	@Override
	public void delete(String id) {
		tweetsRepository.deleteById(id);
	}

	@Override
	public Likes likeTweet(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		List<Likes> likes = mongoTemplate.find(query, Likes.class);
		if (!likes.isEmpty()) {
			return likes.get(0);
		}
		return null;
	}

	@Override
	public Likes save(Likes like) {
		return likeRepository.save(like);
	}

}

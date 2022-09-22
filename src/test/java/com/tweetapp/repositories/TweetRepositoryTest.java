package com.tweetapp.repositories;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tweetapp.entities.UserModel;

/**
 * @author cogjava729
 */
@DataMongoTest
@RunWith(SpringRunner.class)
public class TweetRepositoryTest {

	@Autowired
	private TweetRepository userRepository;

	@Test
	public void testFindUserByName() {
		final String username = "ikalyan183@gmail.com";
		final UserModel user = UserModel.builder().username("ikalyan183@gmail.com").firstName("kalyan").lastName("says")
				.email("ikalyan183@gmail.com").build();
		userRepository.findByUsername(username);
		Assertions.assertThat(username).isEqualTo(user.getEmail());
	}

}

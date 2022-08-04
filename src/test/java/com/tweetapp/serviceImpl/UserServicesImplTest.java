package com.tweetapp.serviceImpl;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.tweetapp.dao.UsersDao;
import com.tweetapp.domain.Likes;
import com.tweetapp.domain.Role;
import com.tweetapp.domain.Tweets;
import com.tweetapp.domain.Users;

@SpringBootTest
public class UserServicesImplTest {
	@Mock
	UsersDao userDao;

	@InjectMocks
	UserServicesImpl userServicesImpl;

	@SuppressWarnings("deprecation")
	@Before
	public void initialize() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAllUsersByUsername() {
		Mockito.when(userDao.searchByUsername(Mockito.anyString())).thenReturn(constructListofUsers());
		userServicesImpl.searchByUsername("sandy");
		assertTrue(true);
	}

	@Test
	public void testGetAllUsers() {
		Tweets tweet = new Tweets();
		tweet.setTweetMessage("Sandeep");
		Mockito.when(userDao.getAllUsers()).thenReturn(constructListofUsers());
		userServicesImpl.getAllUsers();
		assertTrue(true);
	}

	private List<Users> constructListofUsers() {
		List<Users> users = new ArrayList<Users>();
		Users user = constructUsers();
		users.add(user);
		return users;
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

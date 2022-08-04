package com.tweetapp.daoImpl;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.tweetapp.dao.UsersDao;
import com.tweetapp.domain.ERole;
import com.tweetapp.domain.Role;
import com.tweetapp.domain.Users;
import com.tweetapp.repository.RolesRepository;
import com.tweetapp.repository.UserRepository;

@Component
public class UserDaoImpl implements UsersDao {

	private static final Logger LOG = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RolesRepository rolesRepository;

	@Override
	public Users getUserByUsername(String username) {
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username));
		List<Users> users = mongoTemplate.find(query, Users.class);
		if (!users.isEmpty()) {
			return users.get(0);
		}
		return null;
	}

	@Override
	public Boolean isUserAdmin(String username) {
		if (!rolesRepository.findByName(ERole.ROLE_ADMIN).isPresent()) {
			Role adminRole = rolesRepository.findByName(ERole.ROLE_ADMIN).get();

			List<Role> result = getUserByUsername(username).getRoles().stream()
					.filter(ur -> ur.getId().equals(adminRole.getId())).collect(Collectors.toList());
			if (!result.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Users> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<Users> searchByUsername(String username) {
		Query query = new Query();
		Pattern pattern = Pattern.compile("^" + username);
		query.addCriteria(Criteria.where("username").regex(pattern));
		LOG.info(":" + query);
		List<Users> users = mongoTemplate.find(query, Users.class);
		return users;
	}

	@Override
	public Users save(Users users) {
		return userRepository.save(users);
	}
}

package com.tweetapp.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.dto.AuthenticationResponse;
import com.tweetapp.dto.NewPassword;
import com.tweetapp.services.UserOperationsService;

import lombok.extern.log4j.Log4j2;

/**
 * @author cogjava729
 */
@Log4j2
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1.0")
public class UserController {

	private final UserOperationsService userModelService;

	private static final String API_V1_UPDATE_SECRET = "/tweets/{username}/forgot";
	private static final String API_V1_GET_ALL_USERS = "/tweets/users/all";
	private static final String API_V1_SEARCH_BY_USERNAME = "/tweets/user/search/{username}";

	public UserController(@Qualifier("user-model-service") UserOperationsService userModelService) {
		this.userModelService = userModelService;
	}

	// method to update user password
	@PutMapping(value = API_V1_UPDATE_SECRET, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE,
			MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE,
					MediaType.ALL_VALUE })
	public ResponseEntity<?> changePassword(@PathVariable("username") String username,
			@RequestBody NewPassword newPassword) {
		try {
			log.debug("changing password for user: {}", username);
			return new ResponseEntity<>(
					userModelService.changePassword(username, newPassword.getNewPassword(), newPassword.getContact()),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new AuthenticationResponse("Unable to change password"),
					HttpStatus.BAD_REQUEST);
		}
	}

	// Method to retrieve all users list
	@GetMapping(value = API_V1_GET_ALL_USERS, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE,
			MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE,
					MediaType.ALL_VALUE })
	public ResponseEntity<?> getAllUsers() {
		log.debug("fetching total users");
		return new ResponseEntity<>(userModelService.getAllUsers(), HttpStatus.OK);

	}

	// method to search for like users by username
	@GetMapping(value = API_V1_SEARCH_BY_USERNAME, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.TEXT_PLAIN_VALUE, MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.TEXT_PLAIN_VALUE, MediaType.ALL_VALUE })
	public ResponseEntity<?> searchForUsers(@PathVariable String username) {
		log.debug("fetching user by userName: {}", username);
		return new ResponseEntity<>(userModelService.getUsersByUsername(username), HttpStatus.OK);
	}
}

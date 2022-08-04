package com.tweetapp.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.dao.UsersDao;
import com.tweetapp.domain.ERole;
import com.tweetapp.domain.Role;
import com.tweetapp.domain.Users;
import com.tweetapp.payload.request.LoginRequest;
import com.tweetapp.payload.request.SignupRequest;
import com.tweetapp.payload.response.JwtResponse;
import com.tweetapp.payload.response.MessageResponse;
import com.tweetapp.repository.RolesRepository;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.security.jwt.JwtUtils;
import com.tweetapp.security.services.UserDetailsImpl;
import com.tweetapp.util.TweetAppConstants;
import com.tweetapp.util.TweetAppUtil;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RolesRepository roleRepository;

	@Autowired
	UsersDao userDao;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@GetMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return ResponseEntity.ok(
				new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		// Create new user's account
		Users user = new Users(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getEmail(),
				signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()),
				signUpRequest.getContactNumber());
		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();
		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@GetMapping("/{username}/forgot")
	public ResponseEntity<?> forgotPassword(@PathVariable(required = true) String username) {
		Users user = userDao.getUserByUsername(username);
		if (user == null) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: User does not exists!"));
		} else {
			String randomPassword = "temp_"
					+ TweetAppUtil.generateRandomString(TweetAppConstants.RANDOM_PASSWORD_LEFT_LIMIT,
							TweetAppConstants.RANDOM_PASSWORD_RIGHT_LIMIT, TweetAppConstants.RANDOM_PASSWORD_LENGTH);
			user.setPassword(encoder.encode(randomPassword));
			userRepository.save(user);
			return ResponseEntity.ok(new MessageResponse(randomPassword));
		}
	}

}

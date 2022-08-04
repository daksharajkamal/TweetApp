package com.tweetapp.dao;

import java.util.List;

import com.tweetapp.domain.Users;

public interface UsersDao {

	public Users save(Users users);

	public Users getUserByUsername(String username);

	public Boolean isUserAdmin(String username);

	public List<Users> getAllUsers();

	public List<Users> searchByUsername(String username);

}

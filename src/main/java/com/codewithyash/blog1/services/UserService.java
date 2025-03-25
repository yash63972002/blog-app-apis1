package com.codewithyash.blog1.services;

import java.util.List;

import com.codewithyash.blog1.entities.User;
import com.codewithyash.blog1.payloads.UserDto;

public interface UserService {

	UserDto registerNewUser(UserDto userDto);
	
	UserDto createUser(UserDto user);
	
	UserDto updateUser(UserDto user,Integer userId );
	
	UserDto getUserById(Integer userId);
	
	List<UserDto> getAllUsers();
	
	void deleteUser(Integer userId);
}

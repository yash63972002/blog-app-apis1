package com.codewithyash.blog1.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codewithyash.blog1.exceptions.*;
import com.codewithyash.blog1.config.AppConstants;
import com.codewithyash.blog1.entities.Role;
import com.codewithyash.blog1.entities.User;
import com.codewithyash.blog1.payloads.UserDto;
import com.codewithyash.blog1.repositories.RoleRepo;
import com.codewithyash.blog1.repositories.UserRepo;
import com.codewithyash.blog1.services.UserService;

@Service
public class UserServiceimpl implements UserService {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;

	@Override
	public UserDto createUser(UserDto userDto) {
		
		User user = this.dtoToUser(userDto);
		User savedUser = this.userRepo.save(user);
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("User", "id", userId));
		
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setAbout(userDto.getAbout());
		user.setPassword(userDto.getPassword());
		
		User updatedUser = this.userRepo.save(user);
		UserDto userDto1 = this.userToDto(updatedUser);
			
		return userDto1;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("User", "id", userId));
	
		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = this.userRepo.findAll();
		
		List<UserDto> userDtos = users.stream().map(user->this.userToDto(user)).collect(Collectors.toList());
		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user =  this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id", userId));
		this.userRepo.delete(user);
	}
	
	private User dtoToUser(UserDto userDto){
		User user = this.modelMapper.map(userDto, User.class);
//		user.setId(userDto.getId());
//		user.setName(userDto.getName());
//		user.setEmail(userDto.getEmail());
//		user.setAbout(userDto.getAbout());
//		user.setPassword(userDto.getPassword());
		return user;
	}
	
	public UserDto userToDto(User user){
		UserDto userDto = this.modelMapper.map(user, UserDto.class );
		
		return userDto;
	}

	@Override
	public UserDto registerNewUser(UserDto userDto) {
		
		if(userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
			throw new IllegalArgumentException("Password cannot be null or empty");
		}

		User user = new User();
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		
		user.setAbout(userDto.getAbout());
		
		Role role = roleRepo.findById(AppConstants.NORMAL_USER).orElseThrow(() -> new ResourceNotFoundException("Role", "ID", AppConstants.NORMAL_USER));
		user.getRoles().add(role);
		
		
		User savedUser = userRepo.save(user);
		
		UserDto savedUserDto = this.userToDto(savedUser);
		
//		savedUserDto.setId(savedUser.getId());
//        savedUserDto.setName(savedUser.getName());
//        savedUserDto.setEmail(savedUser.getEmail());
//        savedUserDto.setAbout(savedUser.getAbout());

        return savedUserDto;	
        
		
	}

	

}

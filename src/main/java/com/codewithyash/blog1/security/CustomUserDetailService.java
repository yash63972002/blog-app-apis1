package com.codewithyash.blog1.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codewithyash.blog1.entities.User;
import com.codewithyash.blog1.exceptions.ResourceNotFoundException;
import com.codewithyash.blog1.repositories.UserRepo;


@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// loading user from database by username
		User user = this.userRepo.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User", "email :"+username, 0));
		
		
		return user;
	}

}

package com.codewithyash.blog1.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithyash.blog1.payloads.JwtAuthRequest;
import com.codewithyash.blog1.payloads.JwtAuthResponse;
import com.codewithyash.blog1.payloads.UserDto;
import com.codewithyash.blog1.security.JwtTokenHelper;
import com.codewithyash.blog1.services.UserService;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/login")
	public ResponseEntity<?> createToken(@RequestBody JwtAuthRequest request) throws Exception {
		
		try {
			
		
		this.authenticate(request.getUsername(),request.getPassword());	
		
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
		
		String token = this.jwtTokenHelper.generateToken(userDetails);
		
		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		return ResponseEntity.ok(response);
		
		} catch (BadCredentialsException e) {
			
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", "Invalid username or Password !! ");
			errorResponse.put("success", false);
			
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
			
		}
			
	}

	private void authenticate(String username, String password) throws Exception {
		
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(username, password);
		this.authenticationManager.authenticate(authenticationToken);
		
	}
	
	//register new user api
	
//	@PostMapping("/register") 
//	public ResponseEntity<?> registerUser(@RequestBody UserDto userDto ) {
//		try {
//			
//			if(userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
//				throw new IllegalArgumentException("Password can be null or Empty");
//			}
//			userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
//		
//			UserDto registeredUser = userService.registerNewUser(userDto);
//		
//			return new ResponseEntity<>(registeredUser,HttpStatus.CREATED);
//		} catch (Exception e) {
//			Map<String, Object> errorResponse = new HashMap<>();
//			errorResponse.put("Message", "User registration failed: "+e.getMessage());
//			
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//		}
	
//	}
	
	//register new user api
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody UserDto userDto ){
		UserDto registeredUser = this.userService.registerNewUser(userDto);
		
		return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);
	}
	
	

}

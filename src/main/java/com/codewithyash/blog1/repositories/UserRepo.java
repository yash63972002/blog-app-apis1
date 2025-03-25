package com.codewithyash.blog1.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithyash.blog1.entities.User;


public interface UserRepo extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);
	
}

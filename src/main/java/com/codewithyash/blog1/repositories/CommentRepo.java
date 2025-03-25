package com.codewithyash.blog1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithyash.blog1.entities.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer> {
	

}

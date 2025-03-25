package com.codewithyash.blog1.services;

import com.codewithyash.blog1.payloads.CommentDto;

public interface CommentService {
	
	CommentDto createComment(CommentDto commentDto,Integer postId);
	
	void deleteComment(Integer commentId);
	
}

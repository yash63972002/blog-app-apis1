package com.codewithyash.blog1.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codewithyash.blog1.entities.Comment;
import com.codewithyash.blog1.entities.Post;
import com.codewithyash.blog1.exceptions.ResourceNotFoundException;
import com.codewithyash.blog1.payloads.CommentDto;
import com.codewithyash.blog1.repositories.CommentRepo;
import com.codewithyash.blog1.repositories.PostRepo;
import com.codewithyash.blog1.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "post id", postId));
		
		Comment comment = this.modelMapper.map(commentDto, Comment.class );
		
		comment.setPost(post);
		
		Comment savedComment = this.commentRepo.save(comment);
		
		return this.modelMapper.map(savedComment, CommentDto.class);
	}
	

	@Override
	public void deleteComment(Integer commentId) {
		Comment com = this.commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment", "CommentId", commentId));

		this.commentRepo.delete(com);
	}

}

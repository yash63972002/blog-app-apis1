package com.codewithyash.blog1.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codewithyash.blog1.entities.Post;
import com.codewithyash.blog1.payloads.PostDto;
import com.codewithyash.blog1.payloads.PostResponse;


@Service
public interface PostService {
	
	
	//create
	
	PostDto createPost(PostDto postDto, Integer userId,Integer categoryId);
	
	
	
	//update
	
	PostDto updatePost(PostDto postDto, Integer postId);
	
	
	
	//delete
	
	void deletePost(Integer postId);
	
	
	
	//get all posts
	
	PostResponse getAllPost(Integer pageNumber,Integer pageSize,String sortBy,String sortDir);
	
	
	
	//get single post
	
	PostDto getPostById(Integer postId);
	
	
	
	//get all the posts by category
	
	List<PostDto> getPostsByCategory(Integer categoryId);
	
	
	
	//get all posts by user
	
	List<PostDto> getPostsByUser(Integer userId);
	
	
	
	//search posts
	List<PostDto> searchPosts(String keyword);

}

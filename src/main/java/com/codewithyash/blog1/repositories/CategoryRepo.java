package com.codewithyash.blog1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithyash.blog1.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

	

	

}

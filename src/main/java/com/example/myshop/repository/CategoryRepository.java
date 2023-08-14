package com.example.myshop.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.myshop.entity.Category;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
	
}

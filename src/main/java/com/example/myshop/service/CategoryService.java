package com.example.myshop.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myshop.entity.Category;
import com.example.myshop.repository.CategoryRepository;

@Service
@Transactional
public class CategoryService {

	@Autowired
	CategoryRepository catRepository;
	
	public List<Category> listAll(){
		return (List<Category>)catRepository.findAll();
	}
	
	public List<Category> listCategoriesUsedInForm(){
		Iterable<Category> categoriesInDB = catRepository.findAll();
		List<Category> categoriesInForm = new ArrayList<>();
		
		categoriesInDB.forEach(category -> {
			if(category.getParent() == null) {
				categoriesInForm.add(new Category(category.getName()));
				
				category.getChildren().forEach(subCategory -> {
					categoriesInForm.add(new Category("--" + subCategory.getName()));
					
					listChildren(categoriesInForm, subCategory, 1);
				});
			}
		});
		
		return categoriesInForm;
	}
	
	private void listChildren(List<Category> categoriesInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		
		parent.getChildren().forEach(subCategory -> {
			String name = "";
			for(int i = 0; i< newSubLevel; i++) {
				name += "--";
			}
			categoriesInForm.add(new Category(name + subCategory.getName()));
			
			listChildren(categoriesInForm, subCategory, newSubLevel);
		});
	}
}

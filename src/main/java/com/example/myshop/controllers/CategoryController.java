package com.example.myshop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.myshop.entity.Category;
import com.example.myshop.service.CategoryService;

@Controller
public class CategoryController {

	@Autowired
	CategoryService catService;
	
	@GetMapping("/categories")
	public String listOfCategories(Model model) {
		
		List<Category> categories = catService.listAll();
		model.addAttribute("listCategories", categories);
		
		return "categories/categories";
	}
	
	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		List<Category> listCategories = catService.listCategoriesUsedInForm();
		
		model.addAttribute("category", new Category());
		model.addAttribute("pageTitle", "Create New Category");
		model.addAttribute("listCategories", listCategories);
		
		return "categories/category_form";
	}
}

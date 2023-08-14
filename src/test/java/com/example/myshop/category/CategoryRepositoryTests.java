package com.example.myshop.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.myshop.entity.Category;
import com.example.myshop.repository.CategoryRepository;
import static org.assertj.core.api.Assertions.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE) //this is annotation to test with real db, not in-memory
@Rollback(false)
public class CategoryRepositoryTests {

	@Autowired
	CategoryRepository repo;
	
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electronics");
		
		Category savedCategory = repo.save(category);
		
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(7);
		Category memory = new Category("Iphone", parent);
		
		repo.save(memory);
	}
	
	@Test
	public void testGetCategory() {
		Category category = repo.findById(1).get();
		
		System.out.println(category.getName());
		
		Set<Category> children = category.getChildren();
		children.forEach(x -> System.out.println(x.getName()));
		
		assertThat(children.size() > 0);
	}
	
	@Test
	public void testPrintHierarchicalCategories() {
		Iterable<Category> categories = repo.findAll();
		
		categories.forEach(category -> {
			if(category.getParent() == null) {
				System.out.println(category.getName());
				
				category.getChildren().forEach(subCategory -> {
					System.out.println("--" + subCategory.getName());
					
					printChildren(subCategory, 1);
				});
			}
		});
	}
	
	private void printChildren(Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		
		parent.getChildren().forEach(subCategory -> {
			for(int i = 0; i< newSubLevel; i++) {
				System.out.print("--");
			}
			System.out.println(subCategory.getName());
			
			printChildren(subCategory, newSubLevel);
		});
	}
}

package com.ncp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ncp.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	
	@Query("Select DISTINCT c.category From Category as c")
	public List<String> findAllCategoryNames();
	
}

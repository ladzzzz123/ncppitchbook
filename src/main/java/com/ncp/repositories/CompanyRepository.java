package com.ncp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ncp.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{
	
	public List<Company> findByName(String name);
	
	@Query("Select DISTINCT c.name From Company as c")
	public List<String> findAllCompanyNames();
	
}

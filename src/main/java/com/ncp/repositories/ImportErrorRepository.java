package com.ncp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ncp.entity.ImportError;

public interface ImportErrorRepository extends JpaRepository<ImportError, Long>{
	
}

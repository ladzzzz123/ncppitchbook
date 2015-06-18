package com.ncp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ncp.entity.CompanyRecordType;

public interface CompanyRecordTypeRepository extends JpaRepository<CompanyRecordType, Long>{
	
	@Query("Select DISTINCT c.companyRecordType From CompanyRecordType as c Order By c.companyRecordType ASC")
	public List<String> findAllCompanyRecordTypesOrderByType();
	
}

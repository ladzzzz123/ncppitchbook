package com.ncp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ncp.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long>{
	
}

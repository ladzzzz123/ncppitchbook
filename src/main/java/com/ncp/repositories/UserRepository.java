package com.ncp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ncp.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
}

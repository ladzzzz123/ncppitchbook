package com.ncp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ncp.entity.PitchbookRecord;

public interface PitchbookRecordRepository extends JpaRepository<PitchbookRecord, Long>{
	
}

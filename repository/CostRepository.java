package com.equabli.datascrubbing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.Cost;

@Repository
public interface CostRepository extends JpaRepository<Cost, Long>{

	@Query(value="select cost from Cost cost "
    		+ "where recordStatusId = :rawRecordStatusId ")
	public Page<Cost> getCostToProcess(Integer rawRecordStatusId, Pageable pageable);
}
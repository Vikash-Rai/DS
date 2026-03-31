package com.equabli.datascrubbing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.CreditScore;

@Repository
public interface CreditScoreRepository extends JpaRepository<CreditScore, Long>{

	/**
	 * 
	 * @param rawRecordStatusId
	 * @param suspectedRecordStatusId
	 * @param jobName
	 * @return
	 */
	@Query(value="select creditScore from CreditScore creditScore "
    		+ "where recordStatusId = :rawRecordStatusId ")
	public Page<CreditScore> getCreditScoreToProcess(Integer rawRecordStatusId, Pageable pageable);
}
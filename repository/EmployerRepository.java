package com.equabli.datascrubbing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.Employer;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Integer> {

	/**
     * Finds employer by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of employer as per search criteria.
     *          If no employer is found, this method returns an empty list.
     */

	@Query(value="select emp from Employer emp "
    		+ "where recordStatusId = :rawRecordStatusId ")
    public Page<Employer> getEmployerToProcess(Integer rawRecordStatusId, Pageable pageable);
}
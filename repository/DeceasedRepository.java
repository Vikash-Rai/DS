package com.equabli.datascrubbing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.Deceased;

@Repository
public interface DeceasedRepository extends JpaRepository<Deceased, Long> {

	/**
     * Finds Deceased by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * 
     *          
     */

	@Query(value="select g from Deceased g "
    		+ "where recordStatusId = :rawRecordStatusId ")
    public Page<Deceased> getDeceasedToProcess(Integer rawRecordStatusId, Pageable pageable);
}

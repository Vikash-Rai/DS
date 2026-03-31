package com.equabli.datascrubbing.repository;

import com.equabli.datascrubbing.entity.Garnishment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GarnishmentRepository extends JpaRepository<Garnishment, Long> {

	/**
     * Finds Garnishment by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of legal placement as per search criteria.
     *          If no legal placement is found, this method returns an empty list.
     */

	@Query(value="select g from Garnishment g "
    		+ "where recordStatusId = :rawRecordStatusId ")
    public Page<Garnishment> getGarnishmentToProcess(Integer rawRecordStatusId, Pageable pageable);
}
package com.equabli.datascrubbing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.PlacedAccountLog;

@Repository
public interface PlacedAccountLogRepository extends JpaRepository<PlacedAccountLog, Long> {

	/**
     * Finds placedAccountLog by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of placedAccountLog as per search criteria.
     *          If no placedAccountLog is found, this method returns an empty list.
     */

	@Query(value="select pal from PlacedAccountLog pal "
    		+ "where recordStatusId = :rawRecordStatusId ")
    public Page<PlacedAccountLog> getPlacedAccountLogToProcess(Integer rawRecordStatusId, Pageable pageable);
}
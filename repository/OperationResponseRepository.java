package com.equabli.datascrubbing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.OperationResponse;

@Repository
public interface OperationResponseRepository extends JpaRepository<OperationResponse, Long> {

	/**
     * Finds operation response by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of operation response as per search criteria.
     *          If no operation response is found, this method returns an empty list.
     */

	@Query(value="select op from OperationResponse op "
    		+ "where recordStatusId = :rawRecordStatusId ")
    public Page<OperationResponse> getOperationResponseToProcess(Integer rawRecordStatusId, Pageable pageable);
}
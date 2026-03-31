package com.equabli.datascrubbing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.OperationRequest;

@Repository
public interface OperationRequestRepository extends JpaRepository<OperationRequest, Long> {

	/**
     * Finds operation request by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of operation request as per search criteria.
     *          If no operation request is found, this method returns an empty list.
     */

	@Query(value="select op from OperationRequest op "
    		+ "where recordStatusId = :rawRecordStatusId ")
    public Page<OperationRequest> getOperationRequestToProcess(Integer rawRecordStatusId, Pageable pageable);
}
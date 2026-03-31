package com.equabli.datascrubbing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.DialConsentExclusion;

@Repository
public interface DialConsentExclusionRepository extends JpaRepository<DialConsentExclusion, Long> {

	/**
     * Finds dialConsentExclusion by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of dialConsentExclusion as per search criteria.
     *          If no dialConsentExclusion is found, this method returns an empty list.
     */

	@Query(value="select dce from DialConsentExclusion dce "
    		+ "where recordStatusId = :rawRecordStatusId ")
    public Page<DialConsentExclusion> getDialConsentExclusionToProcess(Integer rawRecordStatusId, Pageable pageable);
}
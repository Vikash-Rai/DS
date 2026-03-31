package com.equabli.datascrubbing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.EmailConsentExclusion;

@Repository
public interface EmailConsentExclusionRepository extends JpaRepository<EmailConsentExclusion, Long> {

	/**
     * Finds emailConsentExclusion by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of emailConsentExclusion as per search criteria.
     *          If no emailConsentExclusion is found, this method returns an empty list.
     */

	@Query(value="select ece from EmailConsentExclusion ece "
    		+ "where recordStatusId = :rawRecordStatusId ")
    public Page<EmailConsentExclusion> getEmailConsentExclusionToProcess(Integer rawRecordStatusId, Pageable pageable);
}
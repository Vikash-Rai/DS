package com.equabli.datascrubbing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.ServicingDetail;

@Repository
public interface ServicingDetailRepository extends JpaRepository<ServicingDetail, Long> {

	/**
     * Finds servicing detail by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of servicing detail as per search criteria.
     *          If no servicing detail is found, this method returns an empty list.
     */

	@Query(value="select sd from ServicingDetail sd "
    		+ "where recordStatusId = :rawRecordStatusId ")
    public Page<ServicingDetail> getServicingDetailToProcess(Integer rawRecordStatusId, Pageable pageable);
}
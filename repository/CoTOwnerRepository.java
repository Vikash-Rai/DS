package com.equabli.datascrubbing.repository;

import com.equabli.datascrubbing.entity.CoTOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CoTOwnerRepository extends JpaRepository<CoTOwner, Long> {

    /**
     * Finds CoTOwners by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     *
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of CoTOwner as per search criteria.
     * If no CoTOwner is found, this method returns an empty list.
     */

    @Query(value = "select cotOwner from CoTOwner cotOwner where cotOwner.recordStatusId = :rawRecordStatusId ")
    Page<CoTOwner> getCoTOwnerProcess(Integer rawRecordStatusId, Pageable pageable);
}
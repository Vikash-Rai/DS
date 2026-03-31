package com.equabli.datascrubbing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.PaymentSchedule;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long> {

	/**
     * Finds payment schedule by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of payment schedule as per search criteria.
     *          If no payment schedule is found, this method returns an empty list.
     */

	@Query(value="select ps from PaymentSchedule ps "
    		+ "where recordStatusId = :rawRecordStatusId ")
    public Page<PaymentSchedule> getPaymentScheduleToProcess(Integer rawRecordStatusId, Pageable pageable);

	@Query(value="select sum(amtPayment) from PaymentSchedule ps "
			+ " where (recordStatusId = :rawRecordStatusId) and "
    		+ " (clientId = :clientId) and (clientAccountNumber = :clientAccountNumber) and (partnerPlanNumber = :partnerPlanNumber) ")
    public Double getSumOfAllPaymentSchedule(Integer rawRecordStatusId, Integer clientId, String clientAccountNumber, Long partnerPlanNumber);

	@Query(value="select count(paymentScheduleId) from PaymentSchedule ps "
			+ " where (recordStatusId = :rawRecordStatusId) and "
    		+ " (clientId = :clientId) and (clientAccountNumber = :clientAccountNumber) and (partnerPlanNumber = :partnerPlanNumber) ")
    public Integer getCountOfAllPaymentSchedule(Integer rawRecordStatusId, Integer clientId, String clientAccountNumber, Long partnerPlanNumber);
}
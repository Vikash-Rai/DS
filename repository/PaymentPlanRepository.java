package com.equabli.datascrubbing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.PaymentPlan;

@Repository
public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {

	/**
     * Finds payment plan by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of payment plan as per search criteria.
     *          If no payment plan is found, this method returns an empty list.
     */

	@Query(value="select pp from PaymentPlan pp "
    		+ "where (recordStatusId = :rawRecordStatusId) or (recordStatusId = :suspectedRecordStatusId and dtmUtcUpdate >= coalesce((select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName and dtmUtcAction not in (select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName)), "
    		+ "(select min(dtmUtcUpdate) from PaymentPlan)))")
    public List<PaymentPlan> getPaymentPlanToProcess(Integer rawRecordStatusId, Integer suspectedRecordStatusId, String jobName);

	@Query(value="select pp from PaymentPlan pp "
    		+ "where (recordStatusId = :enabledRecordStatusId and dtmUtcUpdate >= coalesce((select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName and dtmUtcAction not in (select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName)), "
    		+ "(select min(dtmUtcUpdate) from PaymentPlan)))")
    public List<PaymentPlan> getPaymentPlanToProcessAfterUpdate(Integer enabledRecordStatusId, String jobName);
	
	@Query(value="select pp from PaymentSchedule ps inner join PaymentPlan pp on pp.clientId = ps.clientId and pp.clientAccountNumber = ps.clientAccountNumber and pp.partnerPlanNumber = ps.partnerPlanNumber "
	+ " where (pp.recordStatusId = :enabledRecordStatusId) and (ps.recordStatusId = :enabledRecordStatusId) and " + " (ps.dtmUtcUpdate >= coalesce((select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName and dtmUtcAction not in (select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName)), " 
			+ " (select min(dtmUtcUpdate) from PaymentPlan))) ")
	public List<PaymentPlan> getPaymentPlanToProcessafterPayScheduleUpdate(Integer enabledRecordStatusId, String jobName );
	
	@Query(value="select pp from PaymentPlan pp where pp.clientId = :clientId and pp.clientAccountNumber = :clientAccountNumber and pp.partnerPlanNumber = :partnerPlanNumber "
			+ " and pp.recordStatusId = :enabledRecordStatusId" )
			public PaymentPlan getPaymentPlanForGarnishment(Integer enabledRecordStatusId, Integer clientId, String clientAccountNumber , Long partnerPlanNumber) ;
	
	
}
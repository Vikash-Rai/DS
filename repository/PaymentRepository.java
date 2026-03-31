package com.equabli.datascrubbing.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.equabli.datascrubbing.entity.Account;
import com.equabli.datascrubbing.entity.Client;
import com.equabli.datascrubbing.entity.Payment;
import com.equabli.datascrubbing.entity.PaymentPlan;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;



@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	/**
     * Finds payment by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of payment as per search criteria.
     *          If no payment is found, this method returns an empty list.
     */

	@Query(value="select pay from Payment pay "
    		+ "where recordStatusId = :rawRecordStatusId and paymentType = :paymentType ")
    public Page<Payment> getPaymentToProcess(Integer rawRecordStatusId, String paymentType, Pageable pageable);

	@Modifying
	@Transactional
	@Query("update Payment set reversalDate = current_timestamp where paymentId = :paymentId and reversalDate is null ")
	int updatePaymentReversalDate(Long paymentId);

	@Query(value="select new Payment(pay.errwarShortName, pay.errwarDescription, acc.accountId, payment.paymentId, pay.clientAccountNumber, cl.fullName, p.fullName ,payment.paymentPlanId, payment.paymentSerial, "
			+ "payment.amtPayment, payment.paymentMethod, payment.paymentDate, payment.paymentStatus, payment.paymentType, payment.dtPaymentPosting,payment.amtBalance, "
			+ "payment.paymentBrokenReason,payment.approvalCode, payment.partnerBatchNumber, payment.postingNumber, payment.gatewayVendor, payment.countBalancePayment, "
			+ "payment.amtPrincipal, payment.amtInterest, payment.amtLatefee,payment.amtOtherfee,payment.amtCourtcost, payment.amtAttorneyfee, payment.clientPaymentId , "
			+ "payment.reversalParentId, payment.paymentSettlementType, payment.isCommissionable,payment.paymentSource, pay.clientId) "
			+ "from ErrWarMessagePayment pay "
			+ "join Payment payment on pay.paymentId = payment.paymentId "
			+ "left join Account acc on acc.clientId = pay.clientId and acc.clientAccountNumber = pay.clientAccountNumber "
			+ "left join Client cl on pay.clientId = cl.clientId "
			+ "left join Partner p on payment.partnerId  = p.partnerId "
			+ "where pay.errwarType = :type and pay.errwarShortName like %:code and (:clientId is null or pay.clientId = :clientId) "
			+ "and (:partnerId is null or payment.partnerId = :partnerId) and payment.recordStatusId = :suspectedRecordStatusId "
			+ "and (:clientJobScheduleId is null or pay.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(pay.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(pay.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(pay.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(pay.dtmUtcCreate AS date), current_date) ")
	List<Payment> getPaymentDetailsForSuspectedInv(@Param("type") String type,@Param("code") String code,@Param("clientId") Integer clientId,@Param("clientJobScheduleId") Integer clientJobScheduleId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo,@Param("partnerId") Integer partnerId,@Param("suspectedRecordStatusId") Integer suspectedRecordStatusId);

	@Query(value="select pay from Payment pay "
			+ "where paymentId=:paymentId")
	Payment findPaymentByPaymentId(Long paymentId);

	@Query(value = "Select pp.paymentPlanId from PaymentPlan pp join RecordStatus rs on pp.recordStatusId = rs.recordStatusId  and rs.shortName = ('"+ConfRecordStatus.ENABLED+"') " +
			" where pp.partnerId = :partnerId and pp.partnerPlanNumber = :partnerPlanNumber")
	PaymentPlan findPaymentPlanByPartnerIdAndPartnerPlan(Integer partnerId, Long partnerPlanNumber);

	@Query(value = "select new Client(cl.clientId, cl.shortName) from Client cl join RecordStatus rs on cl.recordStatusId = rs.recordStatusId and rs.shortName = '"+ ConfRecordStatus.ENABLED + "' " +
			" where cl.clientId = :clientId")
	Client findClientById(Integer clientId);

	@Query("SELECT new Payment(pa.paymentId, pa.amtOtherfee, pa.amtInterest, pa.amtPrincipal, pa.amtLatefee, pa.amtCourtcost, pa.amtAttorneyfee , pa.pctPartnerCommission) FROM Payment pa JOIN RecordStatus rs ON pa.recordStatusId = rs.recordStatusId AND rs.shortName = ('"+ConfRecordStatus.ENABLED+"') " +
			" WHERE (pa.paymentSource = '"+ CommonConstants.RECORD_SOURCE_CLIENT+"' AND pa.clientId = :clientId AND pa.clientPaymentId = :reversalParentId) " +
			" OR (pa.paymentSource = '"+ CommonConstants.RECORD_SOURCE_PARTNER+"' AND pa.partnerId = :partnerId AND pa.partnerSystemId = :reversalParentId) " +
			" OR (pa.paymentSource = '"+ CommonConstants.RECORD_SOURCE_PARTNER+"' AND pa.partnerId = :partnerId AND pa.postingNumber = :reversalParentId)")
	Payment getDataParentPaymentData(Integer clientId, String reversalParentId, Integer partnerId);

	@Query("SELECT COUNT(pa.paymentId) FROM Payment pa " +
			"WHERE (pa.paymentSource = '"+CommonConstants.RECORD_SOURCE_CLIENT+"' AND pa.clientId = :clientId AND pa.reversalParentId = :reversalParentId) " +
			"OR (pa.paymentSource = '"+CommonConstants.RECORD_SOURCE_PARTNER+"' AND pa.partnerId = :partnerId AND pa.reversalParentId = :reversalParentId) " +
			"AND pa.paymentType = '"+CommonConstants.PAYMENT_TYPE_NSF+"'")
	Integer isNSFDeDup(Integer clientId, String reversalParentId, Integer partnerId);

	@Query(value = "select acc.partnerId from Account acc join RecordStatus rs on acc.recordStatusId = rs.recordStatusId and rs.shortName = 'Enabled' " +
			" where acc.clientId = :clientId and acc.clientAccountNumber = :clientAccountNumber")
	Integer getPartnerIds(Integer clientId, String clientAccountNumber);

	@Query(value = "select new Account(acc.accountId, acc.amtOtherfeeCurrentbalance, acc.amtInterestCurrentbalance, acc.amtPrincipalCurrentbalance, acc.amtCurrentbalance, acc.chargeOffDate, "
			+ "acc.amtPreChargeOffPrinciple, acc.amtPreChargeOffInterest, acc.amtPreChargeOffFees, acc.amtLatefeeCurrentbalance, acc.amtCourtcostCurrentbalance, acc.amtAttorneyfeeCurrentbalance, "
			+ "acc.amtPreChargeOffBalance, acc.amtPostChargeOffInterest, acc.amtPostChargeOffFee, acc.amtPostChargeOffPayment, acc.amtPostChargeOffCredit)  " +
			" from Account acc join RecordStatus rs on acc.recordStatusId = rs.recordStatusId and rs.shortName = 'Enabled' " +
			" where acc.clientId = :clientId and acc.clientAccountNumber = :clientAccountNumber")
	Account getDataAmtAccount(Integer clientId, String clientAccountNumber);

	@Query(value = "select cs.dtFrom from COTService cs join RecordStatus rs on cs.recordStatusId = rs.recordStatusId and rs.shortName = 'Enabled' " +
			" where cs.accountId = (select acc.accountId from Account acc where acc.clientId = :clientId and acc.clientAccountNumber = :clientAccountNumber) and cs.partnerId = :partnerId " +
			" order by cs.dtFrom desc ")
	List<LocalDate> getCotDtFrom(Integer clientId, String clientAccountNumber, Integer partnerId);

	@Query(value = "select cs.dtTill from COTService cs join RecordStatus rs on cs.recordStatusId = rs.recordStatusId and rs.shortName = '" + ConfRecordStatus.ENABLED + "' " +
			" where cs.accountId = (select acc.accountId from Account acc where acc.clientId = :clientId and acc.clientAccountNumber = :clientAccountNumber) and cs.partnerId = :partnerId " +
			" order by cs.dtTill desc ")
	List<LocalDate> getCotDtTill(Integer clientId, String clientAccountNumber, Integer partnerId);

	@Modifying
	@Transactional
	@Query("update Payment set recordStatusId = ?1 where paymentId = ?2")
	int deletePaymentByPaymentId(Integer recordStatusId, Long paymentId);
}
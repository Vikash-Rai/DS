package com.equabli.datascrubbing.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import com.equabli.datascrubbing.entity.ErrWarJson;
import com.equabli.datascrubbing.entity.LookUp;
import com.equabli.datascrubbing.entity.Product;
import com.equabli.domain.entity.ConfRecordStatus;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	/**
     * Finds accounts by using the record status id as a search criteria.
     * @param recordStatusId
     * @return A list of accounts whose record status id is an exact match with the given record status id.
     *          If no account is found, this method returns an empty list.
     */
    @Query(value="select acc from Account acc "
    		+ "where acc.recordStatusId = :recordStatusId ")
    public Page<Account> findByRecordStatusId(Integer recordStatusId, Pageable pageable);

    @Query(value="select acc from Account acc "
    		+ "join Consumer con on acc.clientId = con.clientId and acc.clientAccountNumber = con.clientAccountNumber "
    		+ "join RecordStatus rs on acc.recordStatusId = rs.recordStatusId "
    		+ "where con.recordStatusId = :recordStatusId "
    		+ "and rs.shortName not in ('Disabled', 'Deleted') "
    		+ "and con.dtmUtcUpdate >= coalesce((select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName and dtmUtcAction not in (select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName)), "
    		+ "(select min(dtmUtcUpdate) from Account))")
    public Page<Account> getConsumerToReprocess(Integer recordStatusId, String jobName, Pageable pageable);

    @Query(value="select acc from Account acc "
    		+ "join Address add on acc.clientId = add.clientId and acc.clientAccountNumber = add.clientAccountNumber "
    		+ "join RecordStatus rs on acc.recordStatusId = rs.recordStatusId "
    		+ "where add.recordStatusId = :recordStatusId "
    		+ "and rs.shortName not in ('Disabled', 'Deleted') "
    		+ "and add.dtmUtcUpdate >= coalesce((select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName and dtmUtcAction not in (select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName)), "
    		+ "(select min(dtmUtcUpdate) from Account))")
    public Page<Account> getAddressToReprocess(Integer recordStatusId, String jobName, Pageable pageable);

    @Query(value="select acc from Account acc "
    		+ "join Phone ph on acc.clientId = ph.clientId and acc.clientAccountNumber = ph.clientAccountNumber "
    		+ "join RecordStatus rs on acc.recordStatusId = rs.recordStatusId "
    		+ "where ph.recordStatusId = :recordStatusId "
    		+ "and rs.shortName not in ('Disabled', 'Deleted') "
    		+ "and ph.dtmUtcUpdate >= coalesce((select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName and dtmUtcAction not in (select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName)), "
    		+ "(select min(dtmUtcUpdate) from Account))")
    public Page<Account> getPhoneToReprocess(Integer recordStatusId, String jobName, Pageable pageable);

    @Query(value="select acc from Account acc "
    		+ "join Email em on acc.clientId = em.clientId and acc.clientAccountNumber = em.clientAccountNumber "
    		+ "join RecordStatus rs on acc.recordStatusId = rs.recordStatusId "
    		+ "where em.recordStatusId = :recordStatusId "
    		+ "and rs.shortName not in ('Disabled', 'Deleted') "
    		+ "and em.dtmUtcUpdate >= coalesce((select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName and dtmUtcAction not in (select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName)), "
    		+ "(select min(dtmUtcUpdate) from Account))")
    public Page<Account> getEmailToReprocess(Integer recordStatusId, String jobName, Pageable pageable);

	@Query(value="select new Account(acc.accountId, acc.clientId, acc.clientAccountNumber, acc.originalAccountNumber) "
			+ "from Account acc "
			+ "where acc.errShortName = :shortName and (:clientId is null or acc.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or acc.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(acc.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(acc.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(acc.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(acc.dtmUtcCreate AS date), current_date) "
			+ "and acc.recordStatusId = :recordStatusId ")
	public List<Account> getAccDetailsForSuspectedInv(String shortName, @Param("clientId") Integer clientId, Integer clientJobScheduleId, Integer recordStatusId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);

	@Query(value="select new Account(acc.accountId, acc.clientId, acc.clientAccountNumber, acc.originalAccountNumber) "
			+ "from ErrWarMessageAccount acc "
			+ "where acc.errwarType = :type and acc.errwarShortName like %:code and (:clientId is null or acc.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or acc.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(acc.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(acc.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(acc.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(acc.dtmUtcCreate AS date), current_date)  ")
	public List<Account> getAccDetailsForSuspectedInvNew(@Param("type") String type,@Param("code") String code,@Param("clientId") Integer clientId,@Param("clientJobScheduleId") Integer clientJobScheduleId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);

	@Query(value="select new Account(acc.accountId, cl.fullName, acc.clientAccountNumber, acc.originalAccountNumber, ewm.shortName, ewm.description) "
			+ "from Account acc "
			+ "join ErrWarMessage ewm on acc.errShortName = ewm.shortName "
			+ "join Client cl on acc.clientId = cl.clientId "
			+ "where (:clientId is null or acc.clientId = :clientId) "
			+ "and coalesce(CAST(acc.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(acc.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(acc.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(acc.dtmUtcCreate AS date), current_date) "
			+ "and acc.recordStatusId = :recordStatusId "
			+ "order by acc.accountId asc ")
	public List<Account> downloadSuspectedAccounts(@Param("clientId") Integer clientId, Integer recordStatusId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);

	@Modifying
	@Transactional
	@Query("update Account set recordStatusId = :newRecordStatusId "
			+ "where recordStatusId = :prevRecordStatusId and DATE_PART('day', current_timestamp - dtmUtcUpdate) >= :tlv and clientId = :clientId")
	int accountScrubRejectedForClient(Integer newRecordStatusId, Integer prevRecordStatusId, Integer tlv, @Param("clientId") Integer clientId);

	@Modifying
	@Transactional
	@Query("update Account set recordStatusId = :newRecordStatusId "
			+ "where recordStatusId = :prevRecordStatusId and DATE_PART('day', current_timestamp - dtmUtcUpdate) >= :equabliLevelVal and clientId not in (:clientIds)")
	public int accountScrubRejectedAtEquabliLevelForRemainingClients(Integer newRecordStatusId, Integer prevRecordStatusId, Integer equabliLevelVal, List<Integer> clientIds);

	@Modifying
	@Transactional
	@Query("update Account set recordStatusId = :newRecordStatusId "
			+ "where recordStatusId = :prevRecordStatusId and DATE_PART('day', current_timestamp - dtmUtcUpdate) >= :equabliLevelVal")
	public int accountScrubRejectedAtEquabliLevelForAllClients(Integer newRecordStatusId, Integer prevRecordStatusId, Integer equabliLevelVal);

	@Modifying
	@Transactional
	@Query("update Account set recordStatusId = :recordStatusId, errShortName = :errShortName, errCodeJson = :errCodeJson "
			+ "where clientId = :clientId and clientAccountNumber = :clientAccountNumber and recordStatusId not in (:recordStatusId, :enabledRecordStatusId) ")
	int accountSuspectedByClientIdAndClientAccountNumber(Integer recordStatusId, String errShortName, Set<ErrWarJson> errCodeJson, @Param("clientId") Integer clientId, String clientAccountNumber, Integer enabledRecordStatusId);

	@Modifying
	@Transactional
	@Query("update Account set recordStatusId = :recordStatusId, errShortName = :errShortName, errCodeJson = :errCodeJson "
			+ "where clientId = :clientId and originalAccountNumber = :originalAccountNumber and originalLenderCreditor = :originalLenderCreditor and recordStatusId not in (:recordStatusId, :enabledRecordStatusId) ")
	int accountSuspectedByClientIdAndOriginalAccountNumber(Integer recordStatusId, String errShortName, Set<ErrWarJson> errCodeJson, @Param("clientId") Integer clientId, String originalAccountNumber, String originalLenderCreditor, Integer enabledRecordStatusId);

	@Query(value="select new Account(acc.clientId, acc.clientAccountNumber, acc.currentLenderCreditor, con.firstName, con.middleName, con.lastName, con.identificationNumber, acc.amtPreChargeOffBalance) "
			+ "from Account acc join Consumer con on acc.clientId = con.clientId and acc.clientAccountNumber = con.clientAccountNumber "
			+ "group by acc.clientId, acc.clientAccountNumber, acc.currentLenderCreditor, con.firstName, con.middleName, con.lastName, con.identificationNumber, acc.amtPreChargeOffBalance "
			+ "having count(*) > 1 AND (MAX(acc.dtmUtcUpdate) > :lastSuccessfulDateTime or MAX(con.dtmUtcUpdate) > :lastSuccessfulDateTime) ")
	public Page<Account> getClientAccountConsumerDeDup(LocalDateTime lastSuccessfulDateTime, Pageable pageable);

	@Query(value="select new Account(acc.originalAccountNumber, acc.clientId, acc.originalLenderCreditor, con.firstName, con.middleName, con.lastName, con.identificationNumber, acc.amtPreChargeOffBalance) "
			+ "from Account acc join Consumer con on acc.clientId = con.clientId and acc.clientAccountNumber = con.clientAccountNumber "
			+ "group by acc.clientId, acc.originalAccountNumber, acc.originalLenderCreditor, con.firstName, con.middleName, con.lastName, con.identificationNumber, acc.amtPreChargeOffBalance "
			+ "having count(*) > 1 AND (MAX(acc.dtmUtcUpdate) > :lastSuccessfulDateTime or MAX(con.dtmUtcUpdate) > :lastSuccessfulDateTime) ")
	public Page<Account> getOriginalAccountConsumerDeDup(LocalDateTime lastSuccessfulDateTime, Pageable pageable);

	@Modifying
	@Transactional
	@Query("update Account set queueId = :queueId, queueStatusId = :queueStatusId, queueReasonId = :queueReasonId, partnerId = null, partnerAssignmentDate = null "
			+ "where clientId = :clientId and clientAccountNumber = :clientAccountNumber ")
	int updateQueueAccount(Integer queueId, Integer queueStatusId, Integer queueReasonId, @Param("clientId") Integer clientId, String clientAccountNumber);


	@Query("select new Account(acc.accountId, acc.clientId, acc.partnerId, acc.partnerType, acc.clientAccountNumber, acc.originalAccountNumber, acc.currentLenderCreditor,"
			+" acc.originalLenderCreditor, acc.productId, acc.productSubTypeId, acc.productSubTypeCount, acc.clientJobScheduleId, acc.originalAccountOpenDate,"
			+" acc.assignedDate, acc.delinquencyDate, acc.chargeOffDate, acc.lastPaymentDate, acc.lastPurchaseDate, acc.lastCashAdvanceDate,"
			+" acc.lastBalanceTransferDate, acc.solDate, acc.clientSolDate, acc.equabliSolDate,"
			+" acc.customerType, acc.debtType, acc.portfolioCode, acc.originalAccountApplicationType, acc.amtLastPayment, acc.amtLastPurchase, acc.amtLastCashAdvance, acc.amtLastBalanceTransfer, acc.amtPreChargeOffBalance,"
			+" acc.amtPreChargeOffPrinciple, acc.amtPreChargeOffInterest, acc.amtPreChargeOffFees, acc.amtPostChargeOffInterest, acc.pctPostChargeOffInterest,"
			+" acc.amtPostChargeOffFee, acc.pctPostChargeOffFee, acc.amtPostChargeOffPayment, acc.amtPostChargeOffCredit, acc.amtAssigned, acc.amtPrincipalAssigned,"
			+" acc.amtInterestAssigned, acc.amtLatefeeAssigned, acc.amtOtherfeeAssigned, acc.amtCourtcostAssigned, acc.amtAttorneyfeeAssigned, acc.productAffinity,"
			+" acc.currentbalanceDate, acc.amtCurrentbalance, acc.amtPrincipalCurrentbalance, acc.amtInterestCurrentbalance, acc.amtLatefeeCurrentbalance,"
			+" acc.amtOtherfeeCurrentbalance, acc.amtCourtcostCurrentbalance, acc.amtAttorneyfeeCurrentbalance, acc.saleReviewStatus, acc.partnerAssignmentDate)"
			+ " from Account acc where acc.accountId=:accountId ")
	Account getAccountByAccountId(Long accountId);

    @Query(value = "select new Product(pr.productId, pr.shortName) from Product pr join RecordStatus rs on pr.recordStatusId = rs.recordStatusId and rs.shortName = '" + ConfRecordStatus.ENABLED + "'  where pr.productId = :productId ")
    Product getProduct(Integer productId);

    @Query(value = "select count(prd.productsubtype_id) from conf.productsubtype prd inner join conf.record_status rs on rs.record_status_id = prd.record_status_id and rs.short_name = '" + ConfRecordStatus.ENABLED  +
            "' where prd.product_id = :productId and prd.subproduct_id = :productSubTypeId ", nativeQuery = true)
    Integer productSubTypeCount(Integer productId, Integer productSubTypeId);

    @Query(value = "select lu.lookupId from LookUp lu inner join LookUpGroup lug on lu.lookupGroupId = lug.lookupGroupId " +
            " join RecordStatus rs on lu.recordStatusId = rs.recordStatusId and rs.shortName = '" + ConfRecordStatus.ENABLED + "' " +
            " where lug.keyvalue = :keyValue and lu.keycode = :debtType ")
    LookUp getLookUpByKeyValue(String keyValue, String debtType);

    @Query(value = "select count(acc.account_id) from data.account acc where acc.client_id = :clientId and acc.original_account_number = :originalAccountNumber and acc.original_lender_creditor = :originalLenderCreditor", nativeQuery = true)
    Integer originalAccountNoDeDup(@Param("clientId") Integer clientId, String originalAccountNumber, String originalLenderCreditor);

	@Modifying
	@Transactional
	@Query("update Account set delinquencyDate = null, firstDelinquencyDate = null "
			+ "where clientId = :clientId and clientAccountNumber = :clientAccountNumber ")
	int updateAccountDelinquencyDate(@Param("clientId") Integer clientId, String clientAccountNumber);

}
package com.equabli.datascrubbing.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.ErrWarMessage;

import jakarta.persistence.TemporalType;

@Repository
public interface ErrWarMessageRepository extends JpaRepository<ErrWarMessage, Long> {

	@Query(value="select new ErrWarMessage(ewm.shortName, ewm.description, count(1)) "
			+ "from ErrWarMessage ewm "
			+ "join Account acc on acc.errShortName = ewm.shortName "
			+ "where (:clientId is null or acc.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or acc.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(acc.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(acc.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(acc.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(acc.dtmUtcCreate AS date), current_date) "
			+ "and acc.recordStatusId = :recordStatusId "
			+ "group by ewm.errwarmessageId")
	public List<ErrWarMessage> getAccSuspectedInvDetails(@Param("clientId") Integer clientId,@Param("clientJobScheduleId") Integer clientJobScheduleId,@Param("recordStatusId") Integer recordStatusId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);

	
	@Query(value="select new ErrWarMessage(case when ewm.errwarType='e' then ewm.errwarShortName else CONCAT('W', SUBSTRING(ewm.errwarShortName,2)) end, ewm.errwarDescription, count(1)) "
			+ "from ErrWarMessageAccount ewm "
			+ "where (:clientId is null or ewm.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or ewm.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(ewm.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(ewm.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(ewm.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(ewm.dtmUtcCreate AS date), current_date) "
			+ "group by ewm.errwarShortName, ewm.errwarDescription, ewm.errwarType ")
	public List<ErrWarMessage> getAccSuspectedInvDetailsNew(@Param("clientId") Integer clientId, @Param("clientJobScheduleId") Long clientJobScheduleId,@Param("placementDateFrom") @Nullable Date placementDateFrom,@Param("placementDateTo") @Nullable Date placementDateTo);

	
	@Query(value="select new ErrWarMessage(ewm.shortName, ewm.description, count(1)) "
			+ "from ErrWarMessage ewm "
			+ "join Consumer con on con.errShortName = ewm.shortName "
			+ "join Account acc on acc.clientId = con.clientId and acc.clientAccountNumber = con.clientAccountNumber "
			+ "where (:clientId is null or con.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or acc.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(con.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(con.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(con.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(con.dtmUtcCreate AS date), current_date) "
			+ "and con.recordStatusId = :recordStatusId "
			+ "group by ewm.errwarmessageId")
	public List<ErrWarMessage> getConsumerSuspectedInvDetails(@Param("clientId") Integer clientId,@Param("clientJobScheduleId") Integer clientJobScheduleId,@Param("recordStatusId") Integer recordStatusId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);

	@Query(value="select new ErrWarMessage(case when ewm.errwarType='e' then ewm.errwarShortName else CONCAT('W', SUBSTRING(ewm.errwarShortName,2)) end, ewm.errwarDescription, count(1)) "
			+ "from ErrWarMessageConsumer ewm "
			+ "where (:clientId is null or ewm.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or ewm.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(ewm.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(ewm.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(ewm.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(ewm.dtmUtcCreate AS date), current_date) "
			+ "group by ewm.errwarShortName, ewm.errwarDescription, ewm.errwarType ")
	public List<ErrWarMessage> getConsumerSuspectedInvDetailsNew(@Param("clientId") Integer clientId, @Param("clientJobScheduleId") Long clientJobScheduleId,  @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);

	
	@Query(value="select new ErrWarMessage(ewm.shortName, ewm.description, count(1)) "
			+ "from ErrWarMessage ewm "
			+ "join Address add on add.errShortName = ewm.shortName "
			+ "join Account acc on acc.clientId = add.clientId and acc.clientAccountNumber = add.clientAccountNumber "
			+ "where (:clientId is null or add.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or acc.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(add.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(add.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(add.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(add.dtmUtcCreate AS date), current_date) "
			+ "and add.recordStatusId = :recordStatusId "
			+ "group by ewm.errwarmessageId")
	public List<ErrWarMessage> getAddressSuspectedInvDetails(@Param("clientId") Integer clientId,@Param("clientJobScheduleId") Integer clientJobScheduleId,@Param("recordStatusId") Integer recordStatusId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);

	@Query(value="select new ErrWarMessage(case when ewm.errwarType='e' then ewm.errwarShortName else CONCAT('W', SUBSTRING(ewm.errwarShortName,2)) end, ewm.errwarDescription, count(1)) "
			+ "from ErrWarMessageAddress ewm "
			+ "where (:clientId is null or ewm.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or ewm.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(ewm.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(ewm.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(ewm.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(ewm.dtmUtcCreate AS date), current_date) "
			+ "group by ewm.errwarShortName, ewm.errwarDescription, ewm.errwarType ")
	public List<ErrWarMessage> getAddressSuspectedInvDetailsNew(@Param("clientId") Integer clientId, @Param("clientJobScheduleId") Long clientJobScheduleId,  @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);

	
	@Query(value="select new ErrWarMessage(ewm.shortName, ewm.description, count(1)) "
			+ "from ErrWarMessage ewm "
			+ "join Phone ph on ph.errShortName = ewm.shortName "
			+ "join Account acc on acc.clientId = ph.clientId and acc.clientAccountNumber = ph.clientAccountNumber "
			+ "where (:clientId is null or ph.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or acc.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(ph.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(ph.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(ph.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo,CAST(ph.dtmUtcCreate AS date), current_date) "
			+ "and ph.recordStatusId = :recordStatusId "
			+ "group by ewm.errwarmessageId")
	public List<ErrWarMessage> getPhoneSuspectedInvDetails(@Param("clientId") Integer clientId,@Param("clientJobScheduleId") Integer clientJobScheduleId,@Param("recordStatusId") Integer recordStatusId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);

	
	@Query(value="select new ErrWarMessage(case when ewm.errwarType='e' then ewm.errwarShortName else CONCAT('W', SUBSTRING(ewm.errwarShortName,2)) end, ewm.errwarDescription, count(1)) "
			+ "from ErrWarMessagePhone ewm "
			+ "where (:clientId is null or ewm.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or ewm.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(ewm.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(ewm.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(ewm.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(ewm.dtmUtcCreate AS date), current_date) "
			+ "group by ewm.errwarShortName, ewm.errwarDescription, ewm.errwarType ")
	public List<ErrWarMessage> getPhoneSuspectedInvDetailsNew(@Param("clientId") Integer clientId, @Param("clientJobScheduleId") Long clientJobScheduleId,  @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);

	
	@Query(value="select new ErrWarMessage(ewm.shortName, ewm.description, count(1)) "
			+ "from ErrWarMessage ewm "
			+ "join Email em on em.errShortName = ewm.shortName "
			+ "join Account acc on acc.clientId = em.clientId and acc.clientAccountNumber = em.clientAccountNumber "
			+ "where (:clientId is null or em.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or acc.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(em.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(em.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(em.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(em.dtmUtcCreate AS date), current_date) "
			+ "and em.recordStatusId = :recordStatusId "
			+ "group by ewm.errwarmessageId")
	public List<ErrWarMessage> getEmailSuspectedInvDetails(@Param("clientId") Integer clientId,@Param("clientJobScheduleId") Integer clientJobScheduleId,@Param("recordStatusId") Integer recordStatusId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);
	
	@Query(value="select new ErrWarMessage(case when ewm.errwarType='e' then ewm.errwarShortName else CONCAT('W', SUBSTRING(ewm.errwarShortName,2)) end, ewm.errwarDescription, count(1)) "
			+ "from ErrWarMessageEmail ewm "
			+ "where (:clientId is null or ewm.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or ewm.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(ewm.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(ewm.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(ewm.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(ewm.dtmUtcCreate AS date), current_date) "
			+ "group by ewm.errwarShortName, ewm.errwarDescription, ewm.errwarType ")
	public List<ErrWarMessage> getEmailSuspectedInvDetailsNew(@Param("clientId") Integer clientId, @Param("clientJobScheduleId") Long clientJobScheduleId,  @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);

	@Query(value="select new ErrWarMessage(case when ewm.errwarType='e' then ewm.errwarShortName else CONCAT('W', SUBSTRING(ewm.errwarShortName,2)) end, ewm.errwarDescription, count(1)) "
			+ "from ErrWarMessagePayment ewm "
			+ "join Payment payment on ewm.paymentId = payment.paymentId "
			+ "where (:clientId is null or ewm.clientId = :clientId) and payment.recordStatusId = :suspectedRecordStatusId "
			+ "and (:clientJobScheduleId is null or ewm.clientJobScheduleId = :clientJobScheduleId) "
			+ "and (:partnerId is null or payment.partnerId = :partnerId) "
			+ "and coalesce(CAST(ewm.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(ewm.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(ewm.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(ewm.dtmUtcCreate AS date), current_date) "
			+ "group by ewm.errwarShortName, ewm.errwarDescription, ewm.errwarType ")
	List<ErrWarMessage> getPaymentSuspectedInvDetailsNew(@Param("clientId") Integer clientId,@Param("suspectedRecordStatusId") Integer suspectedRecordStatusId, @Param("clientJobScheduleId") Long clientJobScheduleId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo,@Param("partnerId") Integer partnerId);

	@Query(value=" select new ErrWarMessage(ewm.shortName, ewm.description, ewm.isApplicable) "
			+ " from ErrWarMessage ewm "
			+ " where ewm.isApplicable=true")
	public List<ErrWarMessage> getAllMandatoryApplicableScrubRules();

	@Query(value=" select new ErrWarMessage(case when src.isErrorCode=true or src.isErrorCode is null then ewm.shortName else CONCAT('W', SUBSTRING(ewm.shortName,2)) end,"
			+ " ewm.description, src.isApplicable)"
			+ " from ErrWarMessage ewm "
			+ " join ScrubRuleConfig src on src.errwarShortName = ewm.shortName "
			+ " join Client cl on cl.clientId = src.clientId "
			+ " where src.clientId = :clientId and src.configuredFor = 'CL' and cl.recordStatusId = :recordStatusId ")
	public List<ErrWarMessage> getAllApplicableScrubRulesForClient(@Param("clientId") Integer clientId, Integer recordStatusId);

	@Query(value=" select new ErrWarMessage(case when src.isErrorCode=true or src.isErrorCode is null then ewm.shortName else CONCAT('W', SUBSTRING(ewm.shortName,2)) end,"
			+ " ewm.description, src.isApplicable) "
			+ " from ErrWarMessage ewm "
			+ " join ScrubRuleConfig src on src.errwarShortName = ewm.shortName "
			+ " where src.configuredFor = 'EQ' ")
	public List<ErrWarMessage> getAllDefaultApplicableScrubRulesForEquabli();
	
	
	
	@Query(value="select new ErrWarMessage(ewm.shortName,ewm.description, ewm.isApplicable) "
			+ "from ErrWarMessage ewm "
			+ "where ewm.entityShortName = 'CL' and ewm.isApplicable=true")
	public List<ErrWarMessage> getChangeLogErrorCodeDescription();


}
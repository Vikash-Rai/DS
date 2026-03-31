package com.equabli.datascrubbing.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.equabli.datascrubbing.entity.Email;

import jakarta.persistence.TemporalType;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

	@Modifying
	@Transactional
	@Query("update Email set recordStatusId = :recordStatusId "
			+ "where clientId = :clientId and clientAccountNumber = :clientAccountNumber and recordStatusId not in (:recordStatusId, :enabledRecordStatusId) ")
	int suspectEmailByClientIdAndClientAccountNumber(Integer recordStatusId, Integer clientId, String clientAccountNumber, Integer enabledRecordStatusId);

	@Modifying
	@Transactional
	@Query("update Email set isPrimary = false "
			+ "where clientId = :clientId and clientAccountNumber = :clientAccountNumber and clientConsumerNumber = :clientConsumerNumber and emailId != :emailId and isPrimary = true ")
	int updateEmailIsPrimaryStatus(Integer clientId, String clientAccountNumber, Long clientConsumerNumber, Long emailId);

	@Query(value="select new Email(em.emailId, em.clientId, em.clientAccountNumber) "
			+ "from Email em "
			+ "join Account acc on acc.clientId = em.clientId and acc.clientAccountNumber = em.clientAccountNumber "
			+ "where em.errShortName = :shortName and (:clientId is null or em.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or acc.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(em.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(em.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(em.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(em.dtmUtcCreate AS date), current_date) "
			+ "and em.recordStatusId = :recordStatusId ")
	public List<Email> getEmailDetailsForSuspectedInv(String shortName, Integer clientId, Integer clientJobScheduleId, Integer recordStatusId, @Temporal(TemporalType.DATE) Date placementDateFrom, @Temporal(TemporalType.DATE) Date placementDateTo);
	
	@Query(value="select new Email(em.emailId, em.clientId, em.clientAccountNumber) "
			+ "from ErrWarMessageEmail em "
			+ "where em.errwarType = :type and em.errwarShortName like %:code and (:clientId is null or em.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or em.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(em.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(em.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(em.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(em.dtmUtcCreate AS date), current_date) ")
	public List<Email> getEmailDetailsForSuspectedInvNew(@Param("type") String type,@Param("code") String code,@Param("clientId")  Integer clientId,@Param("clientJobScheduleId")  Integer clientJobScheduleId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo) ;
}
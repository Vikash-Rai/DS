package com.equabli.datascrubbing.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.equabli.datascrubbing.entity.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

	@Modifying
	@Transactional
	@Query("update Phone set recordStatusId = :recordStatusId "
			+ "where clientId = :clientId and clientAccountNumber = :clientAccountNumber and recordStatusId not in (:recordStatusId, :enabledRecordStatusId) ")
	int suspectPhoneByClientIdAndClientAccountNumber(Integer recordStatusId, Integer clientId, String clientAccountNumber, Integer enabledRecordStatusId);

	@Modifying
	@Transactional
	@Query("update Phone set isPrimary = false "
			+ "where clientId = :clientId and clientAccountNumber = :clientAccountNumber and clientConsumerNumber = :clientConsumerNumber and phoneId != :phoneId and isPrimary = true ")
	int updatePhoneIsPrimaryStatus(Integer clientId, String clientAccountNumber, Long clientConsumerNumber, Long phoneId);

	@Query(value="select new Phone(ph.phoneId, ph.clientId, ph.clientAccountNumber) "
			+ "from Phone ph "
			+ "join Account acc on acc.clientId = ph.clientId and acc.clientAccountNumber = ph.clientAccountNumber "
			+ "where ph.errShortName = :shortName and (:clientId is null or ph.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or acc.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(ph.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(ph.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(ph.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(ph.dtmUtcCreate AS date), current_date) "
			+ "and ph.recordStatusId = :recordStatusId ")
	public List<Phone> getPhoneDetailsForSuspectedInv(String shortName, Integer clientId, Integer clientJobScheduleId, Integer recordStatusId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);
	
	@Query(value="select new Phone(ph.phoneId, ph.clientId, ph.clientAccountNumber) "
			+ "from ErrWarMessagePhone ph "
			+ "where ph.errwarType = :type and ph.errwarShortName like %:code and (:clientId is null or ph.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or ph.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(ph.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(ph.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(ph.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(ph.dtmUtcCreate AS date), current_date) ")
	public List<Phone> getPhoneDetailsForSuspectedInvNew(@Param("type") String type,@Param("code")  String code,@Param("clientId")  Integer clientId,@Param("clientJobScheduleId")  Integer clientJobScheduleId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);
}
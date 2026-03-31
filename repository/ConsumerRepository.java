package com.equabli.datascrubbing.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.equabli.datascrubbing.entity.Consumer;

@Repository
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

	@Query(value="select new Consumer(clientId, clientAccountNumber, clientConsumerNumber) "
			+ "from Consumer "
			+ "group by clientId, clientAccountNumber, contactType,isActive,clientConsumerNumber having count(*) > 1 "
			+ "AND MAX(dtmUtcUpdate) > :lastSuccessfulDateTime ")
	public Page<Consumer> getClientConsumerNoDeDup(LocalDateTime lastSuccessfulDateTime, Pageable pageable);

	@Modifying
	@Transactional
	@Query("update Consumer set recordStatusId = :recordStatusId "
			+ "where clientId = :clientId and clientAccountNumber = :clientAccountNumber and recordStatusId not in (:recordStatusId, :enabledRecordStatusId) ")
	int suspectConsumerByClientIdAndClientAccountNumber(Integer recordStatusId, Integer clientId, String clientAccountNumber, Integer enabledRecordStatusId);

	@Query(value="select new Consumer(con.consumerId, con.clientId, con.clientAccountNumber) "
			+ "from Consumer con "
			+ "join Account acc on acc.clientId = con.clientId and acc.clientAccountNumber = con.clientAccountNumber "
			+ "where con.errShortName = :shortName and (:clientId is null or con.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or acc.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(con.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(con.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(con.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(con.dtmUtcCreate AS date), current_date) "
			+ "and con.recordStatusId = :recordStatusId ")
	public List<Consumer> getConsumerDetailsForSuspectedInv(String shortName, Integer clientId, Integer clientJobScheduleId, Integer recordStatusId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);

	@Query(value="select new Consumer(con.consumerId, con.clientId, con.clientAccountNumber) "
			+ "from ErrWarMessageConsumer con "
			+ "where con.errwarType = :type and con.errwarShortName like %:code and (:clientId is null or con.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or con.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(con.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(con.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(con.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(con.dtmUtcCreate AS date), current_date) ")
	public List<Consumer> getConsumerDetailsForSuspectedInvNew(@Param("type") String type,@Param("code") String code,@Param("clientId") Integer clientId,@Param("clientJobScheduleId") Integer clientJobScheduleId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);

	@Query(value = "select new Consumer(cons.consumerId, cons.clientId, cons.clientAccountNumber)  " 
			+ "from Consumer cons "
			+ "join LookUp lu on lu.groupSequence = cons.contactType "
			+ "join LookUpGroup lg on lg.lookupGroupId = lu.lookupGroupId "
			+ "where cons.clientId = :clientId and cons.clientAccountNumber = :clientAccountNumber "
			+ "and cons.clientConsumerNumber = :clientAttorneyId and lg.keyvalue = 'contact_type' and lu.keycode = :contactType ")
	Consumer getConsumerDataByContactType(Integer clientId, String clientAccountNumber, Long clientAttorneyId, String contactType);

	@Query(value = "select new Consumer(cons.consumerId, cons.clientId, cons.clientAccountNumber)  " 
			+ "from Consumer cons "
			+ "where cons.clientId = :clientId and cons.clientAccountNumber = :clientAccountNumber "
			+ "and cons.clientConsumerNumber = :clientConsumerNumber ")
	Consumer getConsumerData(Integer clientId, String clientAccountNumber, Long clientConsumerNumber);

	@Query(value = "select new Consumer(cons.firstName, cons.middleName, cons.lastName, add.address1, add.address2, add.city, add.stateCode, "
			+ "add.country, add.zip, ph.phone, fax.phone, em.emailAddress) "
			+ "from Consumer cons "
			+ "left join Address add on add.consumerId = cons.consumerId and add.isPrimary = true and add.recordStatusId = :recordStatusId "
			+ "left join Phone ph on ph.consumerId = cons.consumerId and ph.isPrimary = true and ph.recordStatusId = :recordStatusId "
			+ "left join Phone fax on fax.consumerId = cons.consumerId and fax.phoneType = 'F' and fax.recordStatusId = :recordStatusId "
			+ "left join Email em on em.consumerId = cons.consumerId and em.isPrimary = true and em.recordStatusId = :recordStatusId "
			+ "where cons.consumerId = :consumerId ")
	List<Consumer> getConsumerDataByConsumerId(Long consumerId, Integer recordStatusId);

	@Query(value = "select max(start_time) from audt.batch_step_execution where step_name = :stepName and status = 'COMPLETED'", nativeQuery = true)
	Optional<LocalDateTime> getMaxBatchStepExecutionStartTime(@Param("stepName") String stepName);
	
	
	@Query(value = "select new Consumer( consumerId, clientId, accountId, clientAccountNumber, clientConsumerNumber ) "
			+ "from Consumer  "
			+ "where identificationNumber = :identificationNumber and  accountId != :accountId ")
	List<Consumer> getConsumerDataByIdentificationNumberAndAccountId(String identificationNumber, Long accountId);
}
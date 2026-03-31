package com.equabli.datascrubbing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.equabli.datascrubbing.entity.COTService;
import com.equabli.domain.ResponseStatus;
import com.equabli.domain.helpers.CommonConstants;

@Repository
public interface CoTServicerRepository extends JpaRepository<COTService, Long> {

	@Modifying
	@Transactional
	@Query("update COTService set dtTill = (select opr.dtResponse from OperationResponse opr where opr.accountId = :accountId "
			+ " and opr.responseStatus='"+ResponseStatus.RESPONSE_STATUS_SUCCESS+"' and opr.responseSource='"+CommonConstants.RECORD_SOURCE_PARTNER+"' order by opr.dtResponse DESC limit 1) "
			+ " where accountId = :accountId "
			+ " and partnerId = (select acc.partnerId from Account acc where acc.accountId = :accountId) "
			+ " and dtTill IS NULL ")
	int updateDtTillCotServicerOperationResponse(Long accountId);
}
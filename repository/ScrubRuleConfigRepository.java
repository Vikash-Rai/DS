package com.equabli.datascrubbing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.equabli.datascrubbing.entity.ScrubRuleConfig;

@Repository
public interface ScrubRuleConfigRepository extends JpaRepository<ScrubRuleConfig, Long> {

	@Modifying
	@Transactional
	@Query(value=" update ScrubRuleConfig set isApplicable = :isApplicable, isErrorCode = :isErrorCode,  updatedBy = :updatedBy, recordSourceId = :recordSourceId, appId = :appId "
			+ " where clientId = :clientId and errwarShortName = :errwarShortName ")
	public int updateErrorWarnMessageDetailsForClient(Integer clientId, String errwarShortName, boolean isApplicable, String updatedBy, Integer recordSourceId, Integer appId, boolean isErrorCode);
	
	@Modifying
	@Transactional
	@Query(value = " insert into conf.scrub_rule_config (configured_for, client_id, errwar_short_name, is_applicable, created_by, updated_by, record_source_id, app_id, is_error) "
			+ " values (:configuredFor, :clientId, :errwarShortName, :isApplicable, :createdBy, :updatedBy, :recordSourceId, :appId, :isErrorCode) ", nativeQuery = true)
	public void insertErrorWarnMessageDetailsForClient(String configuredFor, Integer clientId, String errwarShortName, boolean isApplicable, String createdBy, String updatedBy, Integer recordSourceId, Integer appId, boolean isErrorCode);

}
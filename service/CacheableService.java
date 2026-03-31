package com.equabli.datascrubbing.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.equabli.datascrubbing.entity.ErrWarMessage;
import com.equabli.datascrubbing.entity.LookUp;

public interface CacheableService {

	@Cacheable(cacheNames="lookUpByGroupKeyValue", key="#keyvalue")
	List<LookUp> lookUpByGroupKeyValue(final String keyvalue);
	
//	@Cacheable("getAllMandatoryApplicableScrubRules")
	List<ErrWarMessage> getAllMandatoryApplicableScrubRules();

//	@Cacheable(cacheNames="getAllApplicableScrubRulesForClient", key="#clientId")
	List<ErrWarMessage> getAllApplicableScrubRulesForClient(final Integer clientId, final Integer recordStatusId);

//	@Cacheable("getAllDefaultApplicableScrubRulesForEquabli")
	List<ErrWarMessage> getAllDefaultApplicableScrubRulesForEquabli();
}
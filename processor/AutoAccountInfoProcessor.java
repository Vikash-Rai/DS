package com.equabli.datascrubbing.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.datascrubbing.entity.AutoAccountInfo;
import com.equabli.datascrubbing.entity.ChangeLog;
import com.equabli.datascrubbing.service.CacheableService;
import com.equabli.datascrubbing.util.DataScrubbingUtils;
import com.equabli.datascrubbing.validation.AutoAccountInfoValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class AutoAccountInfoProcessor implements ItemProcessor<AutoAccountInfo, AutoAccountInfo>  {



	  private final Logger logger = LoggerFactory.getLogger(AutoAccountInfoProcessor.class);

	    @Autowired
		private CacheableService cacheableService;

		@Override
		public AutoAccountInfo process(AutoAccountInfo autoAccountInfo) throws Exception {
			try {
				Map<String, Object> validationMap = new HashMap<String, Object>();
				validationMap.put("autoAccountInfo Id", autoAccountInfo.getAutoAccountInfoId());
				validationMap.put("isAutoAccountInfoValidated", true);

				autoAccountInfo.setErrCodeJson(null);
				
				List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(autoAccountInfo.getClientId(), cacheableService);
				
				AutoAccountInfoValidation.mandatoryValidation(autoAccountInfo, validationMap, cacheableService,errWarMessagesList);

				if(Boolean.parseBoolean(validationMap.get("isAutoAccountInfoValidated").toString())) {
					AutoAccountInfoValidation.lookUpValidation(autoAccountInfo, validationMap, cacheableService, errWarMessagesList);

					if(Boolean.parseBoolean(validationMap.get("isAutoAccountInfoValidated").toString())) {
						AutoAccountInfoValidation.standardize(autoAccountInfo);
						AutoAccountInfoValidation.referenceUpdation(autoAccountInfo);
						AutoAccountInfoValidation.misingRefCheck(autoAccountInfo, validationMap, errWarMessagesList);
					}
					
					if(Boolean.parseBoolean(validationMap.get("isAutoAccountInfoValidated").toString())) {
						AutoAccountInfoValidation.businessRule(autoAccountInfo, validationMap, errWarMessagesList);
					}
				}

				logger.debug("{}",validationMap);
				if(!Boolean.parseBoolean(validationMap.get("isAutoAccountInfoValidated").toString())) {
					autoAccountInfo.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
				} else {
					autoAccountInfo.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return autoAccountInfo;
		}



}

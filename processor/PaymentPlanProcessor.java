package com.equabli.datascrubbing.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.datascrubbing.entity.PaymentPlan;
import com.equabli.datascrubbing.repository.PaymentScheduleRepository;
import com.equabli.datascrubbing.service.CacheableService;
import com.equabli.datascrubbing.util.DataScrubbingUtils;
import com.equabli.datascrubbing.validation.PaymentPlanValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class PaymentPlanProcessor implements ItemProcessor<PaymentPlan, PaymentPlan> {

    private final Logger logger = LoggerFactory.getLogger(PaymentPlanProcessor.class);

    @Autowired
	private CacheableService cacheableService;
    
    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

	@Override
	public PaymentPlan process(PaymentPlan pp) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("PaymentPlan Id", pp.getPaymentPlanId());
			validationMap.put("isPaymentPlanValidated", true);
			
			pp.setErrCodeJson(null);
			
			PaymentPlanValidation.mandatoryValidation(pp, validationMap, cacheableService);
			
			List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(pp.getClientId(), cacheableService);

			if(Boolean.parseBoolean(validationMap.get("isPaymentPlanValidated").toString())) {
				PaymentPlanValidation.lookUpValidation(pp, validationMap, cacheableService);

				if(Boolean.parseBoolean(validationMap.get("isPaymentPlanValidated").toString())) {
					PaymentPlanValidation.standardize(pp);
					PaymentPlanValidation.referenceUpdation(pp);
					PaymentPlanValidation.misingRefCheck(pp, validationMap);

					if(Boolean.parseBoolean(validationMap.get("isPaymentPlanValidated").toString())) {
						
						PaymentPlanValidation.businessRule(pp, validationMap,
								paymentScheduleRepository.getSumOfAllPaymentSchedule(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId(),
										pp.getClientId(), pp.getClientAccountNumber(), pp.getPartnerPlanNumber()),
								paymentScheduleRepository.getCountOfAllPaymentSchedule(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId(),
										pp.getClientId(), pp.getClientAccountNumber(), pp.getPartnerPlanNumber()), errWarMessagesList);
					}
				}
			}

			logger.debug("{}",validationMap);
			if(!pp.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())) {
				if(!Boolean.parseBoolean(validationMap.get("isPaymentPlanValidated").toString())) {
					pp.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
				} else {
					pp.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pp;
	}
}
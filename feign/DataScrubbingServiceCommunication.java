package com.equabli.feign;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.equabli.common.auth.TokenData;
import com.equabli.common.feignclient.CommonServiceCommunication;
import com.equabli.datascrubbing.service.StrategyManagerService;
import com.equabli.domain.AppConfigValue;
import com.equabli.domain.Mail;
import com.equabli.domain.Response;
import com.equabli.domain.helpers.ErrorUtils;
import com.equabli.feignClients.AwsService;
import com.equabli.feignClients.BusinessProcessAutomation;
import com.equabli.feignClients.ConfigurationManagementService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataScrubbingServiceCommunication extends CommonServiceCommunication{
	
	@Autowired
	AwsService awsService;
	
	@Autowired
	BusinessProcessAutomation bpaService;
	
	@Autowired
	ConfigurationManagementService configurationService;
	
	@Autowired
	StrategyManagerService strategyService;
	
	@Override
	public String getRequestSource() {
		return "data-scrubbing";
	}
	
	public Optional<Boolean> mailSend(Map<String,Object> headers,Mail mail) {
		Response<Boolean> response = awsService.uploadFile(headers,mail);
		ErrorUtils.validateResponse(response, "aws-service", log);
		return Optional.of(response.getResponse());
	}
	
	public Optional<Map<String, Object>> startProcessChargedOffAccountLoanPro(Map<String,Object> headers) {
		Response<Map<String, Object>> recordStatus = bpaService.startProcessChargedOffAccountLoanPro(headers);
		//ErrorUtils.validateResponse(recordStatus, "business-process-automation", log);
		return Optional.of(recordStatus.getResponse());
	}
	
	public Optional<Map<String, Object>> ledgerTransactionPosting(Map<String,Object> headers) {
		Response<Map<String, Object>> recordSource = bpaService.ledgerTransactionPosting(headers);
		//ErrorUtils.validateResponse(recordSource, "business-process-automation", log);
		return Optional.of(recordSource.getResponse());
	}
	
	public Optional<Map<String, Object>> solCalculationProcess(Map<String,Object> headers) {
		Response<Map<String, Object>> app = bpaService.solCalculationProcess(headers);
		//ErrorUtils.validateResponse(app, "business-process-automation", log);
		return Optional.of(app.getResponse());
	}
	
	public Optional<Map<String, Object>> changeLogProcess(Map<String,Object> headers) {
		Response<Map<String, Object>> app = bpaService.changeLogProcess(headers);
		//ErrorUtils.validateResponse(app, "business-process-automation", log);
		return Optional.of(app.getResponse());
	}
	
	public Optional<List<AppConfigValue>> getAppConfigValue(TokenData tokenData,AppConfigValue appConfigValue) {
		Response<List<AppConfigValue>> response = configurationService.getAppConfigValue(getHeaders(tokenData),appConfigValue);
		ErrorUtils.validateResponse(response, "configuration-management-service", log);
		return Optional.of(response.getResponse());
	}
	
	public Response<Map<String, Object>> getCommission(Map<String,Object> headers,Map<String,Object> request) {
		Response<Map<String, Object>> app = strategyService.getCommissionByClientAccNum(headers,request);
		//ErrorUtils.validateResponse(app, "strategy-service", log);
		return app;
	}
	
	public Optional<Map<String, Object>> nsfPaymentProcess(Map<String,Object> headers) {
		Response<Map<String, Object>> recordSource = bpaService.nsfPaymentProcess(headers);
		return Optional.of(recordSource.getResponse());
	}

}

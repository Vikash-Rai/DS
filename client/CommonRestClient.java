package com.equabli.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.equabli.common.auth.FetchTokenData;
import com.equabli.config.SecretConfig;
import com.equabli.config.ServicesHostUrls;
import com.equabli.domain.Mail;
import com.equabli.domain.Response;
import com.equabli.domain.entity.ConfApp;
import com.equabli.domain.entity.ConfRecordSource;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.domain.helpers.ErrorUtils;
import com.equabli.feign.DataScrubbingServiceCommunication;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CommonRestClient {

//	RestTemplateBuilder restTemplateBuilder;
//	RestTemplate restTemplate;
	private final DataScrubbingServiceCommunication serviceCommnunication;
	private final FetchTokenData fetchTokenData;

	final static Logger logger = LoggerFactory.getLogger(CommonRestClient.class);

//	public CommonRestClient() {
//		restTemplateBuilder = new RestTemplateBuilder();
//		restTemplate = restTemplateBuilder.build();
//		restTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));
//	}

	public Map<String, Object> callingChargedOffAccountsApi(String authHeader) {
		try {
			if(!CommonUtils.isStringNullOrBlank(SecretConfig.mrlLoanProProcessActivate) && SecretConfig.mrlLoanProProcessActivate.equalsIgnoreCase("true")) {
//				HttpHeaders headers = new HttpHeaders();
				Map<String,Object> headers = new HashMap<>();
				headers.put("Authorization", authHeader);
				headers = CommonUtils.setHeadersToInternalRequest(headers, ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_AP).getRecordSourceId(), ConfApp.confApp.get(ConfApp.ECP_BAT).getAppId());
//
//				HttpEntity<Response<Map<String, Object>>> requestEntity = new HttpEntity<>(null, headers);
//
//				ResponseEntity<Response<Map<String, Object>>> returnData = restTemplate.exchange(ServicesHostUrls.businessProcessAutomationHost + "startProcessChargedOffAccountLoanPro", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<Response<Map<String, Object>>>() {});
//				return returnData.getBody().getResponse();
				
				return serviceCommnunication.startProcessChargedOffAccountLoanPro(headers).get();
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public Map<String, Object> callingLedgerTransactionApi(String authHeader) {
//		HttpHeaders headers = new HttpHeaders();
		Map<String,Object> headers = new HashMap<>();
		headers.put("Authorization", authHeader);
		headers = CommonUtils.setHeadersToInternalRequest(headers, ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_AP).getRecordSourceId(), ConfApp.confApp.get(ConfApp.ECP_BAT).getAppId());
//
//		HttpEntity<Response<Map<String, Object>>> requestEntity = new HttpEntity<>(null, headers);
//
//		ResponseEntity<Response<Map<String, Object>>> returnData = restTemplate.exchange(ServicesHostUrls.businessProcessAutomationHost + "ledgerTransactionPosting", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<Response<Map<String, Object>>>() {});
//		return returnData.getBody().getResponse();
		
		return serviceCommnunication.ledgerTransactionPosting(headers).get();
	}

	public Map<String, Object> callingChangeLogProcess(String authHeader) {
		try {
			if(!CommonUtils.isStringNullOrBlank(SecretConfig.mrlLoanProProcessActivate) && SecretConfig.mrlLoanProProcessActivate.equalsIgnoreCase("true")) {
//				HttpHeaders headers = new HttpHeaders();
				Map<String,Object> headers = new HashMap<>();
				headers.put("Authorization", authHeader);
				headers = CommonUtils.setHeadersToInternalRequest(headers, ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_AP).getRecordSourceId(), ConfApp.confApp.get(ConfApp.ECP_BAT).getAppId());
//
//				HttpEntity<Response<Map<String, Object>>> requestEntity = new HttpEntity<>(null, headers);
//
//				ResponseEntity<Response<Map<String, Object>>> returnData = restTemplate.exchange(ServicesHostUrls.businessProcessAutomationHost + "changeLogProcess", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<Response<Map<String, Object>>>() {});
//				return returnData.getBody().getResponse();
				
				return serviceCommnunication.changeLogProcess(headers).get();
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public Map<String, Object> callingSOLRecalculationProcess(String authHeader) {
		try {
			if(!CommonUtils.isStringNullOrBlank(SecretConfig.mrlLoanProProcessActivate) && SecretConfig.mrlLoanProProcessActivate.equalsIgnoreCase("true")) {
//				HttpHeaders headers = new HttpHeaders();
				Map<String,Object> headers = new HashMap<>();
				headers.put("Authorization", authHeader);
				headers = CommonUtils.setHeadersToInternalRequest(headers, ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_AP).getRecordSourceId(), ConfApp.confApp.get(ConfApp.ECP_BAT).getAppId());
//
//				HttpEntity<Response<Map<String, Object>>> requestEntity = new HttpEntity<>(null, headers);
//
//				ResponseEntity<Response<Map<String, Object>>> returnData = restTemplate.exchange(ServicesHostUrls.businessProcessAutomationHost + "solCalculationProcess", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<Response<Map<String, Object>>>() {});
//				return returnData.getBody().getResponse();
				
				return serviceCommnunication.solCalculationProcess(headers).get();
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

//	public Map<String, Object> callingPaymentProcessApi() {
//		HttpHeaders headers = new HttpHeaders();
//		headers = CommonUtils.setHeadersToInternalRequest(headers, ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_AP).getRecordSourceId(), ConfApp.confApp.get(ConfApp.ECP_BAT).getAppId());
//
//		HttpEntity<Response<Map<String, Object>>> requestEntity = new HttpEntity<>(null, headers);
//
//		ResponseEntity<Response<Map<String, Object>>> returnData = restTemplate.exchange(ServicesHostUrls.businessProcessAutomationHost + "paymentProcess", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<Response<Map<String, Object>>>() {});
//		return returnData.getBody().getResponse();
//	}

	public Response<Boolean> sendMail(String subject, String body,String authHeader) {
		Mail mail = new Mail(ServicesHostUrls.emailFromAddress, ServicesHostUrls.partnerSupportEmail.split(";"), ServicesHostUrls.partnerSupportEmail.split(";"), subject, body);
		Response<Boolean> response = new Response<Boolean>();
		try {
//			HttpHeaders headers = new HttpHeaders();
			Map<String,Object> headers = new HashMap<>();
			headers.put("Authorization", authHeader);
			headers = CommonUtils.setHeadersToInternalRequest(headers, ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_CL).getRecordSourceId(), ConfApp.confApp.get(ConfApp.ECP_VI).getAppId());
//
//			HttpEntity<Response<Boolean>> requestUpdate = new HttpEntity(mail, headers);
//			ResponseEntity<Response> returnData = restTemplate.exchange(ServicesHostUrls.awsServiceHost + "mail/send", HttpMethod.POST, requestUpdate, Response.class);
//			Response<Boolean> mailSent = returnData.getBody();
			response.setResponse(serviceCommnunication.mailSend(headers, mail).get());
			
		} catch (Exception ex) {
			ErrorUtils.buildErrorResponse(response, ex, logger);
		}
		return response;
	}
	
	public Map<String, Object> callingNsfPaymentProcessApi(String authHeader) {
		try {
			Map<String,Object> headers = new HashMap<>();
			headers.put("Authorization", authHeader);
			headers = CommonUtils.setHeadersToInternalRequest(headers, ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_AP).getRecordSourceId(), ConfApp.confApp.get(ConfApp.ECP_BAT).getAppId());
			return serviceCommnunication.nsfPaymentProcess(headers).get();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
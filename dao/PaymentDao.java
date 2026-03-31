package com.equabli.datascrubbing.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.equabli.datascrubbing.entity.Payment;
import com.equabli.datascrubbing.entity.PaymentBucketConfig;
import com.equabli.datascrubbing.entity.PaymentPlan;
import com.equabli.domain.Response;

public interface PaymentDao {

	void insertIntoPaymentCancelPayment(Payment cPayment);

	void updatePaymentPlanErrCodeJson(PaymentPlan pp);
	
	Timestamp testDataScrubbingService();

	Response<Map<String,Object>> insertOrUpdatePaymentDetails(Payment payment, Boolean isPartnerCommission);
	
	Double getUpdatedAccountBalance(Payment payment);

	Response<Map<String, Object>> deletePaymentDetails(Long paymentId);
	
	List<Map<String, Object>> isLegalPlacementExists(Integer clientId,String clientAccountNumber);
	
	Response<List<PaymentBucketConfig>>  getPaymentBucketDistributionConfig(Integer clientId);

}

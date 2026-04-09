package com.equabli.datascrubenrichmentservice.approach.enrichmenthandler;

import com.equabli.datascrubenrichmentservice.entity.Account;
import com.equabli.datascrubenrichmentservice.approach.validationhandler.ValidationContext;
import com.equabli.domain.helpers.CommonUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// CurrentBalanceHandler.java
@Component
@Order(2)
public class CurrentBalanceHandler extends AbstractEnrichmentHandler {

    @Override
    public void enrich(Account account, ValidationContext ctx) {
        account.setCurrentBalanceData(account);
        try {
            if (CommonUtils.isObjectNull(account.getChargeOffDate())) {
                account.setPreChargeOffBuckets(account);
            }
        } catch (Exception e) {
            // log warning
        }
        enrichNext(account, ctx);
    }
}
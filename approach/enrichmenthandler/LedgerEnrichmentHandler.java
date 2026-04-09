package com.equabli.datascrubenrichmentservice.approach.enrichmenthandler;

import com.equabli.datascrubenrichmentservice.entity.Account;
import com.equabli.datascrubenrichmentservice.entity.Ledger;
import com.equabli.datascrubenrichmentservice.approach.validationhandler.ValidationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// LedgerEnrichmentHandler.java
@Component
@Order(3)
public class LedgerEnrichmentHandler extends AbstractEnrichmentHandler {

    @Override
    public void enrich(Account account, ValidationContext ctx) {
        account.setLedger(
                Ledger.setLedger(
                        account,
                        ctx.getUpdatedBy(),
                        ctx.getRecordSourceId(),
                        ctx.getAppId()));
        enrichNext(account, ctx);
    }
}
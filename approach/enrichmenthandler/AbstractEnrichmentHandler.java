package com.equabli.datascrubenrichmentservice.approach.enrichmenthandler;

import com.equabli.datascrubenrichmentservice.entity.Account;
import com.equabli.datascrubenrichmentservice.approach.validationhandler.ValidationContext;


// Stores reference to next handler
public abstract class AbstractEnrichmentHandler
        implements EnrichmentHandler {

    private EnrichmentHandler next;

    @Override
    public EnrichmentHandler setNext(EnrichmentHandler next) {
        this.next = next;
        return next;
    }

    protected void enrichNext(Account account, ValidationContext ctx) {
        if (next != null) {
            next.enrich(account, ctx);
        }
    }
}

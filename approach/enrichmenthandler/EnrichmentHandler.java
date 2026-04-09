package com.equabli.datascrubenrichmentservice.approach.enrichmenthandler;

import com.equabli.datascrubenrichmentservice.entity.Account;
import com.equabli.datascrubenrichmentservice.approach.validationhandler.ValidationContext;


public interface EnrichmentHandler {
    void enrich(Account account, ValidationContext ctx); // main processing logic
    EnrichmentHandler setNext(EnrichmentHandler next); // link to next handler
}
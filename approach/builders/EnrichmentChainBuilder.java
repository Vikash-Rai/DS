package com.equabli.datascrubenrichmentservice.approach.builders;

import com.equabli.datascrubenrichmentservice.approach.enrichmenthandler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

 /**
  * Each handler:
  *       modifies account
  *       always calls next (no stop)
  */

@Component
public class EnrichmentChainBuilder {
    // sol → balance → ledger → queue
    @Autowired
    private SOLEnrichmentHandler solHandler;

    @Autowired
    private CurrentBalanceHandler balanceHandler;

    @Autowired
    private LedgerEnrichmentHandler ledgerHandler;

    @Autowired
    private QueueAssignmentHandler queueHandler;

    public EnrichmentHandler build() {
        solHandler
                .setNext(balanceHandler)
                .setNext(ledgerHandler)
                .setNext(queueHandler);

        return solHandler;
    }
}

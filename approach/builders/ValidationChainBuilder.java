package com.equabli.datascrubenrichmentservice.approach.builders;

import com.equabli.datascrubenrichmentservice.approach.validationhandler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Decouples validation logic
// Easy to add/remove handlers
// Follows Open/Closed Principle
// Clean and scalable


/**
 * Each handler:
 *  checks something
 *  either fails -> stop
 *  or passes -> next handler
 */
@Component
public class ValidationChainBuilder {
    //primary -> mandatory -> lookup -> businessRule -> autoLoanCheck
    @Autowired
    private PrimaryConsumerHandler primaryConsumerHandler;

    @Autowired
    private MandatoryFieldHandler mandatoryFieldHandler;

    @Autowired
    private LookupValidationHandler lookupValidationHandler;

    @Autowired
    private BusinessRuleHandler businessRuleHandler;

    @Autowired
    private AutoLoanCheckHandler autoLoanCheckHandler;

    public ValidationHandler build() {

        // Wire the chain
        primaryConsumerHandler
                .setNext(mandatoryFieldHandler)
                .setNext(lookupValidationHandler)
                .setNext(businessRuleHandler)
                .setNext(autoLoanCheckHandler);

        return primaryConsumerHandler;
    }
}
package com.equabli.datascrubenrichmentservice.approach.validationhandler;

import com.equabli.datascrubenrichmentservice.entity.Account;

public interface ValidationHandler {
    ValidationResult handle(Account account, ValidationContext ctx);
    ValidationHandler setNext(ValidationHandler next);
}
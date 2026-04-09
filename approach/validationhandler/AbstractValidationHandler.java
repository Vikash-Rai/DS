package com.equabli.datascrubenrichmentservice.approach.validationhandler;

import com.equabli.datascrubenrichmentservice.entity.Account;
import com.equabli.datascrubenrichmentservice.entity.ErrWarJson;
import com.equabli.domain.entity.ConfRecordStatus;

// the engine of chaining
public abstract class AbstractValidationHandler
        implements ValidationHandler {

    private ValidationHandler next;

    @Override
    public ValidationHandler setNext(ValidationHandler next) {
        this.next = next;
        return next; // enables fluent chaining
    }

    protected ValidationResult passToNext(Account account,
                                          ValidationContext ctx) {
        if (next != null) {
            return next.handle(account, ctx);
        }
        return ValidationResult.passed();
    }

    protected void markSuspected(Account account,
                                 String errCode,
                                 ValidationContext ctx) {
        ctx.markInvalid();
        account.setErrShortName(errCode);
        account.addErrWarJson(new ErrWarJson("e", errCode));
        account.setRecordStatusId(
                ConfRecordStatus.confRecordStatus
                        .get(ConfRecordStatus.SUSPECTED)
                        .getRecordStatusId());
    }
}


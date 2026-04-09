package com.equabli.datascrubenrichmentservice.approach.validationhandler;

import com.equabli.datascrubenrichmentservice.entity.Account;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(5)
public class AutoLoanCheckHandler extends AbstractValidationHandler {

    @Override
    public ValidationResult handle(Account account,
                                   ValidationContext ctx) {
        if (ctx.contains("E70138") &&
                account.getProduct() != null &&
                "AL".equalsIgnoreCase(account.getProduct().getShortName()) &&
                account.getAutoAccountInfoIds() == null) {
            markSuspected(account, "E70138", ctx);
            return ValidationResult.failed("E70138",
                    "Auto loan requires AutoAccountInfo");
        }

        return passToNext(account, ctx);
    }
}
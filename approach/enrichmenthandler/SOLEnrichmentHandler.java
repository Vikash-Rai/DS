package com.equabli.datascrubenrichmentservice.approach.enrichmenthandler;

import com.equabli.datascrubenrichmentservice.entity.Account;
import com.equabli.datascrubenrichmentservice.entity.Address;
import com.equabli.datascrubenrichmentservice.entity.Consumer;
import com.equabli.datascrubenrichmentservice.processor.AccountProcessor;
import com.equabli.datascrubenrichmentservice.approach.validationhandler.ValidationContext;
import com.equabli.domain.Response;
import com.equabli.domain.SOLCalulation;
import com.equabli.domain.StatuteOfLimitation;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.domain.helpers.SOLCalculation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class SOLEnrichmentHandler extends AbstractEnrichmentHandler {

    @Override
    public void enrich(Account account, ValidationContext ctx) {
        try {
            SOLCalulation solCalulation = new SOLCalulation();
            AccountProcessor.setSolCalulation(account, solCalulation);

            Integer solMonth = 0;
            Integer clientConfiguredDays = 0;

            for (Consumer consumer : account.getConsumer()) {
                if (consumer.getContactTypeLookUp() != null &&
                        "PD".equalsIgnoreCase(
                                consumer.getContactTypeLookUp().getKeycode())) {
                    for (Address address : consumer.getAddress()) {
                        if (Boolean.TRUE.equals(address.getIsPrimary())) {
                            solCalulation.setStateCode(
                                    address.getStateCode());
                            if (address.getStatutesOfLimitation() != null) {
                                solMonth = address.getStatutesOfLimitation()
                                        .getSolMonth();
                            }
                            if (address.getClientStatutesOfLimitation()
                                    != null) {
                                clientConfiguredDays = address
                                        .getClientStatutesOfLimitation()
                                        .getSolDay();
                            }
                        }
                    }
                }
            }

            Response<StatuteOfLimitation> response =
                    SOLCalculation.solCalculation(
                            account.getAccountId(), solCalulation,
                            solMonth, clientConfiguredDays,
                            new Response<>());

            StatuteOfLimitation sol = response.getResponse();
            if (sol != null && sol.getIsPrimaryStateExists()) {
                AccountProcessor.updateSOLDateDetailsInAccount(
                        sol.getCalculatedSOLDate(),
                        sol.getCurrentSOLDate(),
                        CommonUtils.isDateNull(sol.getCurrentSOLDate()),
                        false, account);
            } else {
                account.setRecordStatusId(
                        ConfRecordStatus.confRecordStatus
                                .get(ConfRecordStatus.ENABLED)
                                .getRecordStatusId());
            }
        } catch (Exception e) {
            // log and continue — enrichment failure should
            // not block the record
        }

        enrichNext(account, ctx);
    }
}
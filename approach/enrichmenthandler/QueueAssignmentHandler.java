package com.equabli.datascrubenrichmentservice.approach.enrichmenthandler;

import com.equabli.datascrubenrichmentservice.entity.Account;
import com.equabli.datascrubenrichmentservice.approach.validationhandler.ValidationContext;
import com.equabli.domain.Queue;
import com.equabli.domain.QueueReason;
import com.equabli.domain.QueueStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// QueueAssignmentHandler.java
@Component
@Order(4)
public class QueueAssignmentHandler extends AbstractEnrichmentHandler {

    @Override
    public void enrich(Account account, ValidationContext ctx) {
        if (!CommonUtils.isStringNullOrBlank(account.getPartnerType()) &&
                CommonConstants.PARTNER_TYPE_HOLDING_UNIT
                        .equals(account.getPartnerType())) {
            account.setQueueId(
                    Queue.confQueue.get(Queue.QUEUE_DNP).getQueueId());
            account.setQueueStatusId(
                    QueueStatus.confQueueStatus
                            .get(QueueStatus.QUEUESTATUS_DNP)
                            .getQueueStatusId());
            account.setQueueReasonId(
                    QueueReason.confQueueReason
                            .get(QueueReason.QUEUEREASON_NA)
                            .getQueueReasonId());
        } else {
            account.setQueueId(
                    Queue.confQueue.get(Queue.QUEUE_PRP).getQueueId());
            account.setQueueStatusId(
                    QueueStatus.confQueueStatus
                            .get(QueueStatus.QUEUESTATUS_OPN)
                            .getQueueStatusId());
            account.setQueueReasonId(
                    QueueReason.confQueueReason
                            .get(QueueReason.QUEUEREASON_NA)
                            .getQueueReasonId());
        }
        enrichNext(account, ctx);
    }
}
package com.equabli.datascrubenrichmentservice.approach.validationhandler;

import com.equabli.datascrubenrichmentservice.entity.ScrubWarning;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ValidationContext {
    private final List<String> errWarMessagesList;
    private final String updatedBy;
    private final Integer appId;
    private final Integer recordSourceId;
    private final Map<String, Object> validationMap;
    private boolean primaryDebtorFound;
    private List<ScrubWarning> scrubWarnings;

    public ValidationContext(List<String> errWarMessages,
                             String updatedBy,
                             Integer appId,
                             Integer recordSourceId) {
        this.errWarMessagesList = errWarMessages;
        this.updatedBy = updatedBy;
        this.appId = appId;
        this.recordSourceId = recordSourceId;
        this.validationMap = new HashMap<>();
        this.scrubWarnings = new ArrayList<>();
        this.primaryDebtorFound = false;
        this.validationMap.put("isAccountValidated", true);
    }

    public boolean isAccountValid() {
        return Boolean.parseBoolean(
                validationMap.getOrDefault("isAccountValidated", true).toString());
    }

    public void markInvalid() {
        validationMap.put("isAccountValidated", false);
    }

    public boolean contains(String code) {
        return errWarMessagesList.contains(code);
    }
}
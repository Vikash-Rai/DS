package com.equabli.datascrubbing.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.datascrubbing.entity.Compliance;
import com.equabli.datascrubbing.repository.ComplianceRepository;

public class ComplianceWriter implements ItemWriter<Compliance> {

    private final Logger logger = LoggerFactory.getLogger(ComplianceWriter.class);

    @Autowired
    private ComplianceRepository complianceRepository;

    @Override
    public void write(Chunk<? extends Compliance> compliance) throws Exception {
    	complianceRepository.saveAll(compliance);
        for (Compliance comp : compliance) {
            logger.debug("Writer executed.....Compliance Id={}.....Compliance Status={}", comp.getComplianceId(), comp.getRecordStatusId());
        }
    }
}
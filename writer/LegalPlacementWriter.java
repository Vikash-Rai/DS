package com.equabli.datascrubbing.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.datascrubbing.entity.LegalPlacement;
import com.equabli.datascrubbing.repository.LegalPlacementRepository;

public class LegalPlacementWriter implements ItemWriter<LegalPlacement> {

    private final Logger logger = LoggerFactory.getLogger(LegalPlacementWriter.class);

    @Autowired
    private LegalPlacementRepository legalPlacementRepository;

    @Override
    public void write(Chunk<? extends LegalPlacement> legalPlacement) throws Exception {
    	legalPlacementRepository.saveAll(legalPlacement);
        for (LegalPlacement lp : legalPlacement) {
            logger.debug("Writer executed.....LegalPlacement Id={}.....LegalPlacement Status={}", lp.getLegalPlacementId(), lp.getRecordStatusId());
        }
    }
}
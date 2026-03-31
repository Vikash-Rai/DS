package com.equabli.datascrubbing.writer;

import com.equabli.datascrubbing.entity.AccountTag;
import com.equabli.datascrubbing.repository.AccountTagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AccountTagWriter implements ItemWriter<AccountTag> {

    private final Logger logger = LoggerFactory.getLogger(AccountTagWriter.class);

    @Autowired
    private AccountTagRepository accountTagRepository;

    @Override
    public void write(Chunk<? extends AccountTag> accountTags) throws Exception {
        accountTagRepository.saveAll(accountTags);
        for (AccountTag accountTag : accountTags) {
            logger.debug("Writer executed.....Account Tag Id={}.....Account Tag Status={}", accountTag.getAccountTagId(), accountTag.getRecordStatusId());
        }
    }
}
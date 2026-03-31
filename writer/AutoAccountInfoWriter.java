package com.equabli.datascrubbing.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.datascrubbing.entity.AutoAccountInfo;
import com.equabli.datascrubbing.entity.ChangeLog;
import com.equabli.datascrubbing.repository.AutoAccountInfoRepository;

public class AutoAccountInfoWriter  implements ItemWriter<AutoAccountInfo>{



    private final Logger logger = LoggerFactory.getLogger(AutoAccountInfoWriter.class);

    @Autowired
    private AutoAccountInfoRepository autoAccountInfoRepository;

    @Override
    public void write(Chunk<? extends AutoAccountInfo> autoAccountInfo) throws Exception {
    	autoAccountInfoRepository.saveAll(autoAccountInfo);
        for (AutoAccountInfo autoAccountInfo1 : autoAccountInfo) {
            logger.debug("Writer executed.....AutoAccountInfo Id={}.....AutoAccountInfo Status={}", autoAccountInfo1.getAutoAccountInfoId(), autoAccountInfo1.getRecordStatusId());
        }
    }



}

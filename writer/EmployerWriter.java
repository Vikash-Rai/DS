package com.equabli.datascrubbing.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.datascrubbing.entity.Employer;
import com.equabli.datascrubbing.repository.EmployerRepository;

public class EmployerWriter implements ItemWriter<Employer> {

    private final Logger logger = LoggerFactory.getLogger(EmployerWriter.class);

    @Autowired
    private EmployerRepository employerRepository;

    @Override
    public void write(Chunk<? extends Employer> employer) throws Exception {
    	employerRepository.saveAll(employer);
        for (Employer emp : employer) {
            logger.debug("Writer executed.....Employer Id={}.....Employer Status={}", emp.getEmployerId(), emp.getRecordStatusId());
        }
    }
}
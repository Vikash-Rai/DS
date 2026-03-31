package com.equabli.datascrubbing.writer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.equabli.datascrubbing.entity.Account;
import com.equabli.datascrubbing.entity.Ledger;
import com.equabli.datascrubbing.entity.ScrubWarning;
import com.equabli.datascrubbing.repository.AccountRepository;
import com.equabli.datascrubbing.repository.LedgerRepository;
import com.equabli.datascrubbing.repository.ScrubWarningRepository;
import com.equabli.domain.helpers.CommonUtils;

@Transactional
public class AccountWriter implements ItemWriter<Account> {

    private final Logger logger = LoggerFactory.getLogger(AccountWriter.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ScrubWarningRepository scrubWarningRepository;

    @Autowired
    private LedgerRepository ledgerRepository;

    @Override
    public void write(Chunk<? extends Account> accounts) throws Exception {
        logger.debug("Writing batch of {} accounts", accounts.size());

        accountRepository.saveAll(accounts);

    	List<ScrubWarning> warnings = new ArrayList<>();
    	List<Ledger> ledgers = new ArrayList<>();

    	for (Account acc : accounts) {
        	if(acc.getScrubWarnings() != null && acc.getScrubWarnings().size() > 0) {
                warnings.addAll(acc.getScrubWarnings());
        	}
        	if(acc.getLedger() != null && !CommonUtils.isLongNullOrZero(acc.getLedger().getAccountId())) {
                ledgers.add(acc.getLedger());
        	}
        }

    	if (!warnings.isEmpty()) scrubWarningRepository.saveAll(warnings);
    	if (!ledgers.isEmpty()) ledgerRepository.saveAll(ledgers);

    	warnings.clear();
    	ledgers.clear();

    	logger.debug("Finished processing batch of {} accounts", accounts.size());
    }
}
package com.equabli.datascrubbing.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;

import com.equabli.datascrubbing.entity.PaymentSchedule;
import com.equabli.datascrubbing.processor.PaymentScheduleProcessor;
import com.equabli.datascrubbing.reader.CustomItemReader;
import com.equabli.datascrubbing.repository.PaymentScheduleRepository;
import com.equabli.datascrubbing.writer.PaymentScheduleWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class PaymentScheduleConfig {

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    @StepScope
    public CustomItemReader<PaymentSchedule> paymentScheduleReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<PaymentSchedule> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("paymentScheduleId", Direction.ASC);

        reader.setRepository(paymentScheduleRepository);
        reader.setMethodName("getPaymentScheduleToProcess");
        reader.setJobName("Payment Schedule Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<PaymentSchedule, PaymentSchedule> paymentScheduleProcessor() {
        return new PaymentScheduleProcessor();
    }

    @Bean
    public ItemWriter<PaymentSchedule> paymentScheduleWriter() {
        return new PaymentScheduleWriter();
    }

    @Bean
    protected Step processPaymentSchedule(ItemReader<PaymentSchedule> itemReader, ItemProcessor<PaymentSchedule, PaymentSchedule> processor, ItemWriter<PaymentSchedule> writer) {
        return new StepBuilder("processPaymentSchedule",jobRepository).<PaymentSchedule, PaymentSchedule> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job paymentScheduleJob() {
//        return new JobBuilder("paymentScheduleJob",jobRepository)
//          .start(processPaymentSchedule(paymentScheduleReader(), paymentScheduleProcessor(), paymentScheduleWriter()))
//          .build();
//    }
    
    @Bean
    public Job paymentScheduleJob(Step processPaymentSchedule) {
        return new JobBuilder("paymentScheduleJob", jobRepository)
                .start(processPaymentSchedule)
                .build();
    }
}
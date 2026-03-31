package com.equabli.datascrubbing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import com.equabli.config.CommonComponentScanConfig;

@SpringBootApplication
@EnableCaching
@Import(CommonComponentScanConfig.class)
@EnableFeignClients(basePackages = "com.equabli.*")
public class DataScrubbingApplication {

	public static void main(String[] args) {
//		LoggingConfig.main(args);
		SpringApplication.run(DataScrubbingApplication.class, args);
	}
}
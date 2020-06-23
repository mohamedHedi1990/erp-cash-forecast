package org.apac.erp.cach.forecast;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @EnableConfigurationProperties({ FileStorageProperties.class })
public class ErpCachForecastApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ErpCachForecastApplication.class, args);
		System.out.println("------------ The ERP CASH FORECAST server was sucessuflly started ---");

	}

	@Override
	public void run(String... args) throws Exception {
	}

}
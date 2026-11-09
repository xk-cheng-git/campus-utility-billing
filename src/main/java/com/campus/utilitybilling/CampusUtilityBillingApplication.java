package com.campus.utilitybilling;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.campus.utilitybilling.mapper")
public class CampusUtilityBillingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampusUtilityBillingApplication.class, args);
	}

}
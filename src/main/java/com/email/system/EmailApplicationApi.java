package com.email.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class EmailApplicationApi {

	public static void main(String[] args) {
		SpringApplication.run(EmailApplicationApi.class, args);
	}

}

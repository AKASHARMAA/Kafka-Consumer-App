package com.greendeck.KafkaConsumerApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaConsumerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaConsumerAppApplication.class, args);
		
		System.out.println("================= Akash Sharma consumer app on kafka ===============");
	}

}

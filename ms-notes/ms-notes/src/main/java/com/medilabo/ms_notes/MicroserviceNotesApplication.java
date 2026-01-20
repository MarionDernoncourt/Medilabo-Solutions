package com.medilabo.ms_notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoAuditing
public class MicroserviceNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceNotesApplication.class, args);
	}

}

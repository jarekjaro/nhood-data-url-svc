package com.h8.nh.nhoodlocationsvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NhoodLocationSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(NhoodLocationSvcApplication.class, args);
	}
}

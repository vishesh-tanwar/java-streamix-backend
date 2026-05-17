package com.yt.backend.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class YtApplication {
	public static void main(String[] args) {
		SpringApplication.run(YtApplication.class, args);
	}
}

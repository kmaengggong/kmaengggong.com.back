package com.kmaengggong.kmaengggong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KmaengggongApplication {

	public static void main(String[] args) {
		SpringApplication.run(KmaengggongApplication.class, args);
	}

}

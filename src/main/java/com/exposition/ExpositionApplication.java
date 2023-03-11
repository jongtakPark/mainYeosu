package com.exposition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
public class ExpositionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpositionApplication.class, args);
	}
	
	// PutMapping, DeleteMapping 하기 위해서 추가
	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
		return new HiddenHttpMethodFilter();
	}

}

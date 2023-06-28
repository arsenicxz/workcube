package com.vasenin.workcube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableScheduling
@SpringBootApplication
public class WorkcubeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkcubeApplication.class, args);
	}

}

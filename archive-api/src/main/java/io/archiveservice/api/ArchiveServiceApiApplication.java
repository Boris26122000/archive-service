package io.archiveservice.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "io.archiveservice")
@SpringBootApplication
public class ArchiveServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchiveServiceApiApplication.class, args);
	}

}

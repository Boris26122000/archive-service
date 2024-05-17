package io.archiveservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArchiveServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchiveServiceApiApplication.class, args);
		System.out.println("Hello world");
	}

}

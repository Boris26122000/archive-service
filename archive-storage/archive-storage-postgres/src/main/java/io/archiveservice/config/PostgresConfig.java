package io.archiveservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "io.archiveservice.repository")
@EntityScan(basePackages = "io.archiveservice.entity")
public class PostgresConfig {
}

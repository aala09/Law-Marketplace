package tn.esprit.cloud_in_mypocket.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public FlywayMigrationStrategy repairFlywayStrategy() {
        return flyway -> {
            // Repair the Flyway metadata table
            flyway.repair();
            // Then perform the actual migration
            flyway.migrate();
        };
    }
}

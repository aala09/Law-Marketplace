package tn.esprit.cloud_in_mypocket.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;
import jakarta.annotation.PostConstruct;

@Configuration
public class FlywayMigrationInitializer {
    
    private final DataSource dataSource;

    @Autowired
    public FlywayMigrationInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void migrateFlyway() {
        Flyway flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)
            .load();
        
        // Force execution of migrations
        flyway.migrate();
        
        System.out.println("Flyway migration completed successfully");
    }
}

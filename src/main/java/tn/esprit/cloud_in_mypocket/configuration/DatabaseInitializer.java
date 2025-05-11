package tn.esprit.cloud_in_mypocket.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = Logger.getLogger(DatabaseInitializer.class.getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        try {
            logger.info("Checking and adding file columns to dossiers table...");
            
            // First check if columns exist to avoid errors
            boolean hasFileData = columnExists("dossiers", "file_data");
            boolean hasFileName = columnExists("dossiers", "file_name");
            boolean hasFileType = columnExists("dossiers", "file_type");
            boolean hasFileSize = columnExists("dossiers", "file_size");
            
            if (!hasFileData || !hasFileName || !hasFileType || !hasFileSize) {
                StringBuilder alterSql = new StringBuilder("ALTER TABLE dossiers ");
                
                if (!hasFileData) {
                    alterSql.append("ADD COLUMN file_data LONGBLOB, ");
                }
                
                if (!hasFileName) {
                    alterSql.append("ADD COLUMN file_name VARCHAR(255), ");
                }
                
                if (!hasFileType) {
                    alterSql.append("ADD COLUMN file_type VARCHAR(100), ");
                }
                
                if (!hasFileSize) {
                    alterSql.append("ADD COLUMN file_size BIGINT, ");
                }
                
                // Remove the trailing comma and space
                String finalSql = alterSql.substring(0, alterSql.length() - 2);
                
                logger.info("Executing SQL: " + finalSql);
                jdbcTemplate.execute(finalSql);
                logger.info("File columns added successfully to dossiers table");
            } else {
                logger.info("All file columns already exist in dossiers table");
            }
        } catch (Exception e) {
            logger.severe("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private boolean columnExists(String tableName, String columnName) {
        try {
            String sql = "SHOW COLUMNS FROM " + tableName + " LIKE ?";
            return jdbcTemplate.queryForList(sql, columnName).size() > 0;
        } catch (Exception e) {
            // If table doesn't exist or other error, assume column doesn't exist
            return false;
        }
    }
}

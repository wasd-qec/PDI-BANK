package config;

/**
 * Centralized database configuration
 * Single Responsibility: Only handles database configuration
 */
public final class DatabaseConfig {
    private static final String DB_URL = "jdbc:sqlite:mydatabase.db";
    
    private DatabaseConfig() {
        // Prevent instantiation
    }
    
    public static String getDbUrl() {
        return DB_URL;
    }
}

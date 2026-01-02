package Config;

public class DatabaseConfig {
    private static final String DB_URL = "jdbc:sqlite:Asset/bank.db";
    
    private DatabaseConfig() {}
    public static String getDbUrl() {
        return DB_URL;
    }
}

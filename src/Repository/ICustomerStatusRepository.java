package Repository;

/**
 * Interface Segregation Principle: Separate interface for status operations
 */
public interface ICustomerStatusRepository {
    void activate(String accNo);
    void deactivate(String accNo);
    boolean isActive(String accNo);
}

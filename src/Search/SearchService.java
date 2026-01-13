package Search;

import model.Customer;
import model.Transaction;
import repository.CustomerRepository;
import repository.TransactionRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling search and filter operations
 * Single Responsibility: Handles all search and filter logic
 */
public class SearchService {
    
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    
    public SearchService(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }
    
    // ==================== CUSTOMER SEARCH METHODS ====================
    
    /**
     * Search for a customer by specific ID
     * @param id the customer ID to search for
     * @return the Customer if found, null otherwise
     */
    public Customer searchCustomerById(String id) {
        return customerRepository.findById(id);
    }
    
    /**
     * Search for a customer by account number
     * @param accNo the account number to search for
     * @return the Customer if found, null otherwise
     */
    public Customer searchCustomerByAccNo(String accNo) {
        return customerRepository.findByAccNo(accNo);
    }
    
    /**
     * Search customers using comprehensive criteria
     * @param criteria the search/filter criteria
     * @return list of matching customers
     */
    public List<Customer> searchCustomers(CustomerSearchCriteria criteria) {
        List<Customer> customers = customerRepository.findAll();
        
        return customers.stream()
            .filter(c -> matchesCustomerSearchCriteria(c, criteria))
            .filter(c -> matchesCustomerFilterCriteria(c, criteria))
            .collect(Collectors.toList());
    }
    
    /**
     * Quick search for customers by any field (AccNo, ID, Name, or Phone)
     * @param searchTerm the term to search for
     * @return list of matching customers
     */
    public List<Customer> quickSearchCustomers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return customerRepository.findAll();
        }
        
        String term = searchTerm.toLowerCase().trim();
        List<Customer> customers = customerRepository.findAll();
        
        return customers.stream()
            .filter(c -> 
                (c.getAccNo() != null && c.getAccNo().toLowerCase().contains(term)) ||
                (c.getId() != null && c.getId().toLowerCase().contains(term)) ||
                (c.getName() != null && c.getName().toLowerCase().contains(term)) ||
                String.valueOf(c.getPhoneNumber()).contains(term)
            )
            .collect(Collectors.toList());
    }
    
    /**
     * Filter customers by various criteria
     * @param criteria the filter criteria
     * @return list of filtered customers
     */
    public List<Customer> filterCustomers(CustomerSearchCriteria criteria) {
        List<Customer> customers = customerRepository.findAll();
        
        return customers.stream()
            .filter(c -> matchesCustomerFilterCriteria(c, criteria))
            .collect(Collectors.toList());
    }
    
    private boolean matchesCustomerSearchCriteria(Customer customer, CustomerSearchCriteria criteria) {
        if (!criteria.hasSearchTerms()) {
            return true;
        }
        
        boolean matches = true;
        
        if (criteria.getAccNo() != null && !criteria.getAccNo().isEmpty()) {
            matches = matches && customer.getAccNo() != null && 
                      customer.getAccNo().toLowerCase().contains(criteria.getAccNo().toLowerCase());
        }
        
        if (criteria.getId() != null && !criteria.getId().isEmpty()) {
            matches = matches && customer.getId() != null && 
                      customer.getId().toLowerCase().contains(criteria.getId().toLowerCase());
        }
        
        if (criteria.getName() != null && !criteria.getName().isEmpty()) {
            matches = matches && customer.getName() != null && 
                      customer.getName().toLowerCase().contains(criteria.getName().toLowerCase());
        }
        
        if (criteria.getPhoneNumber() != null && !criteria.getPhoneNumber().isEmpty()) {
            matches = matches && String.valueOf(customer.getPhoneNumber()).contains(criteria.getPhoneNumber());
        }
        
        return matches;
    }
    
    private boolean matchesCustomerFilterCriteria(Customer customer, CustomerSearchCriteria criteria) {
        if (!criteria.hasFilters()) {
            return true;
        }
        
        // Filter by name
        if (criteria.getNameFilter() != null && !criteria.getNameFilter().isEmpty()) {
            if (customer.getName() == null || 
                !customer.getName().toLowerCase().contains(criteria.getNameFilter().toLowerCase())) {
                return false;
            }
        }
        
        // Filter by create date range
        if (criteria.getCreateDateFrom() != null && !criteria.getCreateDateFrom().isEmpty()) {
            if (customer.getCreateDate() == null || 
                customer.getCreateDate().compareTo(criteria.getCreateDateFrom()) < 0) {
                return false;
            }
        }
        
        if (criteria.getCreateDateTo() != null && !criteria.getCreateDateTo().isEmpty()) {
            if (customer.getCreateDate() == null || 
                customer.getCreateDate().compareTo(criteria.getCreateDateTo()) > 0) {
                return false;
            }
        }
        
        // Filter by balance range
        if (criteria.getMinBalance() != null) {
            if (customer.getBalance() < criteria.getMinBalance()) {
                return false;
            }
        }
        
        if (criteria.getMaxBalance() != null) {
            if (customer.getBalance() > criteria.getMaxBalance()) {
                return false;
            }
        }
        
        // Filter by active status
        if (criteria.getActive() != null) {
            if (customer.isActive() != criteria.getActive()) {
                return false;
            }
        }
        
        // Filter by address
        if (criteria.getAddressFilter() != null && !criteria.getAddressFilter().isEmpty()) {
            if (customer.getAddress() == null || 
                !customer.getAddress().toLowerCase().contains(criteria.getAddressFilter().toLowerCase())) {
                return false;
            }
        }
        
        return true;
    }
    
    // ==================== TRANSACTION SEARCH METHODS ====================
    
    /**
     * Search for a transaction by ID
     * @param transactionId the transaction ID to search for
     * @return the Transaction if found, null otherwise
     */
    public Transaction searchTransactionById(String transactionId) {
        return transactionRepository.findById(transactionId);
    }
    
    /**
     * Search transactions using comprehensive criteria
     * @param criteria the search/filter criteria
     * @return list of matching transactions
     */
    public List<Transaction> searchTransactions(TransactionSearchCriteria criteria) {
        List<Transaction> transactions = transactionRepository.findAll();
        
        return transactions.stream()
            .filter(t -> matchesTransactionSearchCriteria(t, criteria))
            .filter(t -> matchesTransactionFilterCriteria(t, criteria))
            .collect(Collectors.toList());
    }
    
    /**
     * Quick search for transactions by ID
     * @param transactionId the transaction ID to search for
     * @return list of matching transactions
     */
    public List<Transaction> quickSearchTransactions(String transactionId) {
        if (transactionId == null || transactionId.trim().isEmpty()) {
            return transactionRepository.findAll();
        }
        
        String term = transactionId.toLowerCase().trim();
        List<Transaction> transactions = transactionRepository.findAll();
        
        return transactions.stream()
            .filter(t -> t.getTransactionId() != null && 
                        t.getTransactionId().toLowerCase().contains(term))
            .collect(Collectors.toList());
    }
    
    /**
     * Filter transactions by various criteria
     * @param criteria the filter criteria
     * @return list of filtered transactions
     */
    public List<Transaction> filterTransactions(TransactionSearchCriteria criteria) {
        List<Transaction> transactions = transactionRepository.findAll();
        
        return transactions.stream()
            .filter(t -> matchesTransactionFilterCriteria(t, criteria))
            .collect(Collectors.toList());
    }
    
    /**
     * Get all transactions for a specific customer (as sender or receiver)
     * @param customerId the customer ID
     * @return list of transactions involving the customer
     */
    public List<Transaction> getTransactionsByCustomer(String customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }
    
    private boolean matchesTransactionSearchCriteria(Transaction transaction, TransactionSearchCriteria criteria) {
        if (!criteria.hasSearchTerm()) {
            return true;
        }
        
        if (criteria.getTransactionId() != null && !criteria.getTransactionId().isEmpty()) {
            return transaction.getTransactionId() != null && 
                   transaction.getTransactionId().toLowerCase().contains(criteria.getTransactionId().toLowerCase());
        }
        
        return true;
    }
    
    private boolean matchesTransactionFilterCriteria(Transaction transaction, TransactionSearchCriteria criteria) {
        if (!criteria.hasFilters()) {
            return true;
        }
        
        // Filter by amount range
        if (criteria.getMinAmount() != null) {
            if (transaction.getAmount() < criteria.getMinAmount()) {
                return false;
            }
        }
        
        if (criteria.getMaxAmount() != null) {
            if (transaction.getAmount() > criteria.getMaxAmount()) {
                return false;
            }
        }
        
        // Filter by type
        if (criteria.getType() != null && !criteria.getType().isEmpty()) {
            if (!transaction.getType().equalsIgnoreCase(criteria.getType())) {
                return false;
            }
        }
        
        // Filter by receiver ID
        if (criteria.getReceiverId() != null && !criteria.getReceiverId().isEmpty()) {
            if (transaction.getReceiverId() == null || 
                !transaction.getReceiverId().toLowerCase().contains(criteria.getReceiverId().toLowerCase())) {
                return false;
            }
        }
        
        // Filter by sender ID
        if (criteria.getSenderId() != null && !criteria.getSenderId().isEmpty()) {
            if (transaction.getSenderId() == null || 
                !transaction.getSenderId().toLowerCase().contains(criteria.getSenderId().toLowerCase())) {
                return false;
            }
        }
        
        // Filter by date range
        if (criteria.getDateFrom() != null && !criteria.getDateFrom().isEmpty()) {
            if (transaction.getTimestamp() == null || 
                transaction.getTimestamp().compareTo(criteria.getDateFrom()) < 0) {
                return false;
            }
        }
        
        if (criteria.getDateTo() != null && !criteria.getDateTo().isEmpty()) {
            // Add time to end date if only date is provided
            String dateTo = criteria.getDateTo();
            if (dateTo.length() == 10) {  // yyyy-MM-dd format
                dateTo = dateTo + " 23:59:59";
            }
            if (transaction.getTimestamp() == null || 
                transaction.getTimestamp().compareTo(dateTo) > 0) {
                return false;
            }
        }
        
        return true;
    }
}

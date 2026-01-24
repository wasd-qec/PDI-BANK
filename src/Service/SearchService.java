package Service;

import Object.Customer;
import Object.CustomerSearchCriteria;
import Object.Transaction;
import Object.TransactionSearchCriteria;
import Database.AdminHandles;
import Database.TransactionImplement;
import Database.TransactionInterface;

import java.util.List;
import java.util.stream.Collectors;

public class SearchService {

    private final AdminHandles customerRepository;
    private final TransactionInterface transactionRepository;

    public SearchService() {
        this.customerRepository = new AdminHandles();
        this.transactionRepository = new TransactionImplement();
    }

    public SearchService(AdminHandles customerRepository, TransactionInterface transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public Customer searchCustomerById(String id) {
        // Search through all customers for matching ID
        List<Customer> customers = customerRepository.getAllCustomers();
        return customers.stream()
            .filter(c -> c.getID() != null && c.getID().equals(id))
            .findFirst()
            .orElse(null);
    }

    public Customer searchCustomerByAccNo(String accNo) {
        return customerRepository.getCustomerByAccNo(accNo);
    }

    public List<Customer> searchCustomers(CustomerSearchCriteria criteria) {
        List<Customer> customers = customerRepository.getAllCustomers();

        return customers.stream()
            .filter(c -> matchesCustomerSearchCriteria(c, criteria))
            .filter(c -> matchesCustomerFilterCriteria(c, criteria))
            .collect(Collectors.toList());
    }

    public List<Customer> quickSearchCustomers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return customerRepository.getAllCustomers();
        }

        String term = searchTerm.toLowerCase().trim();
        List<Customer> customers = customerRepository.getAllCustomers();

        return customers.stream()
            .filter(c -> 
                (c.getAccNo() != null && c.getAccNo().toLowerCase().contains(term)) ||
                (c.getID() != null && c.getID().toLowerCase().contains(term)) ||
                (c.getName() != null && c.getName().toLowerCase().contains(term)) ||
                String.valueOf(c.getPhoneNumber()).contains(term)
            )
            .collect(Collectors.toList());
    }

    public List<Customer> filterCustomers(CustomerSearchCriteria criteria) {
        List<Customer> customers = customerRepository.getAllCustomers();

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
            matches = matches && customer.getID() != null && 
                      customer.getID().toLowerCase().contains(criteria.getId().toLowerCase());
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

        if (criteria.getNameFilter() != null && !criteria.getNameFilter().isEmpty()) {
            if (customer.getName() == null || 
                !customer.getName().toLowerCase().contains(criteria.getNameFilter().toLowerCase())) {
                return false;
            }
        }

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

        if (criteria.getActive() != null) {
            if (customer.isActive() != criteria.getActive()) {
                return false;
            }
        }

        if (criteria.getAddressFilter() != null && !criteria.getAddressFilter().isEmpty()) {
            if (customer.getAddress() == null || 
                !customer.getAddress().toLowerCase().contains(criteria.getAddressFilter().toLowerCase())) {
                return false;
            }
        }

        return true;
    }

    public Transaction searchTransactionById(String transactionId) {
        List<Transaction> transactions = transactionRepository.ShowAllTransaction();
        return transactions.stream()
            .filter(t -> t.getTransactionID() != null && t.getTransactionID().equals(transactionId))
            .findFirst()
            .orElse(null);
    }

    public List<Transaction> searchTransactions(TransactionSearchCriteria criteria) {
        List<Transaction> transactions = transactionRepository.ShowAllTransaction();

        return transactions.stream()
            .filter(t -> matchesTransactionSearchCriteria(t, criteria))
            .filter(t -> matchesTransactionFilterCriteria(t, criteria))
            .collect(Collectors.toList());
    }

    public List<Transaction> quickSearchTransactions(String transactionId) {
        if (transactionId == null || transactionId.trim().isEmpty()) {
            return transactionRepository.ShowAllTransaction();
        }

        String term = transactionId.toLowerCase().trim();
        List<Transaction> transactions = transactionRepository.ShowAllTransaction();

        return transactions.stream()
            .filter(t -> t.getTransactionID() != null && 
                        t.getTransactionID().toLowerCase().contains(term))
            .collect(Collectors.toList());
    }

    public List<Transaction> filterTransactions(TransactionSearchCriteria criteria) {
        List<Transaction> transactions = transactionRepository.ShowAllTransaction();

        return transactions.stream()
            .filter(t -> matchesTransactionFilterCriteria(t, criteria))
            .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByCustomer(Customer customer) {
        return transactionRepository.GetTransactionByCustomer(customer);
    }

    private boolean matchesTransactionSearchCriteria(Transaction transaction, TransactionSearchCriteria criteria) {
        if (!criteria.hasSearchTerm()) {
            return true;
        }

        if (criteria.getTransactionId() != null && !criteria.getTransactionId().isEmpty()) {
            return transaction.getTransactionID() != null && 
                   transaction.getTransactionID().toLowerCase().contains(criteria.getTransactionId().toLowerCase());
        }

        return true;
    }

    private boolean matchesTransactionFilterCriteria(Transaction transaction, TransactionSearchCriteria criteria) {
        if (!criteria.hasFilters()) {
            return true;
        }

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

        if (criteria.getType() != null && !criteria.getType().isEmpty()) {
            if (!transaction.getType().equalsIgnoreCase(criteria.getType())) {
                return false;
            }
        }

        if (criteria.getReceiverId() != null && !criteria.getReceiverId().isEmpty()) {
            if (transaction.getReceiverID() == null || 
                !transaction.getReceiverID().toLowerCase().contains(criteria.getReceiverId().toLowerCase())) {
                return false;
            }
        }

        if (criteria.getSenderId() != null && !criteria.getSenderId().isEmpty()) {
            if (transaction.getSenderID() == null || 
                !transaction.getSenderID().toLowerCase().contains(criteria.getSenderId().toLowerCase())) {
                return false;
            }
        }

        if (criteria.getDateFrom() != null && !criteria.getDateFrom().isEmpty()) {
            if (transaction.getTimestamp() == null || 
                transaction.getTimestamp().compareTo(criteria.getDateFrom()) < 0) {
                return false;
            }
        }

        if (criteria.getDateTo() != null && !criteria.getDateTo().isEmpty()) {
            String dateTo = criteria.getDateTo();
            if (dateTo.length() == 10) {
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

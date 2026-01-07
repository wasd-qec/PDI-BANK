package web;

import com.sun.net.httpserver.HttpExchange;
import model.Customer;
import model.Transaction;
import service.CustomerService;
import service.TransactionService;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Controller for transaction-related API endpoints
 */
public class TransactionController extends BaseController {
    
    private static final String BASE_PATH = "/api/transactions";
    private final TransactionService transactionService;
    private final CustomerService customerService;
    
    public TransactionController(TransactionService transactionService, CustomerService customerService) {
        this.transactionService = transactionService;
        this.customerService = customerService;
    }
    
    @Override
    protected void handleGet(HttpExchange exchange, String path) throws IOException {
        String segment = getPathSegment(path, BASE_PATH);
        
        if (segment.isEmpty() || "all".equals(segment)) {
            // GET /api/transactions or /api/transactions/all - Get all transactions
            getAllTransactions(exchange);
        } else if (segment.startsWith("customer/")) {
            // GET /api/transactions/customer/{customerId} - Get customer transactions
            String customerId = segment.substring(9);
            getCustomerTransactions(exchange, customerId);
        } else if ("search".equals(segment)) {
            // GET /api/transactions/search?term=xxx - Search transactions
            Map<String, String> params = parseQueryParams(exchange);
            String searchTerm = params.getOrDefault("term", "");
            if (!searchTerm.isEmpty()) {
                searchTerm = URLDecoder.decode(searchTerm, StandardCharsets.UTF_8);
            }
            searchTransactions(exchange, searchTerm);
        } else {
            sendError(exchange, 404, "Endpoint not found");
        }
    }
    
    @Override
    protected void handlePost(HttpExchange exchange, String path) throws IOException {
        String segment = getPathSegment(path, BASE_PATH);
        
        switch (segment) {
            case "withdraw" -> handleWithdraw(exchange);
            case "deposit" -> handleDeposit(exchange);
            case "transfer" -> handleTransfer(exchange);
            default -> sendError(exchange, 404, "Endpoint not found");
        }
    }
    
    private void getAllTransactions(HttpExchange exchange) throws IOException {
        List<Transaction> transactions = transactionService.getTransactionHistory();
        String json = transactionsToJson(transactions);
        sendSuccess(exchange, json);
    }
    
    private void getCustomerTransactions(HttpExchange exchange, String customerId) throws IOException {
        List<Transaction> transactions = transactionService.getCustomerTransactions(customerId);
        String json = transactionsToJson(transactions);
        sendSuccess(exchange, json);
    }
    
    private void searchTransactions(HttpExchange exchange, String searchTerm) throws IOException {
        List<Transaction> transactions = transactionService.searchTransactions(searchTerm);
        String json = transactionsToJson(transactions);
        sendSuccess(exchange, json);
    }
    
    private void handleWithdraw(HttpExchange exchange) throws IOException {
        Map<String, String> body = parseJsonBody(exchange);
        String customerId = body.get("customerId");
        String amountStr = body.get("amount");
        
        if (customerId == null || amountStr == null) {
            sendError(exchange, 400, "Customer ID and amount required");
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountStr);
            
            // Find customer by ID
            Customer customer = findCustomerById(customerId);
            if (customer == null) {
                sendError(exchange, 404, "Customer not found");
                return;
            }
            
            if (customer.getBalance() < amount) {
                sendError(exchange, 400, "Insufficient funds");
                return;
            }
            
            Transaction transaction = transactionService.processWithdrawal(customer, amount);
            
            if (transaction != null) {
                String json = String.format(
                    "{\"success\": true, \"transaction\": %s, \"newBalance\": %.2f}",
                    transactionToJson(transaction),
                    customer.getBalance()
                );
                sendSuccess(exchange, json);
            } else {
                sendError(exchange, 400, "Withdrawal failed");
            }
        } catch (NumberFormatException e) {
            sendError(exchange, 400, "Invalid amount format");
        }
    }
    
    private void handleDeposit(HttpExchange exchange) throws IOException {
        Map<String, String> body = parseJsonBody(exchange);
        String customerId = body.get("customerId");
        String amountStr = body.get("amount");
        
        if (customerId == null || amountStr == null) {
            sendError(exchange, 400, "Customer ID and amount required");
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountStr);
            
            // Find customer by ID
            Customer customer = findCustomerById(customerId);
            if (customer == null) {
                sendError(exchange, 404, "Customer not found");
                return;
            }
            
            Transaction transaction = transactionService.processDeposit(customer, amount);
            
            if (transaction != null) {
                String json = String.format(
                    "{\"success\": true, \"transaction\": %s, \"newBalance\": %.2f}",
                    transactionToJson(transaction),
                    customer.getBalance()
                );
                sendSuccess(exchange, json);
            } else {
                sendError(exchange, 400, "Deposit failed");
            }
        } catch (NumberFormatException e) {
            sendError(exchange, 400, "Invalid amount format");
        }
    }
    
    private void handleTransfer(HttpExchange exchange) throws IOException {
        Map<String, String> body = parseJsonBody(exchange);
        String senderId = body.get("senderId");
        String receiverAccNo = body.get("receiverAccNo");
        String amountStr = body.get("amount");
        
        if (senderId == null || receiverAccNo == null || amountStr == null) {
            sendError(exchange, 400, "Sender ID, receiver account number, and amount required");
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountStr);
            
            // Find sender by ID
            Customer sender = findCustomerById(senderId);
            if (sender == null) {
                sendError(exchange, 404, "Sender not found");
                return;
            }
            
            // Find receiver by account number
            Customer receiver = customerService.findByAccNo(receiverAccNo);
            if (receiver == null) {
                sendError(exchange, 404, "Recipient account not found");
                return;
            }
            
            if (sender.getBalance() < amount) {
                sendError(exchange, 400, "Insufficient funds");
                return;
            }
            
            Transaction transaction = transactionService.processTransfer(sender, receiver, amount);
            
            if (transaction != null) {
                String json = String.format(
                    "{\"success\": true, \"transaction\": %s, \"newBalance\": %.2f}",
                    transactionToJson(transaction),
                    sender.getBalance()
                );
                sendSuccess(exchange, json);
            } else {
                sendError(exchange, 400, "Transfer failed");
            }
        } catch (NumberFormatException e) {
            sendError(exchange, 400, "Invalid amount format");
        }
    }
    
    private Customer findCustomerById(String customerId) {
        // Search all customers to find by ID
        List<Customer> allCustomers = customerService.getAllCustomers();
        for (Customer c : allCustomers) {
            if (c.getId().equals(customerId)) {
                return c;
            }
        }
        return null;
    }
    
    private String transactionToJson(Transaction t) {
        return String.format(
            "{\"transactionId\": \"%s\", \"receiverId\": \"%s\", \"senderId\": \"%s\", " +
            "\"amount\": %.2f, \"timestamp\": \"%s\", \"type\": \"%s\"}",
            escapeJson(t.getTransactionId()),
            escapeJson(t.getReceiverId()),
            escapeJson(t.getSenderId()),
            t.getAmount(),
            escapeJson(t.getTimestamp()),
            escapeJson(t.getType())
        );
    }
    
    private String transactionsToJson(List<Transaction> transactions) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < transactions.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(transactionToJson(transactions.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }
}

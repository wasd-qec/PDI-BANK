package web;

import com.sun.net.httpserver.HttpExchange;
import model.Customer;
import service.CustomerService;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Controller for customer-related API endpoints
 */
public class CustomerController extends BaseController {
    
    private static final String BASE_PATH = "/api/customer";
    private final CustomerService customerService;
    
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    @Override
    protected void handleGet(HttpExchange exchange, String path) throws IOException {
        String segment = getPathSegment(path, BASE_PATH);
        
        if (segment.isEmpty() || "all".equals(segment)) {
            // GET /api/customer or /api/customer/all - Get all customers
            getAllCustomers(exchange);
        } else if (segment.startsWith("find/")) {
            // GET /api/customer/find/{accNo} - Find customer by account number
            String accNo = segment.substring(5);
            findCustomerByAccNo(exchange, accNo);
        } else if ("search".equals(segment)) {
            // GET /api/customer/search?term=xxx - Search customers
            Map<String, String> params = parseQueryParams(exchange);
            String searchTerm = params.getOrDefault("term", "");
            if (!searchTerm.isEmpty()) {
                searchTerm = URLDecoder.decode(searchTerm, StandardCharsets.UTF_8);
            }
            searchCustomers(exchange, searchTerm);
        } else {
            sendError(exchange, 404, "Endpoint not found");
        }
    }
    
    @Override
    protected void handlePost(HttpExchange exchange, String path) throws IOException {
        String segment = getPathSegment(path, BASE_PATH);
        
        if ("login".equals(segment)) {
            // POST /api/customer/login - Customer login
            handleLogin(exchange);
        } else if ("create".equals(segment)) {
            // POST /api/customer/create - Create new customer
            handleCreate(exchange);
        } else {
            sendError(exchange, 404, "Endpoint not found");
        }
    }
    
    private void getAllCustomers(HttpExchange exchange) throws IOException {
        List<Customer> customers = customerService.getAllCustomers();
        String json = customersToJson(customers);
        sendSuccess(exchange, json);
    }
    
    private void findCustomerByAccNo(HttpExchange exchange, String accNo) throws IOException {
        Customer customer = customerService.findByAccNo(accNo);
        
        if (customer != null) {
            String json = String.format("{\"success\": true, \"customer\": %s}", customerToJson(customer));
            sendSuccess(exchange, json);
        } else {
            sendError(exchange, 404, "Customer not found");
        }
    }
    
    private void searchCustomers(HttpExchange exchange, String searchTerm) throws IOException {
        List<Customer> customers = customerService.searchCustomers(searchTerm);
        String json = customersToJson(customers);
        sendSuccess(exchange, json);
    }
    
    private void handleLogin(HttpExchange exchange) throws IOException {
        Map<String, String> body = parseJsonBody(exchange);
        String accNo = body.get("accNo");
        String password = body.get("password");
        
        if (accNo == null || password == null) {
            sendError(exchange, 400, "Account number and password required");
            return;
        }
        
        Customer customer = customerService.authenticate(accNo, password);
        
        if (customer != null) {
            String json = String.format("{\"success\": true, \"customer\": %s}", customerToJson(customer));
            sendSuccess(exchange, json);
        } else {
            sendJson(exchange, 401, "{\"success\": false, \"message\": \"Invalid credentials\"}");
        }
    }
    
    private void handleCreate(HttpExchange exchange) throws IOException {
        Map<String, String> body = parseJsonBody(exchange);
        
        String name = body.get("name");
        String password = body.get("password");
        String phoneStr = body.getOrDefault("phone", "0");
        String address = body.getOrDefault("address", "");
        String birthDate = body.getOrDefault("birthDate", "");
        String balanceStr = body.getOrDefault("balance", "0");
        
        if (name == null || name.isEmpty() || password == null || password.isEmpty()) {
            sendError(exchange, 400, "Name and password are required");
            return;
        }
        
        try {
            long phone = Long.parseLong(phoneStr);
            double balance = Double.parseDouble(balanceStr);
            
            Customer customer = customerService.createAccount(name, password, phone, address, birthDate, balance);
            
            String json = String.format("{\"success\": true, \"customer\": %s}", customerToJson(customer));
            sendSuccess(exchange, json);
        } catch (NumberFormatException e) {
            sendError(exchange, 400, "Invalid number format for phone or balance");
        }
    }
    
    private String customerToJson(Customer c) {
        return String.format(
            "{\"id\": \"%s\", \"accNo\": \"%s\", \"name\": \"%s\", \"balance\": %.2f, " +
            "\"phoneNumber\": %d, \"address\": \"%s\", \"birthDate\": \"%s\", " +
            "\"createDate\": \"%s\", \"active\": %b}",
            escapeJson(c.getId()),
            escapeJson(c.getAccNo()),
            escapeJson(c.getName()),
            c.getBalance(),
            c.getPhoneNumber(),
            escapeJson(c.getAddress()),
            escapeJson(c.getBirthDate()),
            escapeJson(c.getCreateDate()),
            c.isActive()
        );
    }
    
    private String customersToJson(List<Customer> customers) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < customers.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(customerToJson(customers.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }
}

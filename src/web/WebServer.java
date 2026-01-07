package web;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import repository.*;
import security.PasswordEncryption;
import service.AdminService;
import service.CustomerService;
import service.TransactionService;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Simple HTTP Web Server for ABA Banking System
 * Serves static files and handles API requests
 */
public class WebServer {
    
    private static final int PORT = 8080;
    private static final String WEB_ROOT = "web";
    
    private final HttpServer server;
    private final CustomerService customerService;
    private final TransactionService transactionService;
    private final AdminService adminService;
    
    public WebServer() throws IOException {
        // Initialize dependencies
        PasswordEncryption passwordEncryption = new PasswordEncryption();
        CustomerRepository customerRepository = new CustomerRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();
        AdminRepository adminRepository = new AdminRepositoryImpl(passwordEncryption);
        
        this.customerService = new CustomerService(customerRepository, passwordEncryption);
        this.transactionService = new TransactionService(transactionRepository, customerRepository);
        this.adminService = new AdminService(adminRepository);
        
        // Initialize admin system
        adminService.initialize();
        
        // Create HTTP server
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Register handlers
        registerHandlers();
        
        server.setExecutor(null);
    }
    
    private void registerHandlers() {
        // API Controllers
        CustomerController customerController = new CustomerController(customerService);
        TransactionController transactionController = new TransactionController(transactionService, customerService);
        AdminController adminController = new AdminController(adminService);
        
        // API routes
        server.createContext("/api/customer", customerController);
        server.createContext("/api/transactions", transactionController);
        server.createContext("/api/admin", adminController);
        
        // Static file handler
        server.createContext("/", this::handleStaticFile);
    }
    
    private void handleStaticFile(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        
        // Add CORS headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(200, -1);
            return;
        }
        
        // Default to index.html
        if ("/".equals(path)) {
            path = "/index.html";
        }
        
        // Build file path
        Path filePath = Paths.get(WEB_ROOT + path);
        
        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            // Determine content type
            String contentType = getContentType(path);
            exchange.getResponseHeaders().set("Content-Type", contentType);
            
            byte[] fileBytes = Files.readAllBytes(filePath);
            exchange.sendResponseHeaders(200, fileBytes.length);
            
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(fileBytes);
            }
        } else {
            // 404 Not Found
            String response = "404 - Not Found";
            exchange.sendResponseHeaders(404, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
    
    private String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif")) return "image/gif";
        if (path.endsWith(".svg")) return "image/svg+xml";
        if (path.endsWith(".ico")) return "image/x-icon";
        return "text/plain";
    }
    
    public void start() {
        server.start();
        System.out.println("========================================");
        System.out.println("  ABA Banking System - Web Server");
        System.out.println("========================================");
        System.out.println("  Server started on port " + PORT);
        System.out.println("  Open http://localhost:" + PORT + " in your browser");
        System.out.println("========================================");
    }
    
    public void stop() {
        server.stop(0);
        System.out.println("Server stopped.");
    }
    
    public static void main(String[] args) {
        try {
            WebServer webServer = new WebServer();
            webServer.start();
            
            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(webServer::stop));
            
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

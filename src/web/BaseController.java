package web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Base controller with common HTTP handling utilities
 */
public abstract class BaseController implements HttpHandler {
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Add CORS headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        
        // Handle preflight requests
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(200, -1);
            return;
        }
        
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            
            switch (method) {
                case "GET" -> handleGet(exchange, path);
                case "POST" -> handlePost(exchange, path);
                case "PUT" -> handlePut(exchange, path);
                case "DELETE" -> handleDelete(exchange, path);
                default -> sendError(exchange, 405, "Method not allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }
    
    protected void handleGet(HttpExchange exchange, String path) throws IOException {
        sendError(exchange, 404, "Not found");
    }
    
    protected void handlePost(HttpExchange exchange, String path) throws IOException {
        sendError(exchange, 404, "Not found");
    }
    
    protected void handlePut(HttpExchange exchange, String path) throws IOException {
        sendError(exchange, 404, "Not found");
    }
    
    protected void handleDelete(HttpExchange exchange, String path) throws IOException {
        sendError(exchange, 404, "Not found");
    }
    
    /**
     * Read request body as string
     */
    protected String readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
    
    /**
     * Parse JSON body to Map (simple JSON parsing)
     */
    protected Map<String, String> parseJsonBody(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        return parseSimpleJson(body);
    }
    
    /**
     * Simple JSON parser for flat objects
     */
    protected Map<String, String> parseSimpleJson(String json) {
        Map<String, String> result = new HashMap<>();
        
        if (json == null || json.isEmpty()) return result;
        
        // Remove braces and whitespace
        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);
        
        // Split by comma (simple parsing)
        String[] pairs = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim().replace("\"", "");
                result.put(key, value);
            }
        }
        
        return result;
    }
    
    /**
     * Parse query parameters from URL
     */
    protected Map<String, String> parseQueryParams(HttpExchange exchange) {
        Map<String, String> params = new HashMap<>();
        String query = exchange.getRequestURI().getQuery();
        
        if (query != null && !query.isEmpty()) {
            for (String param : query.split("&")) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
        
        return params;
    }
    
    /**
     * Extract path segment after base path
     * e.g., /api/customer/find/ACC123 with base /api/customer -> find/ACC123
     */
    protected String getPathSegment(String fullPath, String basePath) {
        if (fullPath.startsWith(basePath)) {
            String segment = fullPath.substring(basePath.length());
            if (segment.startsWith("/")) {
                segment = segment.substring(1);
            }
            return segment;
        }
        return "";
    }
    
    /**
     * Send JSON response
     */
    protected void sendJson(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] responseBytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
    
    /**
     * Send success response
     */
    protected void sendSuccess(HttpExchange exchange, String json) throws IOException {
        sendJson(exchange, 200, json);
    }
    
    /**
     * Send error response
     */
    protected void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        String json = String.format("{\"success\": false, \"message\": \"%s\"}", escapeJson(message));
        sendJson(exchange, statusCode, json);
    }
    
    /**
     * Escape special characters in JSON strings
     */
    protected String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}

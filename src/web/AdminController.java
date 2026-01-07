package web;

import com.sun.net.httpserver.HttpExchange;
import service.AdminService;

import java.io.IOException;
import java.util.Map;

/**
 * Controller for admin-related API endpoints
 */
public class AdminController extends BaseController {
    
    private static final String BASE_PATH = "/api/admin";
    private final AdminService adminService;
    
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    @Override
    protected void handlePost(HttpExchange exchange, String path) throws IOException {
        String segment = getPathSegment(path, BASE_PATH);
        
        if ("login".equals(segment)) {
            // POST /api/admin/login - Admin login
            handleLogin(exchange);
        } else {
            sendError(exchange, 404, "Endpoint not found");
        }
    }
    
    private void handleLogin(HttpExchange exchange) throws IOException {
        Map<String, String> body = parseJsonBody(exchange);
        String username = body.get("username");
        String password = body.get("password");
        
        if (username == null || password == null) {
            sendError(exchange, 400, "Username and password required");
            return;
        }
        
        boolean authenticated = adminService.authenticate(username, password);
        
        if (authenticated) {
            String json = "{\"success\": true, \"message\": \"Login successful\"}";
            sendSuccess(exchange, json);
        } else {
            sendJson(exchange, 401, "{\"success\": false, \"message\": \"Invalid credentials\"}");
        }
    }
}

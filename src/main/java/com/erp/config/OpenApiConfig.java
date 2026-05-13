package com.erp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**

 * OpenAPI/Swagger Configuration for ERP System
 * 
 * Access Swagger UI at: http://localhost:8080/swagger-ui.html
 * Access API Docs JSON at: http://localhost:8080/v3/api-docs
 */
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        // Define security scheme for Bearer token authentication
        SecurityScheme bearerAuthScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .description("Enter JWT Bearer token (obtain from /api/auth/login)");
        
        // Define security requirement
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");
        
        return new OpenAPI()
                .info(new Info()
                        .title("PrintFlow ERP System API")
                        .version("1.0.0")
                        .description("""
                                # PrintFlow ERP System - Complete API Documentation
                                
                                ## Overview
                                RESTful API for managing printing business operations including:
                                - **Dashboard**: KPI metrics, alerts, and recent activity
                                - **Orders**: Order management and cost tracking
                                - **Inventory**: Stock management and adjustments
                                - **Reports**: Analytics, profit breakdown, and insights
                                - **Labor Settings**: Employee management and approval workflow
                                - **Authentication**: User login and session management
                                
                                ## Authentication
                                Most endpoints require authentication. To get started:
                                1. Use POST `/api/auth/login` with credentials: username="admin", password="admin123"
                                2. Copy the `token` from the response
                                3. Click "Authorize" button above and enter: `Bearer <your-token>`
                                4. All subsequent requests will include authentication
                                
                                ## API Modules
                                - **Dashboard APIs**: `/api/dashboard/*` - Real-time metrics and alerts
                                - **Authentication APIs**: `/api/auth/*` - Login, logout, token validation
                                - **Order APIs**: `/api/orders/*` - Order CRUD and management
                                - **Inventory APIs**: `/api/inventory/*` - Stock control and tracking
                                - **Reports APIs**: `/api/reports/*` - Business analytics and reports
                                - **Settings APIs**: `/api/settings/labor/*` - Labor master management
                                
                                ## Error Codes
                                - `200 OK` - Successful request
                                - `201 Created` - Resource created successfully
                                - `400 Bad Request` - Invalid input or validation error
                                - `401 Unauthorized` - Invalid credentials or missing token
                                - `404 Not Found` - Resource not found
                                - `500 Internal Server Error` - Server error
                                
                                ## Date Format
                                All dates use ISO 8601 format: `YYYY-MM-DD` or `YYYY-MM-DDTHH:mm:ss`
                                
                                ## CORS
                                Enabled for: `http://localhost:3000` and `http://localhost:5173`
                                """)
                        .contact(new Contact()
                                .name("ERP Support Team")
                                .email("support@printflow.com")
                                .url("https://printflow.com/support"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Testing Server (if available)"),
                        new Server()
                                .url("https://api.printflow.com")
                                .description("Production Server (when deployed)")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", bearerAuthScheme))
                .addSecurityItem(securityRequirement);
    }
}

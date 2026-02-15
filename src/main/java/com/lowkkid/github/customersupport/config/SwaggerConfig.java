package com.lowkkid.github.customersupport.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    @Profile("prod")
    public OpenAPI prodOpenAPI() {
        return createOpenAPI(List.of(
                new Server().url("https://customer-support-backend.lowkkid.dev").description("Production server")));
    }

    @Bean
    @Profile("local")
    public OpenAPI localOpenAPI() {
        return createOpenAPI(List.of(
                new Server().url("http://localhost:8080/api/v1/customer-support").description("Local server")));
    }


    private OpenAPI createOpenAPI(List<Server> servers) {
        return new OpenAPI()
                .info(new Info()
                        .title("Customer Support Backend API")
                        .version("1.0")
                        .description("API documentation for Customer Support Backend"))
                .servers(servers);
    }
}
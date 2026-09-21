package com.quadcore.Ratingup.config.security;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SwaggerConfigTest {

    @Test
    @DisplayName("Should create OpenAPI configuration")
    void testOpenAPI() {
        SwaggerConfig config = new SwaggerConfig();
        OpenAPI api = config.openAPI();
        
        assertNotNull(api);
        assertEquals(1, api.getServers().size());
        assertEquals("http://localhost/api", api.getServers().get(0).getUrl());
        assertNotNull(api.getComponents().getSecuritySchemes().get("bearerAuth"));
    }
}

package com.quadcore.Ratingup.config.db;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FlywayConfigTest {

    @Test
    @DisplayName("Should configure Flyway")
    void testFlyway() {
        FlywayConfig config = new FlywayConfig();
        DataSource dataSource = Mockito.mock(DataSource.class);
        
        Flyway flyway = config.flyway(dataSource);
        
        assertNotNull(flyway);
    }
}

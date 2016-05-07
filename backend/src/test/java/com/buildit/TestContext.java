package com.buildit;

import com.buildit.inventory.application.service.RentalService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by rain on 30.03.16.
 */

@Configuration
@Profile("test")
public class TestContext {
    @Bean
    public RentalService rentalService() {
        return Mockito.mock(RentalService.class);
    }
}


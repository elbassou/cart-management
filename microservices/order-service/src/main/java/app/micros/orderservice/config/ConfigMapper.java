package app.micros.orderservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ConfigMapper {


    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}

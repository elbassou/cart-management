package app.micros.productservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigMapper {


    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}

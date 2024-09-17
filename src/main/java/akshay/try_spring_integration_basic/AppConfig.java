package akshay.try_spring_integration_basic;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("app")
@Data
@Configuration
public class AppConfig {
    String sokey;
}

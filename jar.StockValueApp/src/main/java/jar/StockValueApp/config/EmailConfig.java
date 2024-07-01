package jar.StockValueApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:emailGreeting.yml", factory = YamlPropertySourceFactory.class)
public class EmailConfig {
}

package jar.StockValueApp.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * http://localhost:8080/swagger-ui/index.html
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info().title("Your API Documentation").version("1.0.0"));
    }

    @Bean
    public GroupedOpenApi httpApi() {
        return GroupedOpenApi.builder()
                .group("http")
                .pathsToMatch("/**")
                .build();
    }
}

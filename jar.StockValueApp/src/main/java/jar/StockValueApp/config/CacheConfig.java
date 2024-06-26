package jar.StockValueApp.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                "dcfValuationsCache",
                "dcfValuationsByTickerCache",
                "dcfValuationsByCompanyNameCache",
                "dcfValuationByDateCache",
                "dividendDiscountValuationsCache",
                "dividendDiscountValuationsByTickerCache",
                "dividendDiscountValuationsByCompanyNameCache",
                "dividendDiscountValuationsByDateCache",
                "grahamsValuationsCache",
                "grahamsValuationsByTickerCache",
                "grahamsValuationByCompanyNameCache",
                "grahamsValuationsByDateCache"
        );
    }

}
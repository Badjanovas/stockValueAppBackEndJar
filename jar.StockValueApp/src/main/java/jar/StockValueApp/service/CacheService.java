package jar.StockValueApp.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Caching(evict = {
            @CacheEvict(value = "dcfValuationsCache", allEntries = true),
            @CacheEvict(value = "dcfValuationsByTickerCache", allEntries = true),
            @CacheEvict(value = "dcfValuationsByCompanyNameCache", allEntries = true),
            @CacheEvict(value = "dcfValuationByDateCache", allEntries = true)
    })
    public void evictAllDcfValuationsCaches() {
    }

    @Caching(evict = {
            @CacheEvict(value = "dividendDiscountValuationsCache", allEntries = true),
            @CacheEvict(value = "dividendDiscountValuationsByTickerCache", allEntries = true),
            @CacheEvict(value = "dividendDiscountValuationsByCompanyNameCache", allEntries = true),
            @CacheEvict(value = "dividendDiscountValuationsByDateCache", allEntries = true)
    })
    public void evictAllDividendDiscountValuationsCaches(){
    }

    @Caching(evict = {
            @CacheEvict(value = "grahamsValuationsCache", allEntries = true),
            @CacheEvict(value = "grahamsValuationsByTickerCache", allEntries = true),
            @CacheEvict(value = "grahamsValuationByCompanyNameCache", allEntries = true),
            @CacheEvict(value = "grahamsValuationsByDateCache", allEntries = true)
    })
    public void evictAllGrahamsValuationsCaches(){
    }


}

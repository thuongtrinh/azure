package com.txt.mongoredis.services.caching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    CacheManager cacheManager;

    public void evictAllCaches() {
        cacheManager.getCacheNames().stream()
                .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

    public void evictCacheByName(String cacheName) {
        cacheManager.getCache(cacheName).clear();
    }

//    @Scheduled(fixedRateString = "${app.cacheRefreshTime}", initialDelay = 1000)
//    public void evictAllcachesAtIntervals() {
//        evictAllCaches();
//    }
}

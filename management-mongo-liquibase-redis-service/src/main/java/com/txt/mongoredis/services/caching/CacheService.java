package com.txt.mongoredis.services.caching;

public interface CacheService {

    void evictAllCaches();
    void evictCacheByName(String cacheName);
}

package com.txt.mongoredis.controller;

import com.txt.mongoredis.services.caching.CacheService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "CachingController", description = "CachingController API")
public class CachingController {

    @Autowired
    CacheService cacheService;

    @GetMapping("/clear-all-caches")
    public void clearAllCaches() {
        cacheService.evictAllCaches();
    }

    @GetMapping("/clear-cache-by-name/{cacheName}")
    public void clearCacheByName(@Parameter(name = "cacheName") @PathVariable String cacheName) {
        cacheService.evictCacheByName(cacheName);
    }

}
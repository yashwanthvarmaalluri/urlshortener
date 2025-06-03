package com.java.urlshortener.service;

import com.java.urlshortener.model.ShortUrl;
import com.java.urlshortener.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public ShortUrlService(ShortUrlRepository shortUrlRepository, RedisTemplate<String, String> redisTemplate) {
        this.shortUrlRepository = shortUrlRepository;
        this.redisTemplate = redisTemplate;
    }

    public ShortUrl createShortUrl(String originalUrl, String shortCode) {
        System.out.println("Received original URL: " + originalUrl);

        Optional<ShortUrl> existing = shortUrlRepository.findByOriginalUrl(originalUrl);
        if (existing.isPresent()) {
            System.out.println("Original URL already exists. Returning existing entry.");
            return existing.get(); // Return the existing ShortUrl object
        }

        ShortUrl url = new ShortUrl(shortCode, originalUrl, 0L);
        shortUrlRepository.save(url);
        redisTemplate.opsForValue().set(shortCode, originalUrl, 1, TimeUnit.DAYS);
        return url;
    }

    public Optional<String> getOriginalUrl(String shortCode) {
        // 1. Try Redis first
        System.out.println("Checking for short code: " + shortCode);
        String cachedUrl = redisTemplate.opsForValue().get(shortCode);
        if (cachedUrl != null) {
            System.out.println("Found in Redis");
            return Optional.of(cachedUrl);
        }

        System.out.println("Not found in Redis, checking MongoDB...");
        // 2. Fallback to MongoDB
        Optional<ShortUrl> url = shortUrlRepository.findById(shortCode);
        url.ifPresent(u -> redisTemplate.opsForValue().set(shortCode, u.getOriginalUrl(), 1, TimeUnit.DAYS));
        return url.map(ShortUrl::getOriginalUrl);
    }
}
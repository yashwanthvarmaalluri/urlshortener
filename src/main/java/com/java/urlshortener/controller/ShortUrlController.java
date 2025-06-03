package com.java.urlshortener.controller;

import com.java.urlshortener.model.ShortUrl;
import com.java.urlshortener.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    @Autowired
    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> createShortUrl(@RequestBody String originalUrl) {
        String shortCode = UUID.randomUUID().toString().substring(0, 6); // simple 6-char code
        ShortUrl shortUrl = shortUrlService.createShortUrl(originalUrl, shortCode);
        return ResponseEntity.ok("http://localhost:8080/api/" + shortUrl.getId());
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortCode) {
        Optional<String> originalUrl = shortUrlService.getOriginalUrl(shortCode);
        return originalUrl
                .map(url -> ResponseEntity.status(302).header("Location", url).build())
                .orElse(ResponseEntity.notFound().build());
    }
}
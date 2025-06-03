package com.java.urlshortener.repository;

import com.java.urlshortener.model.ShortUrl;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ShortUrlRepository extends MongoRepository<ShortUrl, String> {

    Optional<ShortUrl> findByOriginalUrl(String originalUrl);
}
package com.java.urlshortener.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "short_urls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortUrl {
    @Id
    private String id;  // short code (e.g. "abc123")

    private String originalUrl;

    private Long usageCount;
}

package com.java.urlshortener.controller;

import com.java.urlshortener.model.ShortUrl;
import com.java.urlshortener.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class WebController {

    @Autowired
    private ShortUrlService shortUrlService;

    @GetMapping("/")
    public String showForm() {
        return "index";
    }

    @PostMapping("/shorten")
    public String handleForm(@RequestParam String originalUrl, Model model) {
        String shortCode = UUID.randomUUID().toString().substring(0, 6);
        ShortUrl shortUrl = shortUrlService.createShortUrl(originalUrl, shortCode);
        model.addAttribute("shortUrl", shortUrl);
        return "index";
    }
}
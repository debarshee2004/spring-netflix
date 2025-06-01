package com.netflix.spring_lolomo_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ArtworkService {

    private final Logger LOGGER = LoggerFactory.getLogger(ArtworkService.class);

    public String generateArtwork(String title) {
        LOGGER.info("Generating Artwork for title: {}", title);

        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return UUID.randomUUID() + "-" + title.toLowerCase()
                .replaceAll(" ", "-") + ".jpg";
    }

}

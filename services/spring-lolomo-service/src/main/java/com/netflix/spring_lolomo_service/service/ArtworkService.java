package com.netflix.spring_lolomo_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ArtworkService {

    private final Logger LOGGER = LoggerFactory.getLogger(ArtworkService.class);

    public Map<String, String> batchGenerator(Set<String> titles) {
        LOGGER.info("Generating Artworks for {}", titles);

        Map<String, String> result = new HashMap<>();
        titles.forEach(t -> result.put(t, generateArtwork(t)));

        return result;
    }

    public String generateArtwork(String title) {
        LOGGER.info("Generating Artwork for title: {}", title);

        return UUID.randomUUID() + "-" + title.toLowerCase()
                .replaceAll(" ", "-") + ".jpg";
    }

}

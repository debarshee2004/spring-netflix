package com.netflix.spring_lolomo_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Service responsible for generating artwork URLs for shows.
 * <p>
 * This service provides both individual and batch artwork URL generation
 * capabilities. In a real-world scenario, this would likely interface with
 * an external artwork storage service or CDN, but for demonstration purposes,
 * it generates mock artwork URLs with a consistent naming pattern.
 */
@Service // Spring stereotype annotation marking this as a service component
public class ArtworkService {

    /**
     * Logger instance for tracking artwork generation operations
     */
    private final Logger LOGGER = LoggerFactory.getLogger(ArtworkService.class);

    /**
     * Generates artwork URLs for multiple show titles in a single batch operation.
     * <p>
     * This method is optimized for use with DataLoader pattern to efficiently
     * handle multiple artwork requests simultaneously. It processes all provided
     * titles and returns a complete mapping of titles to their artwork URLs.
     *
     * @param titles Set of show titles for which artwork URLs need to be generated
     * @return Map containing title-to-artwork-URL mappings for all input titles
     */
    public Map<String, String> batchGenerator(Set<String> titles) {
        // Log the batch generation request for monitoring and debugging
        LOGGER.info("Generating Artworks for {}", titles);

        // Initialize result map to store title -> artwork URL mappings
        Map<String, String> result = new HashMap<>();

        // Process each title and generate its corresponding artwork URL
        titles.forEach(title ->
                result.put(title, generateArtwork(title))
        );

        return result;
    }

    /**
     * Generates a mock artwork URL for a single show title.
     * <p>
     * Creates a consistent, predictable artwork URL format using:
     * - A random UUID for uniqueness
     * - The show title converted to a URL-friendly format
     * - A .jpg file extension
     * <p>
     * Format: {UUID}-{normalized-title}.jpg
     * Example: "abc123-the-office.jpg" for title "The Office"
     *
     * @param title The show title for which to generate an artwork URL
     * @return Generated artwork URL string
     */
    public String generateArtwork(String title) {
        // Log individual artwork generation for detailed tracing
        LOGGER.info("Generating Artwork for title: {}", title);

        // Generate artwork URL with the following components:
        // 1. Random UUID for uniqueness and cache-busting
        // 2. Hyphen separator
        // 3. Title converted to lowercase and spaces replaced with hyphens
        // 4. .jpg file extension
        return UUID.randomUUID() + "-" + title.toLowerCase()
                .replaceAll(" ", "-") + ".jpg";
    }
}
package com.netflix.spring_lolomo_service.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.spring_lolomo_service.codegen.types.Show;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Repository component for managing show data access and categorization.
 * <p>
 * This repository loads show data from a JSON file on application startup
 * and provides various methods to query and filter shows by category, ID,
 * or retrieve all shows. It serves as the primary data access layer for
 * the Lolomo service, abstracting the underlying data storage mechanism.
 */
@Component // Spring component annotation for dependency injection
public class ShowsRepository {

    /**
     * Jackson ObjectMapper for JSON deserialization
     */
    private final ObjectMapper mapper;

    /**
     * In-memory cache of all shows loaded from the JSON file
     */
    private List<Show> shows;

    /**
     * Constructor injection for ObjectMapper dependency.
     *
     * @param objectMapper Jackson ObjectMapper instance for JSON processing
     */
    public ShowsRepository(ObjectMapper objectMapper) {
        this.mapper = objectMapper;
    }

    /**
     * Initializes the repository by loading show data from JSON file.
     * <p>
     * This method is automatically called after dependency injection is complete
     * but before the bean is put into service. It loads the shows.json file from
     * the classpath and deserializes it into a list of Show objects for in-memory
     * access throughout the application lifecycle.
     *
     * @throws RuntimeException if the JSON file cannot be read or parsed
     */
    @PostConstruct // Executed after dependency injection, before bean is ready for use
    public void loadShows() {
        // Load shows.json from the classpath resources
        var showJson = new ClassPathResource("shows.json");

        try (var inputStream = showJson.getInputStream()) {
            // Deserialize JSON array to List<Show> using TypeReference for generic type safety
            shows = mapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            // Wrap IOException in RuntimeException for Spring's exception handling
            throw new RuntimeException("Failed to load shows from JSON file", e);
        }
    }

    /**
     * Retrieves shows filtered by category with special handling for predefined categories.
     * <p>
     * This method provides different logic for special categories:
     * - "Top 10": Returns the first 10 shows from the list
     * - "Continue Watching": Returns a specific subset of shows (indices 9, 7, 0)
     * - Other categories: Filters shows that contain the category in their categories list
     *
     * @param category The category name to filter by
     * @return List of shows matching the specified category
     */
    public List<Show> showsForCategory(String category) {
        return switch (category) {
            // Return first 10 shows for "Top 10" category
            case "Top 10" -> shows.stream().limit(10).toList();

            // Return specific shows for "Continue Watching" to simulate user progress
            case "Continue Watching" -> List.of(shows.get(9), shows.get(7), shows.get(0));

            // For all other categories, filter by shows that contain the category
            default -> shows.stream()
                    .filter(show -> show.getCategories().contains(category))
                    .toList();
        };
    }

    /**
     * Returns all shows in the repository.
     *
     * @return Complete list of all loaded shows
     */
    public List<Show> allShows() {
        return shows;
    }

    /**
     * Finds a show by its unique ID.
     * <p>
     * Searches through all shows and returns the first match based on show ID.
     * Note: This method assumes the ID exists and will throw NoSuchElementException
     * if no matching show is found.
     *
     * @param id The unique identifier of the show to retrieve
     * @return The Show object with the matching ID
     * @throws java.util.NoSuchElementException if no show with the given ID exists
     */
    public Show byId(Integer id) {
        return shows.stream()
                .filter(show -> show.getShowId().equals(id))
                .findAny()
                .get();
        // Will throw NoSuchElementException if not found
    }
}
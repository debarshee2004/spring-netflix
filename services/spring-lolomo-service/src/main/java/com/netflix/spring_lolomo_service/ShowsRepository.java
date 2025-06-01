package com.netflix.spring_lolomo_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.spring_lolomo_service.codegen.types.Show;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ShowsRepository {

    private final ObjectMapper mapper;
    private List<Show> shows;

    public ShowsRepository(ObjectMapper objectMapper) {
        this.mapper = objectMapper;
    }

    @PostConstruct
    public void loadShows() {
        var showJson = new ClassPathResource("shows.json");
        try(var inputStream = showJson.getInputStream()) {
            shows = mapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Show> showsForCategory(String category) {
        return switch (category) {
            case "Top 10" -> shows.stream().limit(10).toList();
            case "Continue Watching" -> List.of(shows.get(9), shows.get(7), shows.get(0));
            default -> shows.stream().filter(s -> s
                    .getCategories().contains(category)).toList();

        };
    }

    public List<Show> allShows() {
        return shows;
    }

    public Show byId(Integer id) {
        return shows.stream().filter(s -> s
                        .getShowId().equals(id)).findAny().get();
    }
}

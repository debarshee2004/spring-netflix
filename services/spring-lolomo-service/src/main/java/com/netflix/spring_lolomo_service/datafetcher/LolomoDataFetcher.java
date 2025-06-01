package com.netflix.spring_lolomo_service.datafetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.spring_lolomo_service.ShowsRepository;
import com.netflix.spring_lolomo_service.codegen.types.ShowCategory;

import java.util.List;

@DgsComponent
public class LolomoDataFetcher {

    private final ShowsRepository showsRepository;

    public LolomoDataFetcher(ShowsRepository showsRepository) {
        this.showsRepository = showsRepository;
    }

    // GraphIQL: http://localhost:8080/graphiql?path=/graphql
    @DgsQuery
    public List<ShowCategory> lolomo() {
        return List.of(ShowCategory.newBuilder()
                .name("Top 10")
                .shows(showsRepository.showsForCategory("Top 10")).build(),
                ShowCategory.newBuilder()
                        .name("Continue Watching")
                        .shows(showsRepository.showsForCategory("Continue Watching")).build());
    }
}

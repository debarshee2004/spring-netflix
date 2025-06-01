package com.netflix.spring_lolomo_service.datafetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.spring_lolomo_service.ShowsRepository;
import com.netflix.spring_lolomo_service.codegen.types.Show;
import com.netflix.spring_lolomo_service.codegen.types.ShowCategory;
import com.netflix.spring_lolomo_service.service.ArtworkService;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    @DgsData(parentType = "Show")
    public CompletableFuture<String> artworkUrl (DgsDataFetchingEnvironment dfe) {
        Show show = dfe.getSourceOrThrow();

        DataLoader<String, String> dataLoader = dfe.getDataLoader(ArtworkDataLoader.class);

        return dataLoader.load(show.getTitle());
    }
}

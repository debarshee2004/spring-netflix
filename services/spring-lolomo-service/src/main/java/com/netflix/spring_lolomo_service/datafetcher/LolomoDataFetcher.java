package com.netflix.spring_lolomo_service.datafetcher;

import com.netflix.graphql.dgs.*;
import com.netflix.spring_lolomo_service.ShowsRepository;
import com.netflix.spring_lolomo_service.codegen.types.SearchInput;
import com.netflix.spring_lolomo_service.codegen.types.Show;
import com.netflix.spring_lolomo_service.codegen.types.ShowCategory;
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

    @DgsQuery
    public List<Show> search(@InputArgument SearchInput filter) {

        return showsRepository.allShows().stream().filter(
                s -> s.getTitle().toLowerCase().startsWith(
                        filter.getTitle().toLowerCase()
                )
        ).toList();

    }
}

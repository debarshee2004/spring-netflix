package com.netflix.spring_lolomo_service.datafetcher;

import com.netflix.graphql.dgs.*;
import com.netflix.spring_lolomo_service.ShowsRepository;
import com.netflix.spring_lolomo_service.codegen.types.SearchInput;
import com.netflix.spring_lolomo_service.codegen.types.Show;
import com.netflix.spring_lolomo_service.codegen.types.ShowCategory;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * GraphQL data fetcher for the LOLOMO (List of Lists of Movies) service.
 * This class handles GraphQL queries related to show categories, individual shows,
 * and search functionality using Netflix's DGS (Domain Graph Service) framework.
 */
@DgsComponent // Marks this class as a DGS component for GraphQL schema binding
public class LolomoDataFetcher {

    /**
     * Repository for accessing show data from the underlying data source
     */
    private final ShowsRepository showsRepository;

    /**
     * Constructor injection for the shows repository dependency.
     *
     * @param showsRepository Repository instance for accessing show data
     */
    public LolomoDataFetcher(ShowsRepository showsRepository) {
        this.showsRepository = showsRepository;
    }

    /**
     * GraphQL query resolver that returns the main Lolomo structure.
     * Creates a list of show categories including "Top 10" and "Continue Watching".
     * <p>
     * Note: GraphiQL interface available at <a href="http://localhost:8080/graphiql?path=/graphql">
     *          http://localhost:8080/graphiql?path=/graphql
     *      </a>
     *
     * @return List of ShowCategory objects containing categorized shows
     */
    @DgsQuery // Indicates this method resolves a top-level GraphQL query
    public List<ShowCategory> lolomo() {
        // Build and return two predefined categories with their respective shows
        return List.of(ShowCategory.newBuilder().name("Top 10").shows(showsRepository.showsForCategory("Top 10")).build(), ShowCategory.newBuilder().name("Continue Watching").shows(showsRepository.showsForCategory("Continue Watching")).build());
    }

    /**
     * GraphQL field resolver for the artworkUrl field on Show objects.
     * Uses DataLoader pattern to efficiently batch artwork URL requests,
     * preventing N+1 query problems when fetching artwork for multiple shows.
     *
     * @param dfe Data fetching environment providing context and the parent Show object
     * @return CompletableFuture<String> representing the asynchronous artwork URL fetch
     */
    @DgsData(parentType = "Show") // Resolves the artworkUrl field for Show type
    public CompletableFuture<String> artworkUrl(DgsDataFetchingEnvironment dfe) {
        // Extract the parent Show object from the GraphQL execution context
        Show show = dfe.getSourceOrThrow();

        // Get the DataLoader for batching artwork URL requests
        DataLoader<String, String> dataLoader = dfe.getDataLoader(ArtworkDataLoader.class);

        // Load artwork URL using the show's title as the key
        // DataLoader will batch multiple requests and execute them efficiently
        return dataLoader.load(show.getTitle());
    }

    /**
     * GraphQL query resolver for searching shows by title.
     * Performs case-insensitive prefix matching against show titles.
     *
     * @param filter SearchInput containing the search criteria (title filter)
     * @return List of Show objects matching the search criteria
     */
    @DgsQuery // Indicates this method resolves a top-level GraphQL query
    public List<Show> search(@InputArgument SearchInput filter) {
        // Stream all shows and filter by title prefix match (case-insensitive)
        return showsRepository.allShows().stream().filter(s ->
                // Convert both show title and search filter to lowercase for comparison
                s.getTitle().toLowerCase().startsWith(filter.getTitle().toLowerCase())).toList();
                // Collect filtered results into a new list
    }
}
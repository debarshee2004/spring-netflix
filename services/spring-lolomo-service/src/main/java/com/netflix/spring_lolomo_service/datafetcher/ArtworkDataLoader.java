package com.netflix.spring_lolomo_service.datafetcher;

import com.netflix.graphql.dgs.DgsDataLoader;
import com.netflix.spring_lolomo_service.service.ArtworkService;
import org.dataloader.MappedBatchLoader;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * DataLoader implementation for efficiently batching artwork URL requests.
 * <p>
 * This class implements the DataLoader pattern to solve the N+1 query problem
 * when fetching artwork URLs for multiple shows. Instead of making individual
 * requests for each show's artwork, this loader batches multiple requests
 * together and executes them in a single operation.
 * <p>
 * The DataLoader pattern provides:
 * - Request batching: Multiple individual requests are collected and executed together
 * - Caching: Results are cached for the duration of a single GraphQL request
 * - Performance optimization: Reduces the number of service calls
 */
@DgsDataLoader // Registers this class as a DataLoader with the DGS framework
public class ArtworkDataLoader implements MappedBatchLoader<String, String> {

    /**
     * Service responsible for generating artwork URLs
     */
    private final ArtworkService artworkService;

    /**
     * Constructor injection for the artwork service dependency.
     *
     * @param artworkService Service instance for generating artwork URLs
     */
    public ArtworkDataLoader(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    /**
     * Batch load method that processes multiple artwork URL requests simultaneously.
     * <p>
     * This method is called by the DataLoader framework when it's ready to execute
     * a batch of accumulated requests. Instead of making individual calls for each
     * show title, all requests are processed together in a single batch operation.
     *
     * @param keys Set of show titles (keys) for which artwork URLs need to be fetched
     * @return CompletionStage containing a Map where:
     * - Key: Show title (String)
     * - Value: Corresponding artwork URL (String)
     * The map should contain entries for all provided keys
     */
    @Override
    public CompletionStage<Map<String, String>> load(Set<String> keys) {
        // Delegate to the artwork service to generate URLs for all requested show titles
        // The service handles the batching logic and returns a map of title -> artwork URL
        return CompletableFuture.completedFuture(
                artworkService.batchGenerator(keys)
        );
    }
}
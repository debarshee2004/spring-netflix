package com.netflix.spring_lolomo_service.datafetcher;

import com.netflix.graphql.dgs.DgsDataLoader;
import com.netflix.spring_lolomo_service.service.ArtworkService;
import org.dataloader.MappedBatchLoader;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader
public class ArtworkDataLoader implements MappedBatchLoader<String, String> {

    private final ArtworkService artworkService;

    public ArtworkDataLoader(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    @Override
    public CompletionStage<Map<String, String>> load(Set<String> keys) {
        return CompletableFuture.completedFuture(artworkService.batchGenerator(keys));
    }
}

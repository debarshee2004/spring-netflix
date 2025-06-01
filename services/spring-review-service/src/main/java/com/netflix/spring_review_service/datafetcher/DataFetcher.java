package com.netflix.spring_review_service.datafetcher;

import com.netflix.graphql.dgs.*;
import com.netflix.spring_review_service.types.Review;
import com.netflix.spring_review_service.types.Show;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * GraphQL data fetcher for show-related queries and reviews in the Netflix reviews service.
 * <p>
 * This component handles GraphQL operations related to shows and their reviews,
 * including fetching recent reviews, show-specific reviews, and entity resolution
 * for federated GraphQL schemas. It serves as part of a microservice architecture
 * where show data might be distributed across multiple services.
 */
@DgsComponent // Marks this class as a DGS component for GraphQL schema binding
public class DataFetcher {

    /**
     * Logger instance for tracking data fetching operations and debugging
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(DataFetcher.class);

    /**
     * GraphQL query resolver that returns a list of recent reviews across all shows.
     * <p>
     * This method provides a top-level query to fetch the most recent reviews
     * in the system. Currently, returns mock data for demonstration purposes,
     * but in a production system would likely query a database or cache
     * ordered by review creation timestamp.
     *
     * @return List of recent Review objects with associated Show information
     */
    @DgsQuery // Indicates this method resolves a top-level GraphQL query
    public List<Review> recentReviews() {
        // Return mock recent reviews with varying ratings and show associations
        return List.of(
                new Review(5, "Great show", new Show(1, null)),
                new Review(1, "Not enough commercials", new Show(2, null)),
                new Review(3, "Too scary", new Show(3, null))
        );
    }

    /**
     * GraphQL field resolver for the reviews field on Show objects.
     * <p>
     * This method is called when a GraphQL query requests the reviews field
     * for a specific Show. It extracts the parent Show object from the execution
     * context and returns reviews specific to that show. The logging helps
     * track which shows are being queried for performance monitoring.
     *
     * @param dfe Data fetching environment providing access to the parent Show object
     * @return List of Review objects associated with the specified show
     */
    @DgsData(parentType = "Show") // Resolves the reviews field for Show type
    public List<Review> reviews(DgsDataFetchingEnvironment dfe) {
        // Extract the parent Show object from the GraphQL execution context
        Show show = dfe.getSourceOrThrow();

        // Log the review fetch operation for monitoring and debugging
        LOGGER.info("Get reviews for show {}", show.showId());

        // Return mock review data specific to this show
        // In production, this would query a database filtered by show ID
        return List.of(new Review(5, "Great show " + show.showId()));
    }

    /**
     * GraphQL Federation entity resolver for Show entities.
     * <p>
     * This method enables GraphQL Federation by allowing this service to resolve
     * Show entities based on their representation from other services. When another
     * service references a Show by its ID, this resolver creates the local Show
     * object that can then have its fields (like reviews) resolved by this service.
     * <p>
     * This is crucial for federated GraphQL architectures where Show data might
     * be owned by one service (e.g., catalog service) but reviews are owned by
     * this service.
     *
     * @param values Map containing the entity representation, typically including
     *               the showId and other identifying fields from the federated schema
     * @return Show object constructed from the provided entity representation
     */
    @DgsEntityFetcher(name = "Show") // Registers this as an entity resolver for Show type
    public Show show(Map<String, Object> values) {
        // Extract the showId from the entity representation
        var showId = (Integer) values.get("showId");

        // Create and return a Show object with the provided ID
        // The null parameter likely represents other fields that this service
        // doesn't own or need to populate (handled by other federated services)
        return new Show(showId, null);
    }
}
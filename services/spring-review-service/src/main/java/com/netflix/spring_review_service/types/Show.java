package com.netflix.spring_review_service.types;

import java.util.List;

/**
 * Immutable data record representing a Netflix show within the review service context.
 * <p>
 * This record serves as a lightweight representation of a show, focusing specifically
 * on the aspects relevant to the review service. It maintains the show's unique
 * identifier and its associated reviews. Being a record, it automatically provides
 * implementations for equals(), hashCode(), toString(), and accessor methods.
 * <p>
 * Note: This is likely a subset or projection of a more complete Show entity that
 * exists in other services (e.g., catalog service). In a federated GraphQL architecture,
 * different services maintain different aspects of the same logical entity.
 */
public record Show(
        /**
         * Unique identifier for the show across the Netflix platform.
         * Used for referencing and linking shows across different microservices
         * in a federated architecture.
         */
        Integer showId,

        /**
         * Collection of reviews associated with this show.
         * May be null when the show is used as a reference object or when reviews
         * are loaded separately through GraphQL field resolvers. In federated
         * GraphQL scenarios, this field is often populated lazily by dedicated
         * data fetchers rather than being loaded eagerly.
         */
        List<Review> reviews
) {
}
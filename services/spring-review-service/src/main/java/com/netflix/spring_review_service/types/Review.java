package com.netflix.spring_review_service.types;

/**
 * Immutable data record representing a user review for a Netflix show.
 * <p>
 * This record encapsulates all the essential information about a review,
 * including the numerical score, textual feedback, and an optional reference
 * to the associated show. Being a record, it provides automatic implementation
 * of equals(), hashCode(), toString(), and getter methods.
 * <p>
 * The record supports both complete reviews (with show association) and
 * standalone reviews (without show reference) through constructor overloading.
 */
public record Review(
        /**
         * Numerical rating score for the review.
         * Typically, represents a rating scale (e.g., 1-5 stars or 1-10 points).
         */
        Integer score,

        /**
         * Textual content of the review containing the user's written feedback.
         * Can include detailed opinions, critiques, or recommendations.
         */
        String text,

        /**
         * Reference to the Show being reviewed.
         * May be null when the review is used in contexts where the show
         * association is handled separately (e.g., in GraphQL field resolvers).
         */
        Show show
) {
    /**
     * Convenience constructor for creating reviews without explicit show association.
     * <p>
     * This constructor is useful when creating reviews in contexts where the show
     * relationship is established separately, such as when the review is being
     * attached to a show through GraphQL field resolution or when the show
     * context is already known from the parent object.
     *
     * @param score The numerical rating score for the review
     * @param text  The textual content of the review
     */
    public Review(Integer score, String text) {
        // Delegate to primary constructor with null show reference
        this(score, text, null);
    }
}
package com.vasenin.workcube.misc;

import com.vasenin.workcube.domains.Review;

public class SocketsBadgeLogic implements BadgeLogic{
    private static final String NAME = "Много розеток";
    private static final int MIN_REVIEWS = 4;
    private static final double MIN_PERCENTAGE = 0.7;
    private static final int DESIRED_RATING = 3;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getMinReviews() {
        return MIN_REVIEWS;
    }

    @Override
    public double getMinPercentage() {
        return MIN_PERCENTAGE;
    }

    @Override
    public Object getDesiredRating() {
        return DESIRED_RATING;
    }

    @Override
    public boolean isReviewEligible(Review review) {
        return review.getSocketRating() != 0 && review.getSocketRating() == DESIRED_RATING;
    }
}

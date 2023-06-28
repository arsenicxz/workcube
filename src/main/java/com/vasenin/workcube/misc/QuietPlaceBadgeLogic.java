package com.vasenin.workcube.misc;

import com.vasenin.workcube.domains.Review;

public class QuietPlaceBadgeLogic implements BadgeLogic{
    private static final String NAME = "Тихое место";
    private static final int MIN_REVIEWS = 4;
    private static final double MIN_PERCENTAGE = 0.7;
    private static final int DESIRED_RATING = 1;

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
        // Проверяем, что отзыв не имеет рейтинг wifi "Не знаю" и имеет рейтинг wifi "Быстрый"
        return review.getNoiseRating() != 0 && review.getNoiseRating() == DESIRED_RATING;
    }
}

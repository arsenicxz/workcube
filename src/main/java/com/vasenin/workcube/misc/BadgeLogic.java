package com.vasenin.workcube.misc;

import com.vasenin.workcube.domains.Review;

public interface BadgeLogic {
    String getName();

    int getMinReviews();

    double getMinPercentage();

    Object getDesiredRating();

    boolean isReviewEligible(Review review);
}

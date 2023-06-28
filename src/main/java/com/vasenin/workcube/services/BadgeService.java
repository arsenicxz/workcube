package com.vasenin.workcube.services;

import com.vasenin.workcube.domains.Badge;
import com.vasenin.workcube.domains.Place;
import com.vasenin.workcube.domains.Review;
import com.vasenin.workcube.misc.BadgeLogic;
import com.vasenin.workcube.misc.FastWifiBadgeLogic;
import com.vasenin.workcube.misc.QuietPlaceBadgeLogic;
import com.vasenin.workcube.misc.SocketsBadgeLogic;
import com.vasenin.workcube.repositories.BadgeRepository;
import com.vasenin.workcube.repositories.PlaceRepository;
import com.vasenin.workcube.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BadgeService {

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private BadgeRepository badgeRepository;

    // Список всех логик бэйджей в приложении
    private List<BadgeLogic> badgeLogics = Arrays.asList(
            new FastWifiBadgeLogic(),
            new SocketsBadgeLogic(),
            new QuietPlaceBadgeLogic()
    );

    public void processReviews(Long placeId) {
        // Получаем заведение по id или возвращаемся, если не найдено
        Place place = placeRepository.findById(placeId).orElse(null);
        if (place == null) {
            // Обработка случая, когда заведение не найдено
            return;
        }

        // Получаем список отзывов по заведению
        List<Review> reviews = reviewRepository.findAllByPlaceId(placeId);

        // Для каждой логики бэйджа проверяем условия для добавления или удаления бэйджа
        for (BadgeLogic badgeLogic : badgeLogics) {
            // Проверяем, что отзывов достаточно для логики бэйджа
            if (reviews.size() < badgeLogic.getMinReviews()) {
                // Обработка случая, когда отзывов меньше минимального количества для данного бэйджа
                continue;
            }

            // Подсчитываем количество и долю отзывов, удовлетворяющих условиям для данного бэйджа, используя потоки Java 8
            long eligibleCount = reviews.stream()
                    .filter(badgeLogic::isReviewEligible)
                    .count();
            double eligiblePercentage = (double) eligibleCount / (reviews.size() - eligibleCount);

            // Проверяем, есть ли у заведения данный бэйдж
            boolean hasBadge = place.getBadges().stream()
                    .anyMatch(badge -> badge.getName().equalsIgnoreCase(badgeLogic.getName()));

            // Если доля отзывов, удовлетворяющих условиям для данного бэйджа, больше или равна минимальной доле
            if (eligiblePercentage >= badgeLogic.getMinPercentage()) {
                // Если у заведения еще нет данного бэйджа
                if (!hasBadge) {
                    // Добавляем бэйдж из базы данных или кэша по имени из логики бэйджа
                    Badge badge = badgeRepository.findByName(badgeLogic.getName());
                    if (badge == null) {
                        badge = new Badge();
                        badge.setName(badgeLogic.getName());
                        badgeRepository.save(badge);
                    }
                    place.addBadge(badge);
                    placeRepository.save(place);
                }
            } else {
                // Если у заведения есть данный бэйдж
                if (hasBadge) {
                    // Удаляем бэйдж из списка бэйджей заведения по имени из логики бэйджа
                    place.removeBadge(badge -> badge.getName().equalsIgnoreCase(badgeLogic.getName()));
                    placeRepository.save(place);
                }
            }
        }
    }
}

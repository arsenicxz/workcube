package com.vasenin.workcube.services;
import com.vasenin.workcube.domains.Place;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class FiltersSpecification<P> {
    public static Specification<Place> filter(String[] types,
                                              String[] metroStations,
                                              boolean isFree,
                                              String startPrice,
                                              String finishPrice,
                                              boolean isAroundTheClock,
                                              String startWeekdaysHours,
                                              String finalWeekdaysHours,
                                              String startWeekendHours,
                                              String finalWeekendHours,
                                              boolean wifiCheck,
                                              boolean quietCheck,
                                              boolean socketsCheck) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Проверяем, были ли выбраны типы заведений
            if (types != null && types.length > 0) {
                predicates.add(root.get("type").in(types));
//                return root.get("type").in(types);
            }
            if (metroStations != null) {

                List<Predicate> stationPredicates = new ArrayList<>();
                for (String station : metroStations) {
                    stationPredicates.add(builder.isMember(station, root.get("metroStations")));
                }
                predicates.add(builder.or(stationPredicates.toArray(new Predicate[0])));
            }

            if (isFree) {
                predicates.add(builder.isTrue(root.get("isFree")));
            }

            if (!Objects.equals(startPrice, "") && !Objects.equals(startPrice, null)) {
                int converted = Integer.parseInt(startPrice);
                predicates.add(builder.greaterThanOrEqualTo(root.get("price"), converted));
            }

            if (!Objects.equals(finishPrice, "") && !Objects.equals(startPrice, null)) {
                int converted = Integer.parseInt(finishPrice);
                predicates.add(builder.lessThanOrEqualTo(root.get("price"), converted));
            }


            if(isAroundTheClock){
                predicates.add(builder.isTrue(root.get("isAroundTheClock")));
            }

            if (!Objects.equals(startWeekdaysHours, "") && !Objects.equals(startWeekdaysHours, null)) {
                int converted = Integer.parseInt(startWeekdaysHours);
                predicates.add(builder.greaterThanOrEqualTo(root.get("startWorkingHoursWeekdays"), converted));
            }

            if (!Objects.equals(finalWeekdaysHours, "") && !Objects.equals(finalWeekdaysHours, null)) {
                int converted = Integer.parseInt(finalWeekdaysHours);
                predicates.add(builder.lessThanOrEqualTo(root.get("finishWorkingHoursWeekdays"), converted));
            }


            if (!Objects.equals(startWeekendHours, "") && !Objects.equals(startWeekendHours, null)) {
                int converted = Integer.parseInt(startWeekendHours);
                predicates.add(builder.greaterThanOrEqualTo(root.get("startWorkingHoursWeekends"), converted));
            }

            if (!Objects.equals(finalWeekendHours, "") && !Objects.equals(finalWeekendHours, null)) {
                int converted = Integer.parseInt(finalWeekendHours);
                predicates.add(builder.lessThanOrEqualTo(root.get("finishWorkingHoursWeekends"), converted));
            }

            if(wifiCheck){
                Join<Object, Object> badges = root.join("badges");
                predicates.add(badges.get("name").in("Быстрый Wi-Fi"));
            }

            if(quietCheck){
                Join<Object, Object> badges = root.join("badges");
                predicates.add(badges.get("name").in("Тихое место"));
            }

            if(socketsCheck){
                Join<Object, Object> badges = root.join("badges");
                predicates.add(badges.get("name").in( "Много розеток"));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

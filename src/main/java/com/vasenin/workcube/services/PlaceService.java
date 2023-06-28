package com.vasenin.workcube.services;

import com.vasenin.workcube.domains.ErrorMessage;
import com.vasenin.workcube.domains.Place;
import com.vasenin.workcube.domains.Review;
import com.vasenin.workcube.domains.User;
import com.vasenin.workcube.misc.ErrorType;
import com.vasenin.workcube.repositories.PlaceRepository;
import com.vasenin.workcube.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PlaceService {
    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    ReviewRepository reviewRepository;
    List<Place> data;

    public List<Place> getData() {
        return data;
    }

    public void calculateRating(long placeId){
        Place place = placeRepository.findById(placeId);
        List<Review> reviews = reviewRepository.findAllByPlaceId(placeId);
        int sizeReviews = reviews.size();
        double sumForReviews = 0;
        if(sizeReviews != 0){
            for(Review review : reviews){
                sumForReviews += review.getRating();
            }
            double temp = sumForReviews/sizeReviews;

            double finalRating = roundDouble(temp, 2);
            System.out.println(finalRating);

            place.setRating(finalRating);
            place.setRatingCount(sizeReviews);
            placeRepository.save(place);
        }else{
            place.setRating(0);
            place.setRatingCount(0);
            placeRepository.save(place);
        }
    }

    private double roundDouble(double value, int to) {
        if (to < 0) throw new IllegalArgumentException();

        BigDecimal newNumber = new BigDecimal(Double.toString(value));
        newNumber = newNumber.setScale(to, RoundingMode.HALF_UP);
        return newNumber.doubleValue();
    }

    private boolean isEmpty(String value){
        return value.length() == 0;
    }

    private boolean isPlaceExists(Place place) {
        return placeRepository.existsByName(place.getName());
    }

    public List<ErrorMessage> checkNewPlace(String name,
                                            String address,
                                            String latitude,
                                            String longitude,
                                            boolean isFree,
                                            String price,
                                            boolean isAroundTheClock,
                                            String startWeekday,
                                            String finalWeekday,
                                            String startWeekend,
                                            String finalWeekend){
        Place place = new Place();

        List<ErrorMessage> errorMessages = new ArrayList<>();
        place.setName(name);

        if(Objects.equals(latitude, "") || Objects.equals(longitude, "") || isEmpty(address)){
            ErrorMessage message = new ErrorMessage("Адрес не выбран. Должна быть точка на карте", ErrorType.LAT_LON_NULL);
            errorMessages.add(message);
        }

        if(!isFree && (price == null || price.equals(""))){
            ErrorMessage message = new ErrorMessage("Необходимо указать цену", ErrorType.PRICE_REQUIRED);
            errorMessages.add(message);
        }

        if(!isAroundTheClock && (startWeekday.equals("") || finalWeekday.equals("") || startWeekend.equals("") || finalWeekend.equals(""))){
            ErrorMessage message = new ErrorMessage("Необходимо указать часы работы", ErrorType.CLOCK_REQUIRED);
            errorMessages.add(message);
        }

        if(isPlaceExists(place) || isEmpty(name)){
            ErrorMessage message = new ErrorMessage("Место уже существует или строка пустая", ErrorType.PLACE_NAME_ERROR);
            errorMessages.add(message);
        }

        return errorMessages;
    }
}

package com.vasenin.workcube.repositories;

import com.vasenin.workcube.domains.Place;
import com.vasenin.workcube.domains.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByUserIdOrderByDateDesc(long userid);
    List<Review> findAllByPlaceId(long id);
    List<Review> findAllByPlaceIdOrderByDateDesc(long id);
    Review findById(long id);
}

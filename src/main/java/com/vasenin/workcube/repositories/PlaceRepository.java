package com.vasenin.workcube.repositories;

import com.vasenin.workcube.domains.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long>, JpaSpecificationExecutor<Place> {
    boolean existsByName(String name);
    List<Place> findAllBySecretCode(String secretCode);
    Place getByName(String name);
    Place findById(long id);

}

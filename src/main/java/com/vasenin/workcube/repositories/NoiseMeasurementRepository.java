package com.vasenin.workcube.repositories;

import com.vasenin.workcube.domains.NoiseMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoiseMeasurementRepository extends JpaRepository<NoiseMeasurement, Long> {
    List<NoiseMeasurement> findAllBySecretCode(String secretCode);
}

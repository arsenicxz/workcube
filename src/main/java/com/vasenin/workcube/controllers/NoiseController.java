package com.vasenin.workcube.controllers;

import com.vasenin.workcube.domains.NoiseMeasurement;
import com.vasenin.workcube.domains.Place;
import com.vasenin.workcube.repositories.NoiseMeasurementRepository;
import com.vasenin.workcube.repositories.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class NoiseController {
    @Autowired
    PlaceRepository placeRepository;
    @Autowired
    NoiseMeasurementRepository noiseMeasurementRepository;

    @PostMapping("/noise/{secretCode}")
    public ResponseEntity<String> receiveNoiseData(@PathVariable(value = "secretCode")String secretCode,
                                                   @RequestBody String noiseData) {
        System.out.println(noiseData);
        List<Place> places = placeRepository.findAllBySecretCode(secretCode);
        if(!places.isEmpty()){
            Date date = new Date();
            SimpleDateFormat formatDateToHHMM;
            formatDateToHHMM = new SimpleDateFormat("kk:mm");

            if (noiseData != null && noiseData.length() > 0 && noiseData.charAt(noiseData.length() - 1) == '=') {

                noiseData = noiseData.substring(0, noiseData.length() - 1);
                double noise = Double.parseDouble(noiseData);

                NoiseMeasurement noiseMeasurement = new NoiseMeasurement();
                noiseMeasurement.setSecretCode(secretCode);
                noiseMeasurement.setDate(date);
                noiseMeasurement.setFormatedDateTime(formatDateToHHMM.format(date));
                noiseMeasurement.setNoiseLevel(noise);

                noiseMeasurementRepository.save(noiseMeasurement);

                return ResponseEntity.ok("Data received successfully");
            }

            return ResponseEntity.ok("Not ok 1");
        }

        return ResponseEntity.ok("Not ok 2");
    }
}

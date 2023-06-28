package com.vasenin.workcube.services;

import com.vasenin.workcube.repositories.NoiseMeasurementRepository;
import com.vasenin.workcube.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {
    @Autowired
    private NoiseMeasurementRepository noiseMeasurementRepository;

    @Scheduled(cron = "0 30 19 * * ?", zone = "Europe/Moscow")
    public void deleteAll() {
        noiseMeasurementRepository.deleteAll();
    }
}

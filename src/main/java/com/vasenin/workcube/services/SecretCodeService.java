package com.vasenin.workcube.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SecretCodeService {
    public String generateSecretCode() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
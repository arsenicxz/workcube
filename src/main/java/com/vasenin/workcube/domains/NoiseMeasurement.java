package com.vasenin.workcube.domains;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;

@Entity
@Table(name = "noise_measurments")
@EnableAutoConfiguration
public class NoiseMeasurement {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private String secretCode;
    private Date date;
    private String formatedDateTime;
    private double noiseLevel;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFormatedDateTime() {
        return formatedDateTime;
    }

    public void setFormatedDateTime(String formatedDateTime) {
        this.formatedDateTime = formatedDateTime;
    }

    public double getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(double noiseLevel) {
        this.noiseLevel = noiseLevel;
    }
}

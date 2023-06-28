package com.vasenin.workcube.domains;

import com.vasenin.workcube.misc.FastWifiBadge;
import com.vasenin.workcube.misc.QuietPlaceBadge;
import com.vasenin.workcube.misc.SocketsBadge;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "wifi_rating", nullable = false)
    private int wifiRating;

    @Column(name = "noise_rating", nullable = false)
    private int noiseRating;

    @Column(name = "socket_rating", nullable = false)
    private int socketRating;

    @Column(name = "seat_rating", nullable = false)
    private int seatRating;
    @Column(name = "location_rating", nullable = false)
    private int locationRating;
    @Column(name = "staff_rating", nullable = false)
    private int staffRating;
    @Column(name = "aethtetic_rating", nullable = false)
    private int aethteticRating;

    @Column(name = "comment_rating")
    private String comment;

    private Date date;

    public double getRating(){
        double sum = this.seatRating + this.locationRating + this.staffRating + this.aethteticRating;
        return Math.ceil(sum / 4 * 100) / 100;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public int getWifiRating() {
        return wifiRating;
    }

    public String getWifiRatingString(){
        return FastWifiBadge.values()[wifiRating].getTitle();
    }

    public String getSocketsString(){
        return SocketsBadge.values()[socketRating].getTitle();
    }

    public String getQuietString(){
        return QuietPlaceBadge.values()[noiseRating].getTitle();
    }

    public void setWifiRating(int wifiRating) {
        this.wifiRating = wifiRating;
    }

    public int getNoiseRating() {
        return noiseRating;
    }

    public void setNoiseRating(int noiseRating) {
        this.noiseRating = noiseRating;
    }

    public int getSocketRating() {
        return socketRating;
    }

    public void setSocketRating(int socketRating) {
        this.socketRating = socketRating;
    }

    public int getSeatRating() {
        return seatRating;
    }

    public void setSeatRating(int seatRating) {
        this.seatRating = seatRating;
    }

    public int getStaffRating() {
        return staffRating;
    }

    public void setStaffRating(int staffRating) {
        this.staffRating = staffRating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLocationRating() {
        return locationRating;
    }

    public void setLocationRating(int locationRating) {
        this.locationRating = locationRating;
    }

    public int getAethteticRating() {
        return aethteticRating;
    }

    public void setAethteticRating(int aethteticRating) {
        this.aethteticRating = aethteticRating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
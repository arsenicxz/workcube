package com.vasenin.workcube.domains;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Set;
import java.util.function.Predicate;


@Entity
@Table(name = "places")
@EnableAutoConfiguration
public class Place {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private String name;
    private String type;
    private String address;
    private String site;
    private double rating;
    private int ratingCount;
    private boolean isFree;
    private double price;
    private String typePrice;
    private boolean isAroundTheClock;
    private int startWorkingHoursWeekdays;
    private int finishWorkingHoursWeekdays;
    private int startWorkingHoursWeekends;
    private int finishWorkingHoursWeekends;
    private double latitude;
    private double longitude;

    @ElementCollection
    @CollectionTable(name = "place_metro_stations", joinColumns = @JoinColumn(name = "place_id"))
    @Column(name = "metro_station")
    private Set<String> metroStations;
    @ManyToMany
    @JoinTable( name = "places_badges",
                joinColumns = @JoinColumn(name = "place_id"),
                inverseJoinColumns = @JoinColumn(name = "badge_id") )
    private Set<Badge> badges;
    @Column(unique = true)
    private String secretCode;
    private String photo;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }



    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Set<String> getMetroStations() {
        return metroStations;
    }

    public void setMetroStations(Set<String> metroStations) {
        this.metroStations = metroStations;
    }

    public Set<Badge> getBadges() {
        return badges;
    }

    public void setBadges(Set<Badge> badges) {
        this.badges = badges;
    }

    public void addBadge(Badge badge) {this.badges.add(badge); }

    public void removeBadge(Predicate<Badge> predicate) {this.badges.removeIf(predicate);}

    public String getPhoto() {return photo;}

    public void setPhoto(String photo) {this.photo = photo;}

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStartWorkingHoursWeekdays() {
        return startWorkingHoursWeekdays;
    }

    public void setStartWorkingHoursWeekdays(int startWorkingHoursWeekdays) {
        this.startWorkingHoursWeekdays = startWorkingHoursWeekdays;
    }

    public int getFinishWorkingHoursWeekdays() {
        return finishWorkingHoursWeekdays;
    }

    public void setFinishWorkingHoursWeekdays(int finishWorkingHoursWeekdays) {
        this.finishWorkingHoursWeekdays = finishWorkingHoursWeekdays;
    }

    public int getStartWorkingHoursWeekends() {
        return startWorkingHoursWeekends;
    }

    public void setStartWorkingHoursWeekends(int startWorkingHoursWeekends) {
        this.startWorkingHoursWeekends = startWorkingHoursWeekends;
    }

    public int getFinishWorkingHoursWeekends() {
        return finishWorkingHoursWeekends;
    }

    public void setFinishWorkingHoursWeekends(int finishWorkingHoursWeekends) {
        this.finishWorkingHoursWeekends = finishWorkingHoursWeekends;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public boolean isAroundTheClock() {
        return isAroundTheClock;
    }

    public void setAroundTheClock(boolean aroundTheClock) {
        isAroundTheClock = aroundTheClock;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getTypePrice() {
        return typePrice;
    }

    public void setTypePrice(String typePrice) {
        this.typePrice = typePrice;
    }
}

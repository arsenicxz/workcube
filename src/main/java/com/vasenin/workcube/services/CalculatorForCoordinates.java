package com.vasenin.workcube.services;

public class CalculatorForCoordinates {
    private static double EARTH_RADIUS = 6378137;
    private static double SCALE = Math.pow(10, 6);

    private static double convertToRadians(double angle){
        return angle * (Math.PI / 180);
    }

    public static double getDistance(double latitude1, double longitude1, double latitude2, double longitude2){
        latitude1 = convertToRadians(latitude1);
        longitude1 = convertToRadians(longitude1);
        latitude2 = convertToRadians(latitude2);
        longitude2 = convertToRadians(longitude2);

        return ( EARTH_RADIUS * Math.acos( Math.cos( latitude1 ) * Math.cos( latitude2 ) * Math.cos( longitude1 - longitude2 ) + Math.sin( latitude1 ) * Math.sin( latitude2 ) ) );
    }

    public static boolean isPointInArea(double lat1, double lon1, double lat2, double lon2, int radius){
        double distance = Math.ceil(getDistance(lat1,lon1,lat2,lon2) * SCALE) / SCALE;
        return !(distance > radius);
    }
}

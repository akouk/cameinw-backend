package com.cameinw.cameinwbackend.utilities;

public class GeoUtils {
    public static final double EARTH_RADIUS_KM = 6371.0;
    public static double computeGeoDistance(double latRepo, double longRepo, double latReq, double longReq) {
        double latRepoRad = Math.toRadians(latRepo);
        double longRepoRad = Math.toRadians(longRepo);
        double latReqRad = Math.toRadians(latReq);
        double longReqRad = Math.toRadians(longReq);

        double latDiff = latReqRad - latRepoRad;
        double longDiff = longReqRad - longRepoRad;

        double a = Math.pow(Math.sin(latDiff / 2), 2) +
                Math.cos(latRepoRad) * Math.cos(latReqRad) *
                        Math.pow(Math.sin(longDiff / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}

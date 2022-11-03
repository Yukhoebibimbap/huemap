package com.huemap.backend.common.utils;

public class GeometryUtil {

  public static double calculateDistance(Double latitude1,
                                         Double longitude1,
                                         Double latitude2,
                                         Double longitude2) {
    final double theta = longitude1 - longitude2;
    double dist = Math.sin(degToRad(latitude1)) * Math.sin(degToRad(latitude2))
        + Math.cos(degToRad(latitude1)) * Math.cos(degToRad(latitude2)) * Math.cos(degToRad(theta));

    dist = Math.acos(dist);
    dist = radToDeg(dist);
    dist = dist * 60 * 1.1515;
    dist = dist * 1609.344;

    return (dist);
  }

  private static double degToRad(double deg) {
    return (deg * Math.PI / 180.0);
  }

  private static double radToDeg(double rad) {
    return (rad * 180 / Math.PI);
  }
}

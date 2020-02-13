package com.phlink.bus.api.trajectory.domain;

import com.goebl.simplify.Point;

public class TrajectoryPoint implements Point {
    private final double lat;
    private final double lng;

    public TrajectoryPoint(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public double getX() {
        return this.lat;
    }

    @Override
    public double getY() {
        return this.lng;
    }
}

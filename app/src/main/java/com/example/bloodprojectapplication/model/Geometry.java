package com.example.bloodprojectapplication.model;

public class Geometry {
    private Location location;
    private String locationType;
    private Viewport viewport;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location value) {
        this.location = value;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String value) {
        this.locationType = value;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport value) {
        this.viewport = value;
    }
}
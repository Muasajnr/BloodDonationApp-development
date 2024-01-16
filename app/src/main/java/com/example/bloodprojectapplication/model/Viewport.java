package com.example.bloodprojectapplication.model;

public class Viewport {
    private Location northeast;
    private Location southwest;

    public Location getNortheast() {
        return northeast;
    }

    public void setNortheast(Location value) {
        this.northeast = value;
    }

    public Location getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Location value) {
        this.southwest = value;
    }
}
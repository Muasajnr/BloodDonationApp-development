package com.example.bloodprojectapplication.model;

public class Geocoding {
    private Result[] results;
    private String status;

    public Result[] getResults() {
        return results;
    }

    public void setResults(Result[] value) {
        this.results = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }
}

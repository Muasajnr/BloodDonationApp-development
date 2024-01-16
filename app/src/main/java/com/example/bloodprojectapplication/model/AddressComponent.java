package com.example.bloodprojectapplication.model;


public class AddressComponent {
    private String longName;
    private String shortName;
    private String[] types;

    public String getLongName() {
        return longName;
    }

    public void setLongName(String value) {
        this.longName = value;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String value) {
        this.shortName = value;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] value) {
        this.types = value;
    }
}
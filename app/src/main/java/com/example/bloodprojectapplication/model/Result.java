package com.example.bloodprojectapplication.model;

public class Result {
    private AddressComponent[] addressComponents;
    private String formattedAddress;
    private Geometry geometry;
    private boolean partialMatch;
    private String placeID;
    private PlusCode plusCode;
    private String[] types;

    public AddressComponent[] getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(AddressComponent[] value) {
        this.addressComponents = value;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String value) {
        this.formattedAddress = value;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry value) {
        this.geometry = value;
    }

    public boolean getPartialMatch() {
        return partialMatch;
    }

    public void setPartialMatch(boolean value) {
        this.partialMatch = value;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String value) {
        this.placeID = value;
    }

    public PlusCode getPlusCode() {
        return plusCode;
    }

    public void setPlusCode(PlusCode value) {
        this.plusCode = value;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] value) {
        this.types = value;
    }
}

package com.example.pharmacies;

public class Address {
    private String country;
    private String region;
    private String city;
    private String neighborhood;
    private String streetNo;
    private String shortAddress;
    private String buildingNo;
    private String apartmentNo;
    private String fullName;
    private String primaryPhone;
    private String secondaryPhone;
    private String postalCode;

    // Constructor

    public Address(){

    }
    public Address(String country, String region, String city, String neighborhood, String streetNo, String shortAddress,
                   String buildingNo, String apartmentNo, String fullName, String primaryPhone, String secondaryPhone) {
        this.country = country;
        this.region = region;
        this.city = city;
        this.neighborhood = neighborhood;
        this.streetNo = streetNo;
        this.shortAddress = shortAddress;
        this.buildingNo = buildingNo;
        this.apartmentNo = apartmentNo;
        this.fullName = fullName;
        this.primaryPhone = primaryPhone;
        this.secondaryPhone = secondaryPhone;
    }

    // Getters and setters


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public String getShortAddress() {
        return shortAddress;
    }

    public void setShortAddress(String shortAddress) {
        this.shortAddress = shortAddress;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getApartmentNo() {
        return apartmentNo;
    }

    public void setApartmentNo(String apartmentNo) {
        this.apartmentNo = apartmentNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}


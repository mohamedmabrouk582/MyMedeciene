package com.example.mohamed.mymedeciene.data;

public class LocationModel {
    private String cityName;
    private String cityState;
    private String  countryName;
    private String lat;
    private String lang;
    private String address;
    private String adminArea;
    private String featureName;
    private String subLocality;
    private String phone;
    private String postalCode;
    private String locality;
    private String subAdminArea;



    public LocationModel(String cityName, String cityState, String countryName, String lat, String lang) {
        this.cityName = cityName;
        this.cityState = cityState;
        this.countryName = countryName;
        this.lat = lat;
        this.lang = lang;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdminArea() {
        return adminArea;
    }

    public void setAdminArea(String adminArea) {
        this.adminArea = adminArea;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getSubLocality() {
        return subLocality;
    }

    public void setSubLocality(String subLocality) {
        this.subLocality = subLocality;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getSubAdminArea() {
        return subAdminArea;
    }

    public void setSubAdminArea(String subAdminArea) {
        this.subAdminArea = subAdminArea;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public LocationModel() {
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityState() {
        return cityState;
    }

    public void setCityState(String cityState) {
        this.cityState = cityState;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String toString() {
        return "LocationModel{" +
                "cityName='" + cityName + '\'' +
                ", cityState='" + cityState + '\'' +
                ", countryName='" + countryName + '\'' +
                ", lat='" + lat + '\'' +
                ", lang='" + lang + '\'' +
                '}';
    }
}

package com.example.proyectouao.model;

import java.util.Objects;

public class _Side {

    private String nameSide;
    private String latitude;
    private String longitude;
    private String description;

    public _Side(String nameSide, String latitude, String longitude, String description) {
        this.nameSide = nameSide;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public _Side() {
    }

    public String getNameSide() {
        return nameSide;
    }

    public void setNameSide(String nameSide) {
        this.nameSide = nameSide;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        _Side side = (_Side) o;
        return Objects.equals(nameSide, side.nameSide) && Objects.equals(latitude, side.latitude) && Objects.equals(longitude, side.longitude) && Objects.equals(description, side.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameSide, latitude, longitude, description);
    }
}

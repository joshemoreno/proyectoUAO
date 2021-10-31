package com.example.proyectouao.model;

import java.util.Objects;

public class _Offer {
    private String id;
    private String nameRestaurant;
    private String comboTitle;
    private String comboDescription;
    private String count;
    private String startDate;
    private String endDate;
    private String status;

    public _Offer(String id, String nameRestaurant, String comboTitle, String comboDescription, String count, String startDate, String endDate, String status) {
        this.id = id;
        this.nameRestaurant = nameRestaurant;
        this.comboTitle = comboTitle;
        this.comboDescription = comboDescription;
        this.count = count;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public _Offer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public void setNameRestaurant(String nameRestaurant) {
        this.nameRestaurant = nameRestaurant;
    }

    public String getComboTitle() {
        return comboTitle;
    }

    public void setComboTitle(String comboTitle) {
        this.comboTitle = comboTitle;
    }

    public String getComboDescription() {
        return comboDescription;
    }

    public void setComboDescription(String comboDescription) {
        this.comboDescription = comboDescription;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        _Offer offer = (_Offer) o;
        return Objects.equals(id, offer.id) && Objects.equals(nameRestaurant, offer.nameRestaurant) && Objects.equals(comboTitle, offer.comboTitle) && Objects.equals(comboDescription, offer.comboDescription) && Objects.equals(count, offer.count) && Objects.equals(startDate, offer.startDate) && Objects.equals(endDate, offer.endDate) && Objects.equals(status, offer.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameRestaurant, comboTitle, comboDescription, count, startDate, endDate, status);
    }
}

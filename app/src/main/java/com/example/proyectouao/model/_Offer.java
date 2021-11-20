package com.example.proyectouao.model;

import java.util.Objects;

public class _Offer {
    private String id;
    private String selectSide;
    private String comboTitle;
    private String comboDescription;
    private String amount;
    private String startDate;
    private String endDate;
    private String status;
    private String url;
    private String price;

    public _Offer() {
    }

    public _Offer(String id, String selectSide, String comboTitle, String comboDescription, String amount, String startDate, String endDate, String status, String url, String price) {
        this.id = id;
        this.selectSide = selectSide;
        this.comboTitle = comboTitle;
        this.comboDescription = comboDescription;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.url = url;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelectSide() {
        return selectSide;
    }

    public void setSelectSide(String selectSide) {
        this.selectSide = selectSide;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

package com.example.proyectouao.model;

public class _Shopping {

    private String id;
    private String comboTitle;
    private String amount;
    private String price;
    private String count;

    public _Shopping() {
    }

    public _Shopping(String id, String comboTitle, String amount, String price, String count) {
        this.id = id;
        this.comboTitle = comboTitle;
        this.amount = amount;
        this.price = price;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComboTitle() {
        return comboTitle;
    }

    public void setComboTitle(String comboTitle) {
        this.comboTitle = comboTitle;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}

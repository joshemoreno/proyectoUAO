package com.example.proyectouao.model;

public class _ShoppingList {
    private String client;
    private String count;
    private String product;

    public _ShoppingList() {
    }

    public _ShoppingList(String client, String count, String product) {
        this.client = client;
        this.count = count;
        this.product = product;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}

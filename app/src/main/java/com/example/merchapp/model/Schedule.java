package com.example.merchapp.model;

public class Schedule {
    private Long id;
    private Store store;
    private String date;

    public Schedule() {}

    public Schedule(Long id, Store store, String date) {
        this.id = id;
        this.store = store;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
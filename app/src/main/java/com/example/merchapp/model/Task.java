package com.example.merchapp.model;

public class Task {
    private Long id;
    private Store store;
    private boolean requiresCashRegisterPhoto;
    private boolean requiresMainZonePhoto;
    private boolean completed;

    public Task() {}

    public Task(Long id, Store store, boolean requiresCashRegisterPhoto, boolean requiresMainZonePhoto, boolean completed) {
        this.id = id;
        this.store = store;
        this.requiresCashRegisterPhoto = requiresCashRegisterPhoto;
        this.requiresMainZonePhoto = requiresMainZonePhoto;
        this.completed = completed;
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

    public boolean isRequiresCashRegisterPhoto() {
        return requiresCashRegisterPhoto;
    }

    public void setRequiresCashRegisterPhoto(boolean requiresCashRegisterPhoto) {
        this.requiresCashRegisterPhoto = requiresCashRegisterPhoto;
    }

    public boolean isRequiresMainZonePhoto() {
        return requiresMainZonePhoto;
    }

    public void setRequiresMainZonePhoto(boolean requiresMainZonePhoto) {
        this.requiresMainZonePhoto = requiresMainZonePhoto;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
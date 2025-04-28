package com.example.merchapp.model;

public class Report {
    private Long id;
    private Task task;
    private int cashRegisterCount;
    private String comment;
    private String cashRegisterPhotoUrl;
    private String mainZonePhotoUrl;

    public Report() {}

    public Report(Long id, Task task, int cashRegisterCount, String comment, String cashRegisterPhotoUrl, String mainZonePhotoUrl) {
        this.id = id;
        this.task = task;
        this.cashRegisterCount = cashRegisterCount;
        this.comment = comment;
        this.cashRegisterPhotoUrl = cashRegisterPhotoUrl;
        this.mainZonePhotoUrl = mainZonePhotoUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getCashRegisterCount() {
        return cashRegisterCount;
    }

    public void setCashRegisterCount(int cashRegisterCount) {
        this.cashRegisterCount = cashRegisterCount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCashRegisterPhotoUrl() {
        return cashRegisterPhotoUrl;
    }

    public void setCashRegisterPhotoUrl(String cashRegisterPhotoUrl) {
        this.cashRegisterPhotoUrl = cashRegisterPhotoUrl;
    }

    public String getMainZonePhotoUrl() {
        return mainZonePhotoUrl;
    }

    public void setMainZonePhotoUrl(String mainZonePhotoUrl) {
        this.mainZonePhotoUrl = mainZonePhotoUrl;
    }
}
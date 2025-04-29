package com.example.merchapp.api;

import com.example.merchapp.model.LoginRequest;
import com.example.merchapp.model.LoginResponse;
import com.example.merchapp.model.Report;
import com.example.merchapp.model.Schedule;
import com.example.merchapp.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @GET("tasks") // Убедитесь, что это правильный путь к API
    Call<List<Task>> getTasks(@Header("Authorization") String authHeader);
    @POST("reports/submit") // Убедитесь, что это правильный путь к API
    Call<Void> submitReport(@Header("Authorization") String authHeader, @Body ReportRequest reportRequest);

    @GET("schedules") // Убедитесь, что это правильный путь к API
    Call<List<Schedule>> getSchedules(@Header("Authorization") String authHeader);
    // Метод для получения отчетов
    @GET("reports") // Замените на реальный путь API для отчетов
    Call<List<Report>> getReports(@Header("Authorization") String authHeader);

    // Метод для логина
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    // Вложенный класс ReportRequest, если он используется внутри ApiService
    public static class ReportRequest {
        private Long taskId;
        private int cashRegisterCount;
        private String comment;
        private String cashPhotoUrl;
        private String zonePhotoUrl;

        // Конструктор
        public ReportRequest(Long taskId, int cashRegisterCount, String comment, String cashPhotoUrl, String zonePhotoUrl) {
            this.taskId = taskId;
            this.cashRegisterCount = cashRegisterCount;
            this.comment = comment;
            this.cashPhotoUrl = cashPhotoUrl;
            this.zonePhotoUrl = zonePhotoUrl;
        }

        // Геттеры и сеттеры
        public Long getTaskId() {
            return taskId;
        }

        public void setTaskId(Long taskId) {
            this.taskId = taskId;
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

        public String getCashPhotoUrl() {
            return cashPhotoUrl;
        }

        public void setCashPhotoUrl(String cashPhotoUrl) {
            this.cashPhotoUrl = cashPhotoUrl;
        }

        public String getZonePhotoUrl() {
            return zonePhotoUrl;
        }

        public void setZonePhotoUrl(String zonePhotoUrl) {
            this.zonePhotoUrl = zonePhotoUrl;
        }
    }
}

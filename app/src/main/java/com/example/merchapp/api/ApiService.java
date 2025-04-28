package com.example.merchapp.api;

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
    @POST("api/auth/signin")
    Call<LoginResponse> signin(@Body LoginRequest request);

    @GET("api/tasks")
    Call<List<Task>> getTasks(@Header("Authorization") String token);

    @GET("api/schedules")
    Call<List<Schedule>> getSchedules(@Header("Authorization") String token);

    @GET("api/reports")
    Call<List<Report>> getReports(@Header("Authorization") String token);

    @POST("api/reports")
    Call<Void> submitReport(@Header("Authorization") String token, @Body ReportRequest report);

    @POST("api/upload")
    Call<UploadResponse> uploadPhoto(@Header("Authorization") String token, @Body UploadRequest request);

    class LoginRequest {
        public String username;
        public String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    class LoginResponse {
        public String token;
        public String type;
        public Long id;
        public String username;
        public String email;
        public List<String> roles;

        public LoginResponse(String token, String type, Long id, String username, String email, List<String> roles) {
            this.token = token;
            this.type = type;
            this.id = id;
            this.username = username;
            this.email = email;
            this.roles = roles;
        }
    }

    class ReportRequest {
        public Long taskId;
        public int cashRegisterCount;
        public String comment;
        public String cashRegisterPhotoUrl;
        public String mainZonePhotoUrl;

        public ReportRequest(Long taskId, int cashRegisterCount, String comment, String cashRegisterPhotoUrl, String mainZonePhotoUrl) {
            this.taskId = taskId;
            this.cashRegisterCount = cashRegisterCount;
            this.comment = comment;
            this.cashRegisterPhotoUrl = cashRegisterPhotoUrl;
            this.mainZonePhotoUrl = mainZonePhotoUrl;
        }
    }

    class UploadRequest {
        public String file;

        public UploadRequest(String file) {
            this.file = file;
        }
    }

    class UploadResponse {
        public String url;

        public UploadResponse(String url) {
            this.url = url;
        }
    }
}
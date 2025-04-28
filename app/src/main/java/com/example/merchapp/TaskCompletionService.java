package com.example.merchapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import com.example.merchapp.api.ApiClient;
import com.example.merchapp.api.ApiService;
import com.example.merchapp.utils.AuthManager;

public class TaskCompletionService extends Service {
    private AuthManager authManager;

    @Override
    public void onCreate() {
        super.onCreate();
        authManager = new AuthManager(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Long taskId = intent.getLongExtra("taskId", -1);
            int cashRegisterCount = intent.getIntExtra("cashRegisterCount", 0);
            String comment = intent.getStringExtra("comment");
            String cashPhotoUrl = intent.getStringExtra("cashPhotoUrl");
            String zonePhotoUrl = intent.getStringExtra("zonePhotoUrl");

            new Thread(() -> {
                submitReport(taskId, cashRegisterCount, comment, cashPhotoUrl, zonePhotoUrl);
                stopSelf();
            }).start();
        }

        return START_NOT_STICKY;
    }

    private void submitReport(Long taskId, int cashRegisterCount, String comment, String cashPhotoUrl, String zonePhotoUrl) {
        String token = authManager.getToken();
        ApiService.ReportRequest report = new ApiService.ReportRequest(taskId, cashRegisterCount, comment, cashPhotoUrl, zonePhotoUrl);

        ApiClient.getApiService().submitReport("Bearer " + token, report).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    sendBroadcast(new Intent("com.example.merchapp.TASK_UPDATED"));
                } else {
                    Toast.makeText(TaskCompletionService.this, "Failed to submit report", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                Toast.makeText(TaskCompletionService.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
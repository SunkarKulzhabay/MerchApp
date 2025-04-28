package com.example.merchapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.example.merchapp.api.ApiClient;
import com.example.merchapp.model.Task;
import com.example.merchapp.utils.AuthManager;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskSyncService extends Service {
    private static final String CHANNEL_ID = "TaskSyncChannel";
    private static final int NOTIFICATION_ID = 1;
    private AuthManager authManager;
    private Handler handler;
    private Runnable taskCheckRunnable;
    private int lastTaskCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        authManager = new AuthManager(this);
        handler = new Handler();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = createNotification();
        startForeground(NOTIFICATION_ID, notification);

        taskCheckRunnable = new Runnable() {
            @Override
            public void run() {
                checkForNewTasks();
                handler.postDelayed(this, 60000);
            }
        };
        handler.post(taskCheckRunnable);

        return START_STICKY;
    }

    private void checkForNewTasks() {
        String token = authManager.getToken();
        if (token == null) return;

        ApiClient.getApiService().getTasks("Bearer " + token).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int currentTaskCount = response.body().size();
                    if (currentTaskCount > lastTaskCount && lastTaskCount > 0) {
                        sendNewTaskNotification(currentTaskCount - lastTaskCount);
                    }
                    lastTaskCount = currentTaskCount;
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
            }
        });
    }

    private void sendNewTaskNotification(int newTaskCount) {
        Intent intent = new Intent(this, TaskListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("New Tasks Available")
                .setContentText("You have " + newTaskCount + " new task(s).")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID + 1, notification);
    }

    private Notification createNotification() {
        Intent intent = new Intent(this, TaskListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Task Sync Service")
                .setContentText("Checking for new tasks...")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Task Sync Notifications",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(taskCheckRunnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
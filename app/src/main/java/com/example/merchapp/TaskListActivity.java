package com.example.merchapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.merchapp.api.ApiClient;
import com.example.merchapp.model.Task;
import com.example.merchapp.utils.AuthManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {
    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private AuthManager authManager;
    private BroadcastReceiver taskUpdateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        authManager = new AuthManager(this);
        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(
                task -> {
                    Intent intent = new Intent(this, TaskDetailActivity.class);
                    intent.putExtra("taskId", task.getId());
                    startActivity(intent);
                },
                (task, view, position) -> showContextMenu(view, task, position)
        );
        taskRecyclerView.setAdapter(taskAdapter);

        // Register receiver to update tasks
        taskUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadTasks();
            }
        };
        registerReceiver(taskUpdateReceiver, new IntentFilter("com.example.merchapp.TASK_UPDATED"));

        // Start syncing service
        Intent serviceIntent = new Intent(this, TaskSyncService.class);
        ContextCompat.startForegroundService(this, serviceIntent);

        // Load tasks
        loadTasks();
    }

    private void loadTasks() {
        String token = authManager.getToken();
        ApiClient.getApiService().getTasks("Bearer " + token).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    taskAdapter.setTasks(response.body());
                } else {
                    Toast.makeText(TaskListActivity.this, "Failed to load tasks", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(TaskListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_view_schedules) {
            startActivity(new Intent(this, ScheduleActivity.class));
            return true;
        } else if (id == R.id.action_view_reports) {
            startActivity(new Intent(this, ReportActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            authManager.clearAuthData();
            stopService(new Intent(this, TaskSyncService.class));
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showContextMenu(View view, Task task, int position) {
        PopupMenu contextMenu = new PopupMenu(this, view);
        contextMenu.getMenuInflater().inflate(R.menu.task_context_menu, contextMenu.getMenu());
        contextMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_popup) {
                showPopupMenu(view, task);
                return true;
            }
            return false;
        });
        contextMenu.show();
    }

    private void showPopupMenu(View view, Task task) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.task_popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_view_details) {
                Intent intent = new Intent(this, TaskDetailActivity.class);
                intent.putExtra("taskId", task.getId());
                startActivity(intent);
                return true;
            }
            return false;
        });
        popup.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(taskUpdateReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }
}

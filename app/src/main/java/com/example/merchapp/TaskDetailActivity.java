package com.example.merchapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.merchapp.utils.AuthManager;

public class TaskDetailActivity extends AppCompatActivity {
    private EditText cashRegisterCountEditText, commentEditText;
    private Button uploadCashPhotoButton, uploadZonePhotoButton, submitButton, cancelButton;
    private AuthManager authManager;
    private Long taskId;
    private String cashPhotoUrl = "", zonePhotoUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        authManager = new AuthManager(this);
        taskId = getIntent().getLongExtra("taskId", -1);

        cashRegisterCountEditText = findViewById(R.id.cashRegisterCountEditText);
        commentEditText = findViewById(R.id.commentEditText);
        uploadCashPhotoButton = findViewById(R.id.uploadCashPhotoButton);
        uploadZonePhotoButton = findViewById(R.id.uploadZonePhotoButton);
        submitButton = findViewById(R.id.submitButton);
        cancelButton = findViewById(R.id.cancelButton);

        uploadCashPhotoButton.setOnClickListener(v -> {
            cashPhotoUrl = "mock_cash_photo_url";
            Toast.makeText(this, "Cash photo selected", Toast.LENGTH_SHORT).show();
        });

        uploadZonePhotoButton.setOnClickListener(v -> {
            zonePhotoUrl = "mock_zone_photo_url";
            Toast.makeText(this, "Zone photo selected", Toast.LENGTH_SHORT).show();
        });

        submitButton.setOnClickListener(v -> showConfirmDialog());

        cancelButton.setOnClickListener(v -> finish());
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm, null);
        builder.setView(dialogView);

        Button confirmButton = dialogView.findViewById(R.id.confirmButton);
        Button cancelDialogButton = dialogView.findViewById(R.id.cancelDialogButton);

        AlertDialog dialog = builder.create();

        confirmButton.setOnClickListener(v -> {
            submitReport();
            dialog.dismiss();
        });

        cancelDialogButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void submitReport() {
        String cashRegisterCountStr = cashRegisterCountEditText.getText().toString().trim();
        String comment = commentEditText.getText().toString().trim();

        if (cashRegisterCountStr.isEmpty()) {
            Toast.makeText(this, "Please enter cash register count", Toast.LENGTH_SHORT).show();
            return;
        }

        int cashRegisterCount = Integer.parseInt(cashRegisterCountStr);

        Intent serviceIntent = new Intent(this, TaskCompletionService.class);
        serviceIntent.putExtra("taskId", taskId);
        serviceIntent.putExtra("cashRegisterCount", cashRegisterCount);
        serviceIntent.putExtra("comment", comment);
        serviceIntent.putExtra("cashPhotoUrl", cashPhotoUrl);
        serviceIntent.putExtra("zonePhotoUrl", zonePhotoUrl);
        startService(serviceIntent);

        Toast.makeText(this, "Report submission started", Toast.LENGTH_SHORT).show();
        finish();
    }
}
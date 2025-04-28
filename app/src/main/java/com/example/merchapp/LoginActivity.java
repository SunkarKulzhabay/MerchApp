package com.example.merchapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.merchapp.api.ApiClient;
import com.example.merchapp.api.ApiService;
import com.example.merchapp.utils.AuthManager;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText usernameEditText, passwordEditText;
    private Button loginButton;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authManager = new AuthManager(this);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        if (authManager.getToken() != null && "ROLE_MERCHANDISER".equals(authManager.getRole())) {
            startActivity(new Intent(this, TaskListActivity.class));
            finish();
        }

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = ApiClient.getApiService();
            ApiService.LoginRequest request = new ApiService.LoginRequest(username, password);
            apiService.signin(request).enqueue(new retrofit2.Callback<ApiService.LoginResponse>() {
                @Override
                public void onResponse(retrofit2.Call<ApiService.LoginResponse> call, retrofit2.Response<ApiService.LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiService.LoginResponse loginResponse = response.body();
                        if (loginResponse.roles.contains("ROLE_MERCHANDISER")) {
                            authManager.saveAuthData(loginResponse.token, loginResponse.roles);
                            startActivity(new Intent(LoginActivity.this, TaskListActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Access denied: MERCHANDISER role required", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ApiService.LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
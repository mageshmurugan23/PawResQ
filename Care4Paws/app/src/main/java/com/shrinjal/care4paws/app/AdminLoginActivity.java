package com.shrinjal.care4paws.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent; // 🔥 ADD
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    EditText adminEmail, adminPassword;
    Button btnAdminLogin;
    ProgressBar progressBar;

    private static final String ADMIN_EMAIL = "admin@care4paws.com";
    private static final String ADMIN_PASSWORD = "admin123";

    boolean isPasswordVisible = false; // 🔥 ADD

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminEmail = findViewById(R.id.adminEmail);
        adminPassword = findViewById(R.id.adminPassword);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);
        progressBar = findViewById(R.id.adminProgress);

        // 👁️ EYE TOGGLE LOGIC (ADDED ONLY THIS)
        adminPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {

                if (event.getRawX() >= (adminPassword.getRight()
                        - adminPassword.getCompoundDrawables()[2].getBounds().width())) {

                    isPasswordVisible = !isPasswordVisible;

                    if (isPasswordVisible) {
                        adminPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    } else {
                        adminPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }

                    adminPassword.setSelection(adminPassword.getText().length());
                    return true;
                }
            }
            return false;
        });

        btnAdminLogin.setOnClickListener(v -> loginAdmin());
    }

    private void loginAdmin() {

        String email = adminEmail.getText().toString().trim();
        String password = adminPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            adminEmail.setError("Email required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            adminPassword.setError("Password required");
            return;
        }

        if (email.equalsIgnoreCase(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {

            Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, AdminDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid Admin Credentials", Toast.LENGTH_SHORT).show();
        }
    }
}
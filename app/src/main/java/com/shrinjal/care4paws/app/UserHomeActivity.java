package com.shrinjal.care4paws.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class UserHomeActivity extends AppCompatActivity {

    TextView tvWelcome;
    Button btnReport, btnMyReports, btnLogout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnReport = findViewById(R.id.btnReportAnimal);
        btnMyReports = findViewById(R.id.btnMyReports);
        btnLogout = findViewById(R.id.btnUserLogout);

        tvWelcome.setText("Welcome to Care4Paws 🐾");

        btnReport.setOnClickListener(v ->
                startActivity(new Intent(this, ReportAnimalActivity.class))
        );

        btnMyReports.setOnClickListener(v ->
                startActivity(new Intent(this, MyReportsActivity.class))
        );

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, IntroActivity.class));

        });
    }
}

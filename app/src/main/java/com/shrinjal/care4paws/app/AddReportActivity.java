package com.shrinjal.care4paws.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.shrinjal.care4paws.data.AppDatabase;
import com.shrinjal.care4paws.data.entity.ReportEntity;

public class AddReportActivity extends AppCompatActivity {

    EditText etAnimal;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

        etAnimal = findViewById(R.id.etReport);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {

            String animal = etAnimal.getText().toString().trim();

            if (animal.isEmpty()) {
                Toast.makeText(this, "Enter animal type", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔥 SAFE LOGIN CHECK
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Toast.makeText(this, "Login required", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔥 GET USER ID
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // 🔥 TEMP VALUES
            double latitude = 19.0760;
            double longitude = 72.8777;
            String status = "Pending";
            String imagePath = null;

            // 🔥 DEBUG (OPTIONAL but useful)
            System.out.println("SAVING REPORT FOR USER: " + userId);

            new Thread(() -> {

                try {
                    AppDatabase db = AppDatabase.getInstance(getApplicationContext());

                    ReportEntity report = new ReportEntity(
                            animal,
                            latitude,
                            longitude,
                            status,
                            imagePath,
                            userId // 🔥 MOST IMPORTANT
                    );

                    db.reportDao().insert(report);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Report Added ✅", Toast.LENGTH_SHORT).show();
                        etAnimal.setText("");
                        finish();
                    });

                } catch (Exception e) {
                    e.printStackTrace();

                    runOnUiThread(() ->
                            Toast.makeText(this, "Error saving report", Toast.LENGTH_SHORT).show()
                    );
                }

            }).start();
        });
    }
}
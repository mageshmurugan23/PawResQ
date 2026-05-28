package com.shrinjal.care4paws.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shrinjal.care4paws.data.AppDatabase;
import com.shrinjal.care4paws.data.entity.FeedbackEntity;

public class AddFeedbackActivity extends AppCompatActivity {

    EditText etFeedback;
    Button btnSubmit;

    FirebaseFirestore fStore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback);

        etFeedback = findViewById(R.id.etFeedback);
        btnSubmit = findViewById(R.id.btnSubmit);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnSubmit.setOnClickListener(v -> {

            String text = etFeedback.getText().toString().trim();

            if (text.isEmpty()) {
                Toast.makeText(this, "Enter feedback", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔥 SAFE CHECK (important)
            if (mAuth.getCurrentUser() == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            String uid = mAuth.getCurrentUser().getUid();

            // 🔥 GET NAME FROM FIRESTORE
            fStore.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {

                        String name = documentSnapshot.getString("name");

                        if (name == null || name.trim().isEmpty()) {
                            name = "User";
                        }

                        String finalName = name;

                        new Thread(() -> {

                            try {
                                FeedbackEntity feedback = new FeedbackEntity();
                                feedback.message = text;
                                feedback.userName = finalName; // ✅ dynamic user name
                                feedback.timestamp = System.currentTimeMillis();

                                AppDatabase.getInstance(getApplicationContext())
                                        .feedbackDao()
                                        .insert(feedback);

                                runOnUiThread(() -> {
                                    Toast.makeText(this, "Feedback Added ✅", Toast.LENGTH_SHORT).show();
                                    etFeedback.setText(""); // 🔥 clear field
                                    finish();
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(() ->
                                        Toast.makeText(this, "Error saving feedback", Toast.LENGTH_SHORT).show()
                                );
                            }

                        }).start();

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to get name", Toast.LENGTH_SHORT).show();
                    });

        });
    }
}
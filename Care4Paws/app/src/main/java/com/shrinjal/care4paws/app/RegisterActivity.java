package com.shrinjal.care4paws.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextFullname, email, password, phone;
    Button btnRegister;
    ProgressBar progressBar;
    TextView loginRedirect;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        editTextFullname = findViewById(R.id.editTextFullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        loginRedirect = findViewById(R.id.textView3);

        btnRegister.setOnClickListener(v -> registerUser());

        loginRedirect.setOnClickListener(v ->
                startActivity(new Intent(RegisterActivity.this, Login_Activity.class))
        );
    }

    private void registerUser() {

        String fullname = editTextFullname.getText().toString().trim();
        String emailTxt = email.getText().toString().trim();
        String passwordTxt = password.getText().toString().trim();
        String phoneTxt = phone.getText().toString().trim();

        if (TextUtils.isEmpty(fullname)) {
            editTextFullname.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(emailTxt)) {
            email.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(passwordTxt) || passwordTxt.length() < 6) {
            password.setError("Min 6 chars");
            return;
        }
        if (TextUtils.isEmpty(phoneTxt)) {
            phone.setError("Required");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(emailTxt, passwordTxt)
                .addOnSuccessListener(authResult -> {

                    String uid = mAuth.getCurrentUser().getUid();

                    Map<String, Object> user = new HashMap<>();
                    user.put("name", fullname);
                    user.put("email", emailTxt);
                    user.put("phone", phoneTxt);

                    fStore.collection("users").document(uid).set(user);

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(this, Login_Activity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}

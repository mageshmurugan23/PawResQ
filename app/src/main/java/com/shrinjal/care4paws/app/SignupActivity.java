package com.shrinjal.care4paws.app;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
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

public class SignupActivity extends AppCompatActivity {

    EditText editTextFullname, email, password, confirmPassword;
    Button btnRegister;
    ProgressBar progressBar;
    TextView txtLogin;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    boolean isPasswordVisible = false;
    boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        editTextFullname = findViewById(R.id.editTextFullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);
        progressBar = findViewById(R.id.progressBar);

        txtLogin.setPaintFlags(
                txtLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG
        );

        // 👁️ PASSWORD TOGGLE
        password.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {

                if (event.getRawX() >= (password.getRight()
                        - password.getCompoundDrawables()[2].getBounds().width())) {

                    isPasswordVisible = !isPasswordVisible;

                    if (isPasswordVisible) {
                        password.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    } else {
                        password.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }

                    password.setSelection(password.getText().length());
                    return true;
                }
            }
            return false;
        });

        // 👁️ CONFIRM PASSWORD TOGGLE
        confirmPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {

                if (event.getRawX() >= (confirmPassword.getRight()
                        - confirmPassword.getCompoundDrawables()[2].getBounds().width())) {

                    isConfirmPasswordVisible = !isConfirmPasswordVisible;

                    if (isConfirmPasswordVisible) {
                        confirmPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    } else {
                        confirmPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }

                    confirmPassword.setSelection(confirmPassword.getText().length());
                    return true;
                }
            }
            return false;
        });

        btnRegister.setOnClickListener(v -> registerUser());

        txtLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, Login_Activity.class));
            finish();
        });
    }

    private void registerUser() {

        String name = editTextFullname.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(mail)
                || TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnSuccessListener(authResult -> {

                    String uid = mAuth.getCurrentUser().getUid();

                    Map<String, Object> user = new HashMap<>();
                    user.put("name", name);
                    user.put("email", mail);

                    fStore.collection("users").document(uid).set(user);

                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(SignupActivity.this, Login_Activity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
package com.shrinjal.care4paws.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

public class Login_Activity extends AppCompatActivity {

    EditText email, password;
    Button btnLogin;
    TextView txtSignup, txtForgot;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // ❌ AUTO LOGIN REMOVED

        // Views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignup = findViewById(R.id.txtSignup);
        txtForgot = findViewById(R.id.txtForgot);
        progressBar = findViewById(R.id.progressBar);

        // LOGIN BUTTON
        btnLogin.setOnClickListener(v -> loginUser());

        // SIGN UP
        txtSignup.setOnClickListener(v ->
                startActivity(new Intent(this, SignupActivity.class))
        );

        // FORGOT PASSWORD
        txtForgot.setOnClickListener(v -> {
            String mail = email.getText().toString().trim();
            if (TextUtils.isEmpty(mail)) {
                Toast.makeText(this, "Enter email to reset password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(mail)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(this, "Reset link sent to email", Toast.LENGTH_LONG).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show()
                    );
        });

        // PASSWORD EYE TOGGLE
        password.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (password.getCompoundDrawables()[2] != null &&
                        event.getRawX() >= (password.getRight()
                                - password.getCompoundDrawables()[2].getBounds().width())) {

                    if (isPasswordVisible) {
                        password.setInputType(
                                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                        );
                        password.setCompoundDrawablesWithIntrinsicBounds(
                                0, 0, R.drawable.ic_eye, 0
                        );
                        isPasswordVisible = false;
                    } else {
                        password.setInputType(
                                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        );
                        password.setCompoundDrawablesWithIntrinsicBounds(
                                0, 0, R.drawable.ic_eye_closed, 0
                        );
                        isPasswordVisible = true;
                    }

                    password.setSelection(password.getText().length());
                    return true;
                }
            }
            return false;
        });
    }

    private void loginUser() {
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Email and Password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnSuccessListener(authResult -> {

                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);

                    Toast.makeText(this,
                            "Logged in as: " + authResult.getUser().getEmail(),
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
} 
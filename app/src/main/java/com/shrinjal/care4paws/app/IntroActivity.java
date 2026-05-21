package com.shrinjal.care4paws.app;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Text views
        TextView txtLetsGet = findViewById(R.id.txtLetsGet);
        TextView txtStarted = findViewById(R.id.txtStarted);
        TextView txtSignupNow = findViewById(R.id.txtSignupNow);

        // Buttons
        MaterialButton btnUserLogin = findViewById(R.id.btnUserLogin);
        MaterialButton btnAdminLogin = findViewById(R.id.btnAdminLogin);

        // Animation for "LET'S GET STARTED"
        Animation textAnim = AnimationUtils.loadAnimation(this, R.anim.text_slide_fade);

        txtLetsGet.startAnimation(textAnim);

        // Small delay for better effect
        txtStarted.postDelayed(() ->
                txtStarted.startAnimation(textAnim), 300);

        // Underline SIGN UP NOW
        txtSignupNow.setPaintFlags(
                txtSignupNow.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG
        );

        // USER LOGIN click
        btnUserLogin.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, Login_Activity.class);
            startActivity(intent);
        });

        // ADMIN LOGIN click
        btnAdminLogin.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, AdminLoginActivity.class);
            startActivity(intent);
        });

        // SIGN UP NOW click
        txtSignupNow.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}

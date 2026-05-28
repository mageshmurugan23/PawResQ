package com.shrinjal.care4paws.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        MaterialButton btnContinue = findViewById(R.id.btnContinue);

        // Button subtle pulse animation
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        btnContinue.startAnimation(pulse);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Optional click animation
                v.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .withEndAction(() -> {
                            v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();

                            // Move to next screen
                            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.slide_in_left,
                                    android.R.anim.slide_out_right);
                        })
                        .start();
            }
        });
    }
}

package com.br.leili_ferreira.dlarhoje;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler(). postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new
                        Intent(getBaseContext(),MainActivity.class));
                finish();
            }
        },3200);
    }
}
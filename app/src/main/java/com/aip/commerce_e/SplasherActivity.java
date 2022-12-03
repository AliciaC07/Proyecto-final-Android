package com.aip.commerce_e;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

public class SplasherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splasher);

        Objects.requireNonNull(getSupportActionBar()).hide();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            if(MainActivity.userLogged != null)
                intent = new Intent(this, MainActivity.class);
            else
                intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }, 3000);
    }
}
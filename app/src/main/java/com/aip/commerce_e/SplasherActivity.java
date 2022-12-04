package com.aip.commerce_e;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.aip.commerce_e.databinding.ActivityMainBinding;
import com.aip.commerce_e.databinding.ActivitySplasherBinding;

import java.util.Objects;

public class SplasherActivity extends AppCompatActivity {

    private ActivitySplasherBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splasher);
        binding = ActivitySplasherBinding.inflate(getLayoutInflater());

        Objects.requireNonNull(getSupportActionBar()).hide();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            if(MainActivity.userLogged != null)
                intent = new Intent(this, MainActivity.class);
            else
                intent = new Intent(this, LoginActivity.class);
            binding.progressBar3.setProgress(0);

            startActivity(intent);

        }, 3000);
    }
}
package com.example.postcutter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonChoose = findViewById(R.id.main_B_choose);
        buttonChoose.setOnClickListener(e -> startCutterActivity());

        ImageButton buttonSettings = findViewById(R.id.main_settings);
        buttonSettings.setOnClickListener(e -> startSettingsActivity());
    }

    private void startCutterActivity(){
        Intent intent = new Intent(this, CutterActivity.class);
        startActivity(intent);
    }

    private void startSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
package com.example.postcutter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public static final String IMAGE_DATA = "image data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonChoose = findViewById(R.id.main_B_choose);
        buttonChoose.setOnClickListener(e -> loadImage());

        ImageButton buttonSettings = findViewById(R.id.main_settings);
        buttonSettings.setOnClickListener(e -> startSettingsActivity());
    }

    private void startSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void loadImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Pick an image"), 1);//TODO string file
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Intent intent = new Intent(this, CutterActivity.class);
            intent.putExtra(IMAGE_DATA, data.getData().toString());
            startActivity(intent);
        }
    }
}
package com.example.postcutter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageDetaylActivity extends AppCompatActivity {
    private String imagePath;
    private SubsamplingScaleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detayl);

//        TODO zdielanie obrazka
//        Intent intent = getIntent();
//        try {
//            InputStream inputStream;
//            if (Intent.ACTION_SEND.equals(intent.getAction()) && intent.getType() != null) {
//                Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
//                inputStream = getContentResolver().openInputStream(imageUri);
//
//            } else {
//                inputStream = getContentResolver().openInputStream(Uri.parse(imagePath));
//            }
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//            eraseView.getImageView().setImageBitmap(bitmap);
//        }catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        imagePath = getIntent().getStringExtra("imagePath");

        imageView = findViewById(R.id.idIVImage);
        imageView.setImage(ImageSource.uri(imagePath));

        ImageButton buttonCutter = findViewById(R.id.imageDetail_cutter);
        buttonCutter.setOnClickListener(e -> openCutterActivity(imagePath));

        ImageButton buttonTextErase = findViewById(R.id.imageDetail_textDelete);
        buttonTextErase.setOnClickListener(e -> openTextEraseActivity(imagePath));
    }

    private void openCutterActivity(String imagePath) {
        Intent i = new Intent(this, CutterActivity.class);
        i.putExtra("imagePath", imagePath);
        startActivity(i);
    }

    private void openTextEraseActivity(String imagePath) {
        Intent i = new Intent(this, TextEraseActivity.class);
        i.putExtra("imagePath", imagePath);
        startActivity(i);
    }
}
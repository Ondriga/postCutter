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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageDetailActivity extends AppCompatActivity {
    private static final String IMAGE_FILE = "selectedPicture";

    private SubsamplingScaleImageView imageView;
    private String tmpPictureFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detayl);

        imageView = findViewById(R.id.idIVImage);

        Bitmap imageBitmap;
        Intent intent = getIntent();
        try {
            if (Intent.ACTION_SEND.equals(intent.getAction()) && intent.getType() != null) {
                Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                imageBitmap = BitmapFactory.decodeStream(inputStream);
            } else {
                String imagePath = intent.getStringExtra("imagePath");
                File imgFile = new  File(imagePath);
                imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
            imageView.setImage(ImageSource.bitmap(imageBitmap));
            temporaryStorePicture(imageBitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ImageButton buttonCutter = findViewById(R.id.imageDetail_cutter);
        buttonCutter.setOnClickListener(e -> openCutterActivity());

        ImageButton buttonTextErase = findViewById(R.id.imageDetail_textDelete);
        buttonTextErase.setOnClickListener(e -> openTextEraseActivity());
    }

    private void temporaryStorePicture(Bitmap bitmap) {
        try {
            File file = File.createTempFile(IMAGE_FILE, null);
            this.tmpPictureFilePath = file.getPath();
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openCutterActivity() {
        Intent i = new Intent(this, CutterActivity.class);
        i.putExtra("imgCachePath", this.tmpPictureFilePath);
        startActivity(i);
    }

    private void openTextEraseActivity() {
        Intent i = new Intent(this, TextEraseActivity.class);
        i.putExtra("imgCachePath", this.tmpPictureFilePath);
        startActivity(i);
    }
}
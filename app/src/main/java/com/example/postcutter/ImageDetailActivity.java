package com.example.postcutter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
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
    public static final String IMG_CACHE_FILE_NAME = "imgCachePath";

    private String tmpPictureFilePath;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detayl);

        SubsamplingScaleImageView imageView = findViewById(R.id.idIVImage);

        Bitmap imageBitmap;
        Intent intent = getIntent();
        try {
            if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getType() != null) {
                Uri imageUri = (Uri) intent.getData();
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                imageBitmap = BitmapFactory.decodeStream(inputStream);
            } else if (Intent.ACTION_SEND.equals(intent.getAction()) && intent.getType() != null) {
                Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                imageBitmap = BitmapFactory.decodeStream(inputStream);
            } else {
                imagePath = intent.getStringExtra("imagePath");
                File imgFile = new File(imagePath);
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

        ImageButton buttonShare = findViewById(R.id.imageDetail_share);
        buttonShare.setOnClickListener(e -> shareImage());

        ImageButton buttonImgDelete = findViewById(R.id.imageDetail_imgDelete);
        buttonImgDelete.setOnClickListener(e -> deleteImage());
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
        i.putExtra(IMG_CACHE_FILE_NAME, this.tmpPictureFilePath);
        startActivity(i);
    }

    private void openTextEraseActivity() {
        Intent i = new Intent(this, TextEraseActivity.class);
        i.putExtra(IMG_CACHE_FILE_NAME, this.tmpPictureFilePath);
        startActivity(i);
    }

    private void shareImage() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        File photoFile = new File(imagePath);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));
        shareIntent.setType("image/jpg");

        startActivity(Intent.createChooser(shareIntent, null));
    }

    private void deleteImage() {
        String[] retCol = { MediaStore.Images.Media._ID };
        Cursor cur = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                retCol,
                MediaStore.MediaColumns.DATA + "='" + imagePath + "'", null, null
        );
        if (cur.getCount() == 0) {
            return;
        }
        cur.moveToFirst();
        int id = cur.getInt(cur.getColumnIndex(MediaStore.MediaColumns._ID));
        cur.close();

        Uri uri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
        );
        getContentResolver().delete(uri, null, null);

        finish();
    }
}
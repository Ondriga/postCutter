package com.example.postcutter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.postcutter.customViews.EraseView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import postCutter.Cutter;

public class CutterActivity extends AppCompatActivity {
    private Cutter cutter;
    private final LoadingDialog loadingDialog = new LoadingDialog(CutterActivity.this);

    private EraseView eraseView;

    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        OpenCVLoader.initDebug();
        System.loadLibrary("opencv_java3");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutter);

        ActivityCompat.requestPermissions(CutterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        String path = getIntent().getStringExtra(ImageDetailActivity.IMG_CACHE_FILE_NAME);
        File file = new File(path);
        imageBitmap = BitmapFactory.decodeFile(file.getPath());

        eraseView = findViewById(R.id.cutter_eraseView);
        ImageButton cutButton = findViewById(R.id.cutter_imageButton);
        cutButton.setOnClickListener(e -> saveImageToGallery());

        this.cutter = new Cutter();
        loadImage();
    }

    private void loadImage() {
        this.loadingDialog.startLoadingDialog();//Start loading screen

        eraseView.loadPicture(imageBitmap);
        Mat mat = new Mat();
        Utils.bitmapToMat(imageBitmap, mat);
        ImageProcess imageProcess = new ImageProcess(mat);
        imageProcess.start();
    }

    private void saveImageToGallery() {
        cutter.setRectangle(eraseView.getRectangle());

        Mat imageMat = cutter.getCroppedImage();
        Bitmap bitmap = Bitmap.createBitmap(imageMat.width(), imageMat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imageMat, bitmap);

        String fileName = String.format("picture_%d", System.currentTimeMillis());
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, fileName, "");

        Intent intent = new Intent();
        intent.putExtra(ImageDetailActivity.IMG_RETURN_PATH, path);
        setResult(ImageDetailActivity.RETURN_REQUEST_CODE, intent);
        finish();
    }

    private class ImageProcess extends Thread {
        private final Mat picture;

        private ImageProcess(Mat picture) {
            this.picture = picture;
        }

        @Override
        public void run() {
            cutter.loadPicture(this.picture);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    eraseView.setRectangle(cutter.getRectangle());
                }
            });
            loadingDialog.stopLoadingDialog();
        }
    }
}

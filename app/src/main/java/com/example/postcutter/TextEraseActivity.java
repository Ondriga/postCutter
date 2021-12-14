package com.example.postcutter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import com.example.postcutter.customViews.EraseView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import inpainting.Inpainter;

public class TextEraseActivity extends AppCompatActivity {
    private Bitmap imageBitmap;
    private EraseView eraseView;
    private Mat originalImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        OpenCVLoader.initDebug();
        System.loadLibrary("opencv_java3");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_erase);

        String path = getIntent().getStringExtra(ImageDetailActivity.IMG_CACHE_FILE_NAME);
        File file = new File(path);
        imageBitmap = BitmapFactory.decodeFile(file.getPath());

        eraseView = findViewById(R.id.textErase_eraseView);

        Button button = findViewById(R.id.textErase_button);
        button.setOnClickListener(e -> doClick());

        originalImage = new Mat();
        Utils.bitmapToMat(this.imageBitmap, this.originalImage);
        eraseView.loadPicture(this.imageBitmap);
    }

    private void doClick() {
        Mat convertMat = new Mat();
        Imgproc.cvtColor(originalImage, convertMat, Imgproc.COLOR_RGBA2RGB);

        Mat imageMat = Inpainter.inpainging(convertMat, eraseView.getRectangle());

        Bitmap bitmap = Bitmap.createBitmap(imageMat.width(), imageMat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imageMat, bitmap);

        String fileName = String.format("picture_%d", System.currentTimeMillis());
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, fileName, "");

        Intent intent = new Intent();
        intent.putExtra(ImageDetailActivity.IMG_RETURN_PATH, path);
        setResult(ImageDetailActivity.RETURN_REQUEST_CODE, intent);
        finish();
    }

}
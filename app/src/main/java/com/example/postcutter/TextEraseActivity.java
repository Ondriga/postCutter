package com.example.postcutter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.postcutter.customViews.EraseView;
import com.example.postcutter.dialogs.TextEraseMethodDialog;
import com.example.postcutter.functions.ImageAction;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class TextEraseActivity extends AppCompatActivity {
    private EraseView eraseView;
    private Mat originalImage;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        OpenCVLoader.initDebug();
        System.loadLibrary("opencv_java3");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_erase);

        path = getIntent().getStringExtra(ImageDetailActivity.IMG_PATH);
        Bitmap imageBitmap = ImageAction.getImageOrientedCorrect(path);

        eraseView = findViewById(R.id.textErase_eraseView);

        ImageButton button = findViewById(R.id.textErase_button);
        button.setOnClickListener(e -> doClick());

        originalImage = new Mat();
        Utils.bitmapToMat(imageBitmap, this.originalImage);
        eraseView.loadPicture(imageBitmap);
    }

    private void doClick() {
        Mat convertMat = new Mat();
        Imgproc.cvtColor(originalImage, convertMat, Imgproc.COLOR_RGBA2RGB);

        new TextEraseMethodDialog(TextEraseActivity.this, convertMat, eraseView.getRectangle(), path);
    }

}
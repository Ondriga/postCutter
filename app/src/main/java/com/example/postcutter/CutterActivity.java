package com.example.postcutter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;

import com.example.postcutter.customViews.EraseView;
import com.example.postcutter.dialogs.LoadingDialog;
import com.example.postcutter.dialogs.SettingDialog;
import com.example.postcutter.functions.ImageAction;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;

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

        String path = getIntent().getStringExtra(ImageDetailActivity.IMG_PATH);
        File file = new File(path);
        imageBitmap = BitmapFactory.decodeFile(file.getPath());

        eraseView = findViewById(R.id.cutter_eraseView);
        ImageButton cutButton = findViewById(R.id.cutter_imageButton);
        cutButton.setOnClickListener(e -> saveImageToGallery());

        ImageButton settingsButton = findViewById(R.id.cutter_settingsButton);
        settingsButton.setOnClickListener(e -> startSettingDialog());

        this.cutter = new Cutter();
        SharedPreferences sharedPreferences = getSharedPreferences(SettingDialog.SHARED_PREFS, MODE_PRIVATE);
        boolean[] methodsPermissions = {false, false, false, false};
        for(int i=0; i<sharedPreferences.getInt(SettingDialog.SUGGESTION_ACCURACY, SettingDialog.ACCURACY_DEFAULT); i++) {
            methodsPermissions[i] = true;
        }
        this.cutter.setMethodsPermission(
                methodsPermissions[3],
                methodsPermissions[2],
                methodsPermissions[1],
                methodsPermissions[0]
        );
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

        String path = ImageAction.saveAsNew(CutterActivity.this, bitmap);

        Intent intent = new Intent();
        intent.putExtra(ImageDetailActivity.IMG_RETURN_PATH, path);
        setResult(ImageDetailActivity.RETURN_REQUEST_CODE, intent);
        finish();
    }

    private void startSettingDialog() {
        new SettingDialog(CutterActivity.this);
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
                    eraseView.activateBreakpoints(cutter.getHorizontalLines(), cutter.getVerticalLines(), CutterActivity.this);
                }
            });
            loadingDialog.stopLoadingDialog();
        }
    }
}

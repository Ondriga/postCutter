package com.example.postcutter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.postcutter.customViews.EraseView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import postCutter.Cutter;

public class CutterActivity extends AppCompatActivity {
    private Cutter cutter;
    private final LoadingDialog loadingDialog = new LoadingDialog(CutterActivity.this);

    private EraseView eraseView;

    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        OpenCVLoader.initDebug();
        System.loadLibrary("opencv_java3");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutter);

        ActivityCompat.requestPermissions(CutterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        imagePath = getIntent().getStringExtra("imagePath");

        eraseView = findViewById(R.id.cutter_eraseView);
        ImageButton cutButton = findViewById(R.id.cutter_imageButton);
        cutButton.setOnClickListener(e -> saveImageToGallery());

        this.cutter = new Cutter();
        loadImage();
    }

    private void loadImage() {
        this.loadingDialog.startLoadingDialog();//Start loading screen

        eraseView.loadPicture(imagePath);
        Mat mat = Imgcodecs.imread(imagePath);
        ImageProcess imageProcess = new ImageProcess(mat);
        imageProcess.start();
    }

    private void saveImageToGallery() {
        FileOutputStream outputStream = null;
        //TODO change BE
        cutter.getRectangle().setCornerA(eraseView.getRectangle().getCornerA());
        cutter.getRectangle().setCornerB(eraseView.getRectangle().getCornerB());
        //TODO change BE
        Mat imageMat = cutter.getCroppedImage();
        Bitmap bitmap = Bitmap.createBitmap(imageMat.width(), imageMat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imageMat, bitmap);

        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/PostCutterPics/");
        dir.mkdirs();

        String fileName = String.format("picture_%d.png", System.currentTimeMillis());
        File outFile = new File(dir, fileName);

        try {
            outputStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            Toast.makeText(getApplicationContext(), "Image saved!", Toast.LENGTH_LONG).show();
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

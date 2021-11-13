package com.example.postcutter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.postcutter.cutter.CutterGui;
import com.squareup.picasso.Picasso;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import postCutter.Cutter;
import postCutter.geometricShapes.Coordinate;
import postCutter.geometricShapes.rectangle.MyRectangle;

public class CutterActivity extends AppCompatActivity {
    private CutterGui cutterGui;
    private Cutter cutter;
    private final LoadingDialog loadingDialog = new LoadingDialog(CutterActivity.this);

    private ImageView imageView;
    private ImageButton cutButton;

    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        OpenCVLoader.initDebug();
        System.loadLibrary("opencv_java3");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutter);

        ActivityCompat.requestPermissions(CutterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        imagePath = getIntent().getStringExtra("imagePath");

        imageView = findViewById(R.id.cutter_image);
        cutButton = findViewById(R.id.cutter_imageButton);

        this.cutter = new Cutter();
        cutterGui = new CutterGui(this);
        loadImage();

        cutButton.setOnClickListener(e -> saveImageToGallery());
    }

    private void loadImage() {
        this.loadingDialog.startLoadingDialog();//Start loading screen

        Picasso.get().load("file:" + imagePath).into(imageView);

        Mat mat = Imgcodecs.imread(imagePath);

        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                Coordinate coordinateA = new Coordinate(imageView.getLeft(), imageView.getTop());
                Coordinate coordinateB = new Coordinate(imageView.getRight(), imageView.getBottom());
                MyRectangle imageRectangle = MyRectangle.createRectangle(coordinateA, coordinateB);

                ImageProcess imageProcess = new ImageProcess(mat, imageRectangle);
                imageProcess.start();
            }
        });
    }

    private void saveImageToGallery() {
        FileOutputStream outputStream = null;
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
        private final MyRectangle imageRectangle;

        private ImageProcess(Mat picture, MyRectangle imageRectangle) {
            this.picture = picture;
            this.imageRectangle = imageRectangle;
        }

        @Override
        public void run() {
            cutter.loadPicture(this.picture);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cutterGui.loadImage(cutter, imageRectangle, imageView.getDrawable().getIntrinsicWidth(), imageView.getDrawable().getIntrinsicHeight());
                }
            });

            loadingDialog.stopLoadingDialog();
        }
    }
}

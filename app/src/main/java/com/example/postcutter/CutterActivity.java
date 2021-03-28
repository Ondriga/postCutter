package com.example.postcutter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.postcutter.cutter.CutterGui;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        OpenCVLoader.initDebug();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutter);

        this.cutter = new Cutter();
        cutterGui = new CutterGui(this);
        loadImage();
        ImageButton cutButton = findViewById(R.id.cutter_imageButton);

        ActivityCompat.requestPermissions(CutterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(CutterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        cutButton.setOnClickListener(e -> saveImageToGallery());
    }

    private void loadImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Pick an image"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ImageView imageView = findViewById(R.id.cutter_image);
                imageView.setImageBitmap(bitmap);

                Mat mat = new Mat();
                Utils.bitmapToMat(bitmap, mat);
                cutter.loadPicture(mat);

                imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        System.loadLibrary("opencv_java3");

                        ImageView imageView = findViewById(R.id.cutter_image);
                        imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        Coordinate coordinateA = new Coordinate(imageView.getLeft(), imageView.getTop());
                        Coordinate coordinateB = new Coordinate(imageView.getRight(),  imageView.getBottom());
                        MyRectangle imageRectangle = MyRectangle.createRectangle(coordinateA, coordinateB);

                        cutterGui.prepare(cutter.getRectangle(), imageRectangle, imageView.getDrawable().getIntrinsicWidth(), imageView.getDrawable().getIntrinsicHeight());
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImageToGallery(){
        FileOutputStream outputStream = null;
        Mat imageMat = cutter.getCroppedImage();
        Bitmap bitmap = Bitmap.createBitmap(imageMat.width(), imageMat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imageMat, bitmap);

        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/PostCutterPics");
        dir.mkdirs();

        String fileName = String.format("picture_%d", System.currentTimeMillis());
        File outFile = new File(dir, fileName);

        try {
            outputStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

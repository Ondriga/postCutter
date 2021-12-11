package com.example.postcutter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
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

        String path = getIntent().getStringExtra("imgCachePath");
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

        FileOutputStream outputStream = null;
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

}
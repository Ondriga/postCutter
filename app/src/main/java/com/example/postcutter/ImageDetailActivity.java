package com.example.postcutter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageButton;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.postcutter.functions.ImageAction;

import java.io.File;

public class ImageDetailActivity extends AppCompatActivity {
    public static final String IMG_PATH = "imgPathSend";
    public static final String IMG_RETURN_PATH = "returnImgPath";
    public static final int RETURN_REQUEST_CODE = 5;

    private String imagePath;

    private SubsamplingScaleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detayl);

        imageView = findViewById(R.id.idIVImage);

        ImageButton buttonCutter = findViewById(R.id.imageDetail_cutter);
        ImageButton buttonTextErase = findViewById(R.id.imageDetail_textDelete);
        ImageButton buttonShare = findViewById(R.id.imageDetail_share);
        ImageButton buttonImgDelete = findViewById(R.id.imageDetail_imgDelete);

        Bitmap imageBitmap;
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getType() != null) {
            Uri imageUri = (Uri) intent.getData();
            imagePath = ImageAction.getRealPathFromURI(ImageDetailActivity.this, imageUri);
        } else {
            imagePath = intent.getStringExtra("imagePath");
        }
        File imgFile = new File(imagePath);
        imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        imageView.setImage(ImageSource.bitmap(imageBitmap));

        buttonCutter.setOnClickListener(e -> openCutterActivity());
        buttonTextErase.setOnClickListener(e -> openTextEraseActivity());
        buttonShare.setOnClickListener(e -> shareImage());
        buttonImgDelete.setOnClickListener(e -> deleteImage());
    }

    private void openCutterActivity() {
        Intent i = new Intent(this, CutterActivity.class);
        i.putExtra(IMG_PATH, this.imagePath);
        startActivityForResult(i, RETURN_REQUEST_CODE);
    }

    private void openTextEraseActivity() {
        Intent i = new Intent(this, TextEraseActivity.class);
        i.putExtra(IMG_PATH, this.imagePath);
        startActivityForResult(i, RETURN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RETURN_REQUEST_CODE && data != null) {
            imagePath = data.getStringExtra(IMG_RETURN_PATH);

            File imgFile = new File(imagePath);
            Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImage(ImageSource.bitmap(imageBitmap));
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ImageDetailActivity.this);
        builder.setMessage(R.string.delete_dialog_title)
                .setPositiveButton(R.string.delte, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ImageAction.delete(ImageDetailActivity.this, imagePath);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }
}
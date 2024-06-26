package postCutter.fe.postcutter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageButton;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import postCutter.fe.postcutter.R;

import postCutter.fe.postcutter.dialogs.HelpDialog;
import postCutter.fe.postcutter.functions.BarAnimationHandler;
import postCutter.fe.postcutter.functions.Help;
import postCutter.fe.postcutter.functions.ImageAction;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

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

        requestPermissions();

        buttonCutter.setOnClickListener(e -> openCutterActivity());
        buttonTextErase.setOnClickListener(e -> openTextEraseActivity());
        buttonShare.setOnClickListener(e -> shareImage());
        buttonImgDelete.setOnClickListener(e -> deleteImage());

        ConstraintLayout bottomBar = findViewById(R.id.imageDetail_bottomBar);
        BarAnimationHandler.Builder builder = new BarAnimationHandler.Builder();
        BarAnimationHandler barAnimationHandler = builder.bottomBar(bottomBar).build();

        ConstraintLayout mainLayout = findViewById(R.id.imageDetail_mainLayout);
        mainLayout.setOnClickListener(e -> barAnimationHandler.showHide());
        imageView.setOnClickListener(e -> barAnimationHandler.showHide());

        this.prepareHelp();
    }

    private void prepareHelp() {
        List<Help> helps = new ArrayList<>();
        helps.add(new Help(getResources().getString(R.string.help_detail_button_crop), R.drawable.detail__button_crop));
        helps.add(new Help(getResources().getString(R.string.help_detail_button_replace), R.drawable.detail__button_replace));
        helps.add(new Help(getResources().getString(R.string.help_detail_button_share), R.drawable.detail__button_share));
        helps.add(new Help(getResources().getString(R.string.help_detail_button_delete), R.drawable.detail__button_delete));

        SharedPreferences sharedPreferences = getSharedPreferences(Help.SHARED_PREFS, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Help.DETAIL_HELP, true)) {
            sharedPreferences.edit().putBoolean(Help.DETAIL_HELP, false).commit();
            HelpDialog helpDialog = new HelpDialog(this, helps);
            helpDialog.show();
        }
    }

    private void prepareImage() {
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getType() != null) {
            Uri imageUri = (Uri) intent.getData();
            imagePath = ImageAction.getRealPathFromURI(ImageDetailActivity.this, imageUri);
        } else {
            imagePath = intent.getStringExtra("imagePath");
        }

        imageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
        imageView.setImage(ImageSource.uri(imagePath));
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

            imageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
            imageView.setImage(ImageSource.uri(imagePath));
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
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_dialog_title)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImageAction.delete(ImageDetailActivity.this, imagePath);
                        finish();
                    }
                }).show();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ImageDetailActivity.this, READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        if (checkPermission()) {
            prepareImage();
        } else {
            ActivityCompat.requestPermissions(ImageDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (storageAccepted) {
                    Toast.makeText(this, getString(R.string.permission_granted_text), Toast.LENGTH_SHORT).show();
                    prepareImage();
                } else {
                    Toast.makeText(this, getString(R.string.permission_denied_text), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
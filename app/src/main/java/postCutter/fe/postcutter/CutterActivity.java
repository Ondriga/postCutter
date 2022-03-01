package postCutter.fe.postcutter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import postCutter.fe.postcutter.R;

import postCutter.fe.postcutter.customViews.EraseView;
import postCutter.fe.postcutter.dialogs.CutterSaveDialog;
import postCutter.fe.postcutter.dialogs.LoadingDialog;
import postCutter.fe.postcutter.dialogs.SettingDialog;
import postCutter.fe.postcutter.functions.BarAnimationHandler;
import postCutter.fe.postcutter.functions.ImageAction;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import postCutter.Cutter;

public class CutterActivity extends AppCompatActivity {
    private Cutter cutter;
    private final LoadingDialog loadingDialog = new LoadingDialog(CutterActivity.this);

    private String path;
    private boolean allowSuggestion = true;

    private EraseView eraseView;

    private Bitmap imageBitmap;

    private BarAnimationHandler barAnimationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        OpenCVLoader.initDebug();
        System.loadLibrary("opencv_java3");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutter);

        ActivityCompat.requestPermissions(CutterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        path = getIntent().getStringExtra(ImageDetailActivity.IMG_PATH);
        imageBitmap = ImageAction.getImageOrientedCorrect(path);

        eraseView = findViewById(R.id.cutter_eraseView);
        ImageButton cutButton = findViewById(R.id.cutter_imageButton);
        cutButton.setOnClickListener(e -> saveImageToGallery());

        ImageButton settingsButton = findViewById(R.id.cutter_settingsButton);

        SettingDialog dialog = new SettingDialog(CutterActivity.this);
        settingsButton.setOnClickListener(e -> startSettingDialog(dialog));

        dialog.getDialog().setOnDismissListener(dialog1 -> {
            if (dialog.doReload()) {
                setCutter();
                processImage();
            }
        });

        setCutter();

        ConstraintLayout topBar = findViewById(R.id.cutter_topBar);
        ConstraintLayout bottomBar = findViewById(R.id.cutter_bottomBar);
        View screenBlocker = findViewById(R.id.cutter_screenBlocker);

        BarAnimationHandler.Builder builder = new BarAnimationHandler.Builder();
        barAnimationHandler = builder.topBar(topBar)
                .bottomBar(bottomBar)
                .screenBlocker(screenBlocker)
                .build();

        screenBlocker.setOnClickListener(e -> barAnimationHandler.showHide());

        ConstraintLayout mainLayout = findViewById(R.id.cutter_mainLayout);
        mainLayout.setOnClickListener(e -> barAnimationHandler.showHide());
        eraseView.getRectangleView().setOnClickListener(e -> barAnimationHandler.showHide());

        eraseView.loadPicture(imageBitmap);
        eraseView.activateBreakpoints(new ArrayList<>(), new ArrayList<>(), CutterActivity.this);
        processImage();
    }

    private void setCutter() {
        this.cutter = new Cutter(this.loadingDialog.getProgress());
        SharedPreferences sharedPreferences = getSharedPreferences(SettingDialog.SHARED_PREFS, MODE_PRIVATE);
        boolean[] methodsPermissions = {false, false, false, false};
        for (int i = 0; i < sharedPreferences.getInt(SettingDialog.SUGGESTION_ACCURACY, SettingDialog.ACCURACY_DEFAULT); i++) {
            methodsPermissions[i] = true;
        }
        this.cutter.setMethodsPermission(
                methodsPermissions[3],
                methodsPermissions[2],
                methodsPermissions[1],
                methodsPermissions[0]
        );
    }

    private void processImage() {
        Mat mat = new Mat();
        Utils.bitmapToMat(imageBitmap, mat);

        ImageProcess imageProcess = new ImageProcess(mat);
        this.loadingDialog.startLoadingDialog(imageProcess);//Start loading screen
        imageProcess.start();
    }

    private void saveImageToGallery() {
        cutter.setRectangle(eraseView.getRectangle());

        Mat imageMat = cutter.getCroppedImage();
        Bitmap bitmap = Bitmap.createBitmap(imageMat.width(), imageMat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imageMat, bitmap);

        new CutterSaveDialog(CutterActivity.this, bitmap, path);
    }

    private void startSettingDialog(SettingDialog dialog) {
        dialog.startDialog(allowSuggestion);
    }

    public class ImageProcess extends Thread {
        private final Mat picture;
        private final AtomicBoolean running = new AtomicBoolean(true);

        private ImageProcess(Mat picture) {
            this.picture = picture;
        }

        @Override
        public void run() {
            cutter.loadPicture(this.picture);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (running.get()) {
                        eraseView.setRectangle(cutter.getRectangle());
                        eraseView.activateBreakpoints(cutter.getHorizontalLines(), cutter.getVerticalLines(), CutterActivity.this);
                        allowSuggestion = true;
                    }
                    barAnimationHandler.showHide();
                }
            });
            loadingDialog.stopLoadingDialog();
        }

        public void stopThread() {
            this.running.set(false);
            cutter.stop();
            allowSuggestion = false;
        }
    }
}
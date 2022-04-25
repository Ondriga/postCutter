package postCutter.fe.postcutter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import postCutter.fe.postcutter.R;

import postCutter.fe.postcutter.customViews.EraseView;
import postCutter.fe.postcutter.dialogs.HelpDialog;
import postCutter.fe.postcutter.dialogs.TextEraseMethodDialog;
import postCutter.fe.postcutter.functions.BarAnimationHandler;
import postCutter.fe.postcutter.functions.Help;
import postCutter.fe.postcutter.functions.ImageAction;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class TextEraseActivity extends AppCompatActivity {
    private EraseView eraseView;
    private Mat originalImage;
    private String path;

    private List<Help> helps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        OpenCVLoader.initDebug();
        System.loadLibrary("opencv_java3");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_erase);

        prepareHelp();

        path = getIntent().getStringExtra(ImageDetailActivity.IMG_PATH);
        Bitmap imageBitmap = ImageAction.getImageOrientedCorrect(path);

        ImageButton helpButton = findViewById(R.id.textErase_help_button);
        helpButton.setOnClickListener(e -> showHelp());

        eraseView = findViewById(R.id.textErase_eraseView);

        ImageButton button = findViewById(R.id.textErase_button);
        button.setOnClickListener(e -> doClick());

        ConstraintLayout topBar = findViewById(R.id.textErase_topBar);
        ConstraintLayout bottomBar = findViewById(R.id.textErase_bottomBar);
        View screenBlocker = findViewById(R.id.textErase_screenBlocker);

        BarAnimationHandler.Builder builder = new BarAnimationHandler.Builder();
        BarAnimationHandler barAnimationHandler = builder
                .topBar(topBar)
                .bottomBar(bottomBar)
                .screenBlocker(screenBlocker)
                .build();

        screenBlocker.setOnClickListener(e -> barAnimationHandler.showHide());

        ConstraintLayout mainLayout = findViewById(R.id.textErase_mainLayout);
        mainLayout.setOnClickListener(e -> barAnimationHandler.showHide());

        originalImage = new Mat();
        Utils.bitmapToMat(imageBitmap, this.originalImage);
        eraseView.loadPicture(imageBitmap);
        eraseView.getRectangleView().setOnClickListener(e -> barAnimationHandler.showHide());

        int maxWidth = (int) (getResources().getDimension(R.dimen.max_rectangle_width));
        int maxHeight = (int) (getResources().getDimension(R.dimen.max_rectangle_height));
        eraseView.setMaxRectangle(maxWidth, maxHeight);
    }

    private void prepareHelp() {
        this.helps.add(new Help(getResources().getString(R.string.help_replace_hide), R.drawable.replace__hide));
        this.helps.add(new Help(getResources().getString(R.string.help_rectangle_resize), R.drawable.rectangle__resize));
        this.helps.add(new Help(getResources().getString(R.string.help_rectangle_move), R.drawable.rectangle__move));
        this.helps.add(new Help(getResources().getString(R.string.help_replace_show), R.drawable.replace__show));
        this.helps.add(new Help(getResources().getString(R.string.help_replace_save), R.drawable.replace__button_save));

        SharedPreferences sharedPreferences = getSharedPreferences(Help.SHARED_PREFS, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Help.REMOVE_HELP, true)) {
            sharedPreferences.edit().putBoolean(Help.REMOVE_HELP, false).commit();
            showHelp();
        }
    }

    private void showHelp() {
        HelpDialog helpDialog = new HelpDialog(this, this.helps);
        helpDialog.show();
    }

    private void doClick() {
        Mat convertMat = new Mat();
        Imgproc.cvtColor(originalImage, convertMat, Imgproc.COLOR_RGBA2RGB);

        new TextEraseMethodDialog(TextEraseActivity.this, convertMat, eraseView.getRectangle(), path);
    }

}
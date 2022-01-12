package com.example.postcutter.functions;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.RadioButton;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import postCutter.geometricShapes.Coordinate;
import postCutter.geometricShapes.rectangle.MyRectangle;

public class TextEraseMethod {
    private final static int AREA_AROUND_DETAIL = 60;

    private final Bitmap image;
    private final MyRectangle rectangle;
    private final RadioButton radioButton;
    private final Activity activity;

    public TextEraseMethod(Mat imageMat, Activity activity, RadioButton radioButton, MyRectangle rectangle) {
        this.radioButton = radioButton;
        this.rectangle = rectangle;
        this.activity = activity;

        image = Bitmap.createBitmap(imageMat.width(), imageMat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imageMat, image);

        radioButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getImgDetailOnErase());
    }

    public Bitmap getImage() {
        return image;
    }

    public RadioButton getRadioButton() {
        return radioButton;
    }

    private Drawable getImgDetailOnErase() {
        Coordinate cornerA = new Coordinate(
                Math.max(rectangle.getCornerA().getX() - AREA_AROUND_DETAIL, 0),
                Math.max(rectangle.getCornerA().getY() - AREA_AROUND_DETAIL, 0)
        );
        Coordinate cornerB = new Coordinate(
                Math.min(rectangle.getCornerB().getX() + AREA_AROUND_DETAIL, image.getWidth() - 1),
                Math.min(rectangle.getCornerB().getY() + AREA_AROUND_DETAIL, image.getHeight() - 1)
        );
        MyRectangle detailRectangle = MyRectangle.createRectangle(cornerA, cornerB);

        Bitmap imgDetail = Bitmap.createBitmap(
                image,
                detailRectangle.getCornerA().getX(),
                detailRectangle.getCornerA().getY(),
                detailRectangle.getWidth(),
                detailRectangle.getHeight()
        );

        return new BitmapDrawable(activity.getResources(), imgDetail);
    }

}

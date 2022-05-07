/*
 * Source code for the frontend of Bachelor thesis.
 * TextEraseMethod class
 *
 * (C) Patrik Ondriga (xondri08)
 */

package postCutter.fe.postcutter.functions;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.RadioButton;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import postCutter.geometricShapes.Coordinate;
import postCutter.geometricShapes.rectangle.MyRectangle;

/**
 * Representing method for replace area from image.
 */
public class TextEraseMethod {
    /// Constant for edition area around area of replace.
    public final static int AREA_AROUND_DETAIL = 60;

    /// Image for replaced area.
    private final Bitmap image;
    /// Position and dimensions of replace area.
    private final MyRectangle rectangle;
    /// Radio button with image of replaced area.
    private final RadioButton radioButton;
    /// Activity where this class is use.
    private final Activity activity;

    /**
     * Constructor.
     *
     * @param imageMat    original image for replace area.
     * @param activity    activity where this class is use.
     * @param radioButton radio button for image or replaced area.
     * @param rectangle   position and dimensions of replaced area.
     */
    public TextEraseMethod(Mat imageMat, Activity activity, RadioButton radioButton, MyRectangle rectangle) {
        this.radioButton = radioButton;
        this.rectangle = rectangle;
        this.activity = activity;

        image = Bitmap.createBitmap(imageMat.width(), imageMat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imageMat, image);

        radioButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getImgDetailOnErase());
    }

    /**
     * Getter for image with replaced area.
     *
     * @return image with replaced area.
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Getter fro radio button.
     *
     * @return radio button.
     */
    public RadioButton getRadioButton() {
        return radioButton;
    }

    /**
     * Detail on replaced place on image.
     *
     * @return crop replaced place on image.
     */
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

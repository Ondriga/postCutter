/*
 * Source code for the frontend of Bachelor thesis.
 * RectangleView class
 *
 * (C) Patrik Ondriga (xondri08)
 */

package postCutter.fe.postcutter.customViews.rectangle;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import postCutter.fe.postcutter.R;
import postCutter.fe.postcutter.dialogs.SettingDialog;

import java.util.List;

import postCutter.geometricShapes.Coordinate;
import postCutter.geometricShapes.line.MyLine;
import postCutter.geometricShapes.rectangle.MyRectangle;

import static android.content.Context.MODE_PRIVATE;

/**
 * Representing rectangle component for crop/replace frame component. It`s made of Component objects.
 */
public class RectangleView extends FrameLayout {
    /// Constant for minimum rectangle side.
    private static final int MIN_RECTANGLE_SIDE = 50;
    /// Constant form distance between rectangle side and edge to jump on edge position.
    private static final int MAX_BREAK_POINT_DISTANCE = 20;
    /// Constant for x and y inner rectangle shift again real real position of this component.
    public static int SHIFT;

    /// Max rectangle width is by default set for -1. Negative value representing no restriction for max width.
    private int maxRectangleWidth = -1;
    /// Max rectangle height is by default set for -1. Negative value representing no restriction for max height.
    private int maxRectangleHeight = -1;
    /// Representing position and dimensions of this rectangle component.
    private MyRectangle rectangle;

    /// Inner rectangle component by which is rectangle move.
    private InnerComponent innerRectangle;

    /// Component by which is rectangle resize by top left corner.
    private Component topLeftCorner;
    /// Component by which is rectangle resize by top right corner.
    private Component topRightCorner;
    /// Component by which is rectangle resize by bottom left corner.
    private Component bottomLeftCorner;
    /// Component by which is rectangle resize by bottom right corner.
    private Component bottomRightCorner;

    /// Component by which is rectangle resize by top side.
    private Component topSide;
    /// Component by which is rectangle resize by left side.
    private Component leftSide;
    /// Component by which is rectangle resize by bottom side.
    private Component bottomSide;
    /// Component by which is rectangle resize by right side.
    private Component rightSide;

    /// Width of the real image.
    private int realImageWidth;
    /// Height of the real image.
    private int realImageHeight;
    /// Position and dimensions of image showed in ImageView.
    private MyRectangle showedImage;

    /// List of horizontal edges in image.
    private List<MyLine> horizontalBreakpoints = null;
    /// List of vertical edges in image.
    private List<MyLine> verticalBreakpoints = null;
    /// Allow jumping on edges position.
    private boolean useBreakpoints = false;
    /// Activity in which is this component used.
    private Activity activity;

    /**
     * Constructor.
     *
     * @param context This value cannot be null.
     * @param attrs   This value may be null.
     */
    public RectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.rectangle_view, this);
        SHIFT = (int) (getResources().getDimension(R.dimen.rectangle_innerRectangleMargin)
                + getResources().getDimension(R.dimen.rectangle_border_width));
        Coordinate placeHolder1 = new Coordinate(0, 0);
        Coordinate placeHolder2 = new Coordinate(100, 100);
        this.rectangle = MyRectangle.createRectangle(placeHolder1, placeHolder2);
    }

    /**
     * Set rectangle in center of the image.
     */
    private void updateRectangle() {
        int centerX = this.showedImage.getWidth() / 2 + this.showedImage.getCornerA().getX();
        int centerY = this.showedImage.getHeight() / 2 + this.showedImage.getCornerA().getY();

        this.rectangle.getCornerA().setX(centerX - MIN_RECTANGLE_SIDE * 2);
        this.rectangle.getCornerA().setY(centerY - MIN_RECTANGLE_SIDE);
        this.rectangle.getCornerB().setX(centerX + MIN_RECTANGLE_SIDE * 2);
        this.rectangle.getCornerB().setY(centerY + MIN_RECTANGLE_SIDE);

        this.changeViewDimensions();
    }

    /**
     * Prepare this component before it will be used.
     *
     * @param realImageWidth  real image width.
     * @param realImageHeight real image height.
     * @param showedImage     position and dimensions of showed image.
     */
    public void prepare(int realImageWidth, int realImageHeight, MyRectangle showedImage) {
        prepareViews();

        this.realImageWidth = realImageWidth;
        this.realImageHeight = realImageHeight;
        this.showedImage = showedImage;

        updateRectangle();
    }

    /**
     * Prepare Views before this component will be used.
     */
    private void prepareViews() {
        innerRectangle = new InnerComponent(findViewById(R.id.rectangle_innerRectangle));

        topLeftCorner = new Component(findViewById(R.id.rectangle_topLeftCorner));
        topRightCorner = new Component(findViewById(R.id.rectangle_topRightCorner));
        bottomLeftCorner = new Component(findViewById(R.id.rectangle_bottomLeftCorner));
        bottomRightCorner = new Component(findViewById(R.id.rectangle_bottomRightCorner));

        topSide = new Component(findViewById(R.id.rectangle_topSide));
        leftSide = new Component(findViewById(R.id.rectangle_leftSide));
        bottomSide = new Component(findViewById(R.id.rectangle_bottomSide));
        rightSide = new Component(findViewById(R.id.rectangle_rightSide));
    }

    /**
     * Move with this rectangle component.
     *
     * @param newCoordinate new position for rectangle component.
     */
    public void changeInnerRectanglePosition(CoordinateFloat newCoordinate) {
        this.rectangle.move((int) newCoordinate.getX(), (int) newCoordinate.getY());
        changeViewDimensions();
    }

    /**
     * Resize rectangle component by it`s top left corner.
     *
     * @param newCoordinate new position for top left corner.
     */
    public void changeTopLeftPosition(CoordinateFloat newCoordinate) {
        changeTopPosition(newCoordinate.getY());
        changeLeftPosition(newCoordinate.getX());
    }

    /**
     * Resize rectangle component by it`s bottom left corner.
     *
     * @param newCoordinate new position for bottom left corner.
     */
    public void changeBottomLeftPosition(CoordinateFloat newCoordinate) {
        changeBottomPosition(newCoordinate.getY());
        changeLeftPosition(newCoordinate.getX());
    }

    /**
     * Resize rectangle component by it`s top right corner.
     *
     * @param newCoordinate new position for top right corner.
     */
    public void changeTopRightPosition(CoordinateFloat newCoordinate) {
        changeTopPosition(newCoordinate.getY());
        changeRightPosition(newCoordinate.getX());
    }

    /**
     * Resize rectangle component by it`s bottom right corner.
     *
     * @param newCoordinate new position for bottom right corner.
     */
    public void changeBottomRightPosition(CoordinateFloat newCoordinate) {
        changeBottomPosition(newCoordinate.getY());
        changeRightPosition(newCoordinate.getX());
    }

    /**
     * Change position for top side of rectangle component.
     *
     * @param y new Y value for top side.
     */
    public void changeTopPosition(float y) {
        int newY = (int) y;

        newY = (this.rectangle.getCornerB().getY() - SHIFT) - newY < MIN_RECTANGLE_SIDE ?
                this.rectangle.getCornerB().getY() - MIN_RECTANGLE_SIDE :
                newY + SHIFT;

        if (this.useBreakpoints) {
            newY = getYNearBreakpoint(newY);
        }

        this.rectangle.getCornerA().setY(newY);

        if (this.maxRectangleHeight > 0 && this.rectangle.getHeight() > this.maxRectangleHeight) {
            int difference = this.rectangle.getHeight() - this.maxRectangleHeight;
            newY = this.rectangle.getCornerA().getY() + difference;
            this.rectangle.getCornerA().setY(newY);
        }

        changeViewDimensions();
    }

    /**
     * Change position for left side of rectangle component.
     *
     * @param x new X value for left side.
     */
    public void changeLeftPosition(float x) {
        int newX = (int) x;

        newX = (this.rectangle.getCornerB().getX() - SHIFT) - newX < MIN_RECTANGLE_SIDE ?
                this.rectangle.getCornerB().getX() - MIN_RECTANGLE_SIDE :
                newX + SHIFT;

        if (this.useBreakpoints) {
            newX = getXNearBreakpoint(newX);
        }

        this.rectangle.getCornerA().setX(newX);

        if (this.maxRectangleWidth > 0 && this.rectangle.getWidth() > this.maxRectangleWidth) {
            int difference = this.rectangle.getWidth() - this.maxRectangleWidth;
            newX = this.rectangle.getCornerA().getX() + difference;
            this.rectangle.getCornerA().setX(newX);
        }

        changeViewDimensions();
    }

    /**
     * Change position for bottom side of rectangle component.
     *
     * @param y new Y value for bottom side.
     */
    public void changeBottomPosition(float y) {
        int newY = (int) y;

        newY = newY - (this.rectangle.getCornerA().getY() + SHIFT) < MIN_RECTANGLE_SIDE ?
                this.rectangle.getCornerA().getY() + MIN_RECTANGLE_SIDE :
                newY - SHIFT;

        if (this.useBreakpoints) {
            newY = getYNearBreakpoint(newY);
        }

        this.rectangle.getCornerB().setY(newY);

        if (this.maxRectangleHeight > 0 && this.rectangle.getHeight() > this.maxRectangleHeight) {
            int difference = this.rectangle.getHeight() - this.maxRectangleHeight;
            newY = this.rectangle.getCornerB().getY() - difference;
            this.rectangle.getCornerB().setY(newY);
        }

        changeViewDimensions();
    }

    /**
     * Change position for right side of rectangle component.
     *
     * @param x new X value for right side.
     */
    public void changeRightPosition(float x) {
        int newX = (int) x;

        newX = newX - (this.rectangle.getCornerA().getX() + SHIFT) < MIN_RECTANGLE_SIDE ?
                this.rectangle.getCornerA().getX() + MIN_RECTANGLE_SIDE :
                newX - SHIFT;

        if (this.useBreakpoints) {
            newX = getXNearBreakpoint(newX);
        }

        this.rectangle.getCornerB().setX(newX);

        if (this.maxRectangleWidth > 0 && this.rectangle.getWidth() > this.maxRectangleWidth) {
            int difference = this.rectangle.getWidth() - this.maxRectangleWidth;
            newX = this.rectangle.getCornerB().getX() - difference;
            this.rectangle.getCornerB().setX(newX);
        }

        changeViewDimensions();
    }

    /**
     * Check if the distance between rectangle side and some edge is smaller than break point constant.
     *
     * @param value           x or y value of rectangle side.
     * @param breakpointValue x or y value of edge.
     * @return true if the distance between rectangle side and edge is smaller than constant, otherwise false.
     */
    private boolean isInBreakpointInterval(int value, int breakpointValue) {
        return breakpointValue - MAX_BREAK_POINT_DISTANCE <= value &&
                value <= breakpointValue + MAX_BREAK_POINT_DISTANCE;
    }

    /**
     * Finding y value for jump.
     * Check if the jumping on edges is allowed. Then check if some edge is closer than break point
     * distance constant.
     *
     * @param y value of rectangle component side.
     * @return y value for next jump if jump is allowed and was found edge which is closer than
     * break point distance, otherwise given y value of rectangle component side.
     */
    private int getYNearBreakpoint(int y) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SettingDialog.SHARED_PREFS, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(SettingDialog.SUGGESTION_SWITCH, SettingDialog.SWITCH_DEFAULT)) {
            for (MyLine line : this.horizontalBreakpoints) {
                int lineYInShowedImg = mapping(
                        this.realImageHeight,
                        this.showedImage.getHeight(),
                        line.getStartPoint().getY()) + showedImage.getCornerA().getY();
                if (isInBreakpointInterval(y, lineYInShowedImg)) {
                    return lineYInShowedImg;
                }
            }
        }
        return y;
    }

    /**
     * Finding x value for jump.
     * Check if the jumping on edges is allowed. Then check if some edge is closer than break point
     * distance constant.
     *
     * @param x value of rectangle component side.
     * @return x value for next jump if jump is allowed and was found edge which is closer than
     * break point distance, otherwise given x value of rectangle component side.
     */
    private int getXNearBreakpoint(int x) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SettingDialog.SHARED_PREFS, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(SettingDialog.SUGGESTION_SWITCH, SettingDialog.SWITCH_DEFAULT)) {
            for (MyLine line : this.verticalBreakpoints) {
                int lineXInShowedImg = mapping(
                        this.realImageWidth,
                        this.showedImage.getWidth(),
                        line.getStartPoint().getX()) + showedImage.getCornerA().getX();
                if (isInBreakpointInterval(x, lineXInShowedImg)) {
                    return lineXInShowedImg;
                }
            }
        }

        return x;
    }

    /**
     * Change rectangle component View position and dimensions by rectangle object.
     */
    private void changeViewDimensions() {
        this.setX(this.rectangle.getCornerA().getX() - SHIFT);
        this.setY(this.rectangle.getCornerA().getY() - SHIFT);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
        params.height = this.rectangle.getHeight() + 2 * SHIFT;
        params.width = this.rectangle.getWidth() + 2 * SHIFT;
        this.setLayoutParams(params);
        checkRectangle4SideBars();
    }

    /**
     * Hide components in middle of the rectangle sides if the width of height
     * is less than allowed value, otherwise showe.
     */
    private void checkRectangle4SideBars() {
        int minSide = 2 * (int) (getResources().getDimension(R.dimen.rectangle_longSide));
        showHorizontalSideBars(rectangle.getWidth() > minSide);
        showVerticalSideBars(rectangle.getHeight() > minSide);
    }

    /**
     * Hide or show components in the middle of the left and right sides of the rectangle.
     *
     * @param visible true for show and false for hide.
     */
    private void showVerticalSideBars(boolean visible) {
        int visibility = View.VISIBLE;
        if (!visible) {
            visibility = View.GONE;
        }
        this.leftSide.getView().setVisibility(visibility);
        this.rightSide.getView().setVisibility(visibility);
    }

    /**
     * Hide or show components in the middle of the top and bottom sides of the rectangle.
     *
     * @param visible true for show and false for hide.
     */
    private void showHorizontalSideBars(boolean visible) {
        int visibility = View.VISIBLE;
        if (!visible) {
            visibility = View.GONE;
        }
        this.topSide.getView().setVisibility(visibility);
        this.bottomSide.getView().setVisibility(visibility);
    }

    /**
     * Transforming value from one scale rectangle to another. Calculate corresponding value from one scale to another.
     *
     * @param from  scale from where is value.
     * @param to    scale to where I want transforming value.
     * @param value for transforming.
     * @return transformed value.
     */
    private int mapping(int from, int to, int value) {
        double ration = (double) from / to;
        return (int) Math.round(value / ration);
    }

    /**
     * Getter for rectangle representing rectangle component.
     *
     * @return rectangle.
     */
    public MyRectangle getRectangle() {
        return rectangle;
    }

    /**
     * Getter for inner rectangle.
     *
     * @return inner rectangle.
     */
    public Component getInnerRectangle() {
        return innerRectangle;
    }

    /**
     * Getter for top left corner component.
     *
     * @return top left corner component.
     */
    public Component getTopLeftCorner() {
        return topLeftCorner;
    }

    /**
     * Getter for top right corner component.
     *
     * @return top right corner component.
     */
    public Component getTopRightCorner() {
        return topRightCorner;
    }

    /**
     * Getter for bottom left corner component.
     *
     * @return bottom left corner component.
     */
    public Component getBottomLeftCorner() {
        return bottomLeftCorner;
    }

    /**
     * Getter for bottom right corner component.
     *
     * @return bottom right corner component.
     */
    public Component getBottomRightCorner() {
        return bottomRightCorner;
    }

    /**
     * Getter for component int the middle of the top side.
     *
     * @return component int the middle of the top side.
     */
    public Component getTopSide() {
        return topSide;
    }

    /**
     * Getter for component int the middle of the left side.
     *
     * @return component int the middle of the left side.
     */
    public Component getLeftSide() {
        return leftSide;
    }

    /**
     * Getter for component int the middle of the bottom side.
     *
     * @return component int the middle of the bottom side.
     */
    public Component getBottomSide() {
        return bottomSide;
    }

    /**
     * Getter for component int the middle of the right side.
     *
     * @return component int the middle of the right side.
     */
    public Component getRightSide() {
        return rightSide;
    }

    /**
     * Setter for restriction of the max width and height of rectangle component.
     *
     * @param maxWidth  max value of width.
     * @param maxHeight max value of height.
     */
    public void setMaxRectangle(int maxWidth, int maxHeight) {
        this.maxRectangleWidth = maxWidth;
        this.maxRectangleHeight = maxHeight;
    }

    /**
     * Return rectangle position a dimensions of rectangle component considering to real image.
     *
     * @return transforming rectangle of rectangle component.
     */
    public MyRectangle getRectangleInNormalSize() {
        int x = mapping(
                showedImage.getWidth(),
                realImageWidth,
                rectangle.getCornerA().getX() - showedImage.getCornerA().getX()
        );
        int y = mapping(
                showedImage.getHeight(),
                realImageHeight,
                rectangle.getCornerA().getY() - showedImage.getCornerA().getY()
        );
        Coordinate cornerA = new Coordinate(x, y);
        x = mapping(
                showedImage.getWidth(),
                realImageWidth,
                rectangle.getCornerB().getX() - showedImage.getCornerA().getX()
        );
        y = mapping(
                showedImage.getHeight(),
                realImageHeight,
                rectangle.getCornerB().getY() - showedImage.getCornerA().getY()
        );
        Coordinate cornerB = new Coordinate(x, y);
        return MyRectangle.createRectangle(cornerA, cornerB);
    }

    /**
     * Set position and dimensions of rectangle component by rectangle in real image scale.
     *
     * @param rectangle in scale of real image.
     */
    public void setRectangleInNormalSize(MyRectangle rectangle) {
        int x = mapping(
                realImageWidth,
                showedImage.getWidth(),
                rectangle.getCornerA().getX()) + showedImage.getCornerA().getX();
        int y = mapping(
                realImageHeight,
                showedImage.getHeight(),
                rectangle.getCornerA().getY()) + showedImage.getCornerA().getY();
        Coordinate cornerA = new Coordinate(x, y);
        x = mapping(
                realImageWidth,
                showedImage.getWidth(),
                rectangle.getCornerB().getX()) + showedImage.getCornerA().getX();
        y = mapping(
                realImageHeight,
                showedImage.getHeight(),
                rectangle.getCornerB().getY()) + showedImage.getCornerA().getY();
        Coordinate cornerB = new Coordinate(x, y);
        this.rectangle = MyRectangle.createRectangle(cornerA, cornerB);
        changeViewDimensions();
    }

    /**
     * Allowed jumping on edges.
     *
     * @param horizontalBreakpoints list of horizontal edges.
     * @param verticalBreakpoints   list of vertical edges.
     * @param activity              Activity in which is this component used.
     */
    public void activateBreakpoints(
            List<MyLine> horizontalBreakpoints,
            List<MyLine> verticalBreakpoints,
            Activity activity) {
        this.horizontalBreakpoints = horizontalBreakpoints;
        this.verticalBreakpoints = verticalBreakpoints;
        this.useBreakpoints = true;
        this.activity = activity;
    }
}

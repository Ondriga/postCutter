package com.example.postcutter.customViews.rectangle;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.postcutter.R;
import com.example.postcutter.SettingDialog;

import java.util.List;

import postCutter.geometricShapes.Coordinate;
import postCutter.geometricShapes.line.MyLine;
import postCutter.geometricShapes.rectangle.MyRectangle;

import static android.content.Context.MODE_PRIVATE;

public class RectangleView extends FrameLayout {

    private static final int MIN_RECTANGLE_SIDE = 80;
    private static final int MAX_BREAK_POINT_DISTANCE = 20;

    private MyRectangle rectangle;

    private InnerComponent innerRectangle;

    private Component topLeftCorner;
    private Component topRightCorner;
    private Component bottomLeftCorner;
    private Component bottomRightCorner;

    private Component topSide;
    private Component leftSide;
    private Component bottomSide;
    private Component rightSide;

    public static int SHIFT;

    private int realImageWidth;
    private int realImageHeight;
    private MyRectangle showedImage;

    private List<MyLine> horizontalBreakpoints = null;
    private List<MyLine> verticalBreakpoints = null;
    private boolean useBreakpoints = false;

    private Activity activity;

    public RectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.rectangle_view, this);
        SHIFT = (int) (getResources().getDimension(R.dimen.rectangle_innerRectangleMargin)
                + getResources().getDimension(R.dimen.rectangle_border_width));
        Coordinate placeHolder1 = new Coordinate(0, 0);
        Coordinate placeHolder2 = new Coordinate(100, 100);
        this.rectangle = MyRectangle.createRectangle(placeHolder1, placeHolder2);
    }

    private void updateRectangle() {
        int centerX = this.showedImage.getWidth() / 2 + this.showedImage.getCornerA().getX();
        int centerY = this.showedImage.getHeight() / 2 + this.showedImage.getCornerA().getY();

        this.rectangle.getCornerA().setX(centerX - MIN_RECTANGLE_SIDE * 2);
        this.rectangle.getCornerA().setY(centerY - MIN_RECTANGLE_SIDE);
        this.rectangle.getCornerB().setX(centerX + MIN_RECTANGLE_SIDE * 2);
        this.rectangle.getCornerB().setY(centerY + MIN_RECTANGLE_SIDE);

        this.changeViewDimensions();
    }

    public void prepare(int realImageWidth, int realImageHeight, MyRectangle showedImage) {
        prepareViews();

        this.realImageWidth = realImageWidth;
        this.realImageHeight = realImageHeight;
        this.showedImage = showedImage;

        updateRectangle();
    }

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

    public void changeInnerRectanglePosition(CoordinateFloat newCoordinate) {
        this.rectangle.move((int) newCoordinate.getX(), (int) newCoordinate.getY());
        changeViewDimensions();
    }

    public void changeTopLeftPosition(CoordinateFloat newCoordinate) {
        changeTopPosition(newCoordinate.getY());
        changeLeftPosition(newCoordinate.getX());
    }

    public void changeBottomLeftPosition(CoordinateFloat newCoordinate) {
        changeBottomPosition(newCoordinate.getY());
        changeLeftPosition(newCoordinate.getX());
    }

    public void changeTopRightPosition(CoordinateFloat newCoordinate) {
        changeTopPosition(newCoordinate.getY());
        changeRightPosition(newCoordinate.getX());
    }

    public void changeBottomRightPosition(CoordinateFloat newCoordinate) {
        changeBottomPosition(newCoordinate.getY());
        changeRightPosition(newCoordinate.getX());
    }

    public void changeTopPosition(float y) {
        int newY = (int) y;

        newY = (this.rectangle.getCornerB().getY() - SHIFT) - newY < MIN_RECTANGLE_SIDE ?
                this.rectangle.getCornerB().getY() - MIN_RECTANGLE_SIDE :
                newY + SHIFT;

        if (this.useBreakpoints) {
            newY = getYNearBreakpoint(newY);
        }

        this.rectangle.getCornerA().setY(newY);
        changeViewDimensions();
    }

    public void changeLeftPosition(float x) {
        int newX = (int) x;

        newX = (this.rectangle.getCornerB().getX() - SHIFT) - newX < MIN_RECTANGLE_SIDE ?
                this.rectangle.getCornerB().getX() - MIN_RECTANGLE_SIDE :
                newX + SHIFT;

        if (this.useBreakpoints) {
            newX = getXNearBreakpoint(newX);
        }

        this.rectangle.getCornerA().setX(newX);
        changeViewDimensions();
    }

    public void changeBottomPosition(float y) {
        int newY = (int) y;

        newY = newY - (this.rectangle.getCornerA().getY() + SHIFT) < MIN_RECTANGLE_SIDE ?
                this.rectangle.getCornerA().getY() + MIN_RECTANGLE_SIDE :
                newY - SHIFT;

        if (this.useBreakpoints) {
            newY = getYNearBreakpoint(newY);
        }

        this.rectangle.getCornerB().setY(newY);
        changeViewDimensions();
    }

    public void changeRightPosition(float x) {
        int newX = (int) x;

        newX = newX - (this.rectangle.getCornerA().getX() + SHIFT) < MIN_RECTANGLE_SIDE ?
                this.rectangle.getCornerA().getX() + MIN_RECTANGLE_SIDE :
                newX - SHIFT;

        if (this.useBreakpoints) {
            newX = getXNearBreakpoint(newX);
        }

        this.rectangle.getCornerB().setX(newX);
        changeViewDimensions();
    }

    private boolean isInBreakpointInterval(int value, int breakpointValue) {
        return breakpointValue - MAX_BREAK_POINT_DISTANCE <= value &&
                value <= breakpointValue + MAX_BREAK_POINT_DISTANCE;
    }

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

    private void changeViewDimensions() {
        this.setX(this.rectangle.getCornerA().getX() - SHIFT);
        this.setY(this.rectangle.getCornerA().getY() - SHIFT);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
        params.height = this.rectangle.getHeight() + 2 * SHIFT;
        params.width = this.rectangle.getWidth() + 2 * SHIFT;
        this.setLayoutParams(params);
        checkRectangle4SideBars();
    }

    private void checkRectangle4SideBars() {
        int minSide = 2 * (int) (getResources().getDimension(R.dimen.rectangle_longSide));
        showHorizontalSideBars(rectangle.getWidth() > minSide);
        showVerticalSideBars(rectangle.getHeight() > minSide);
    }

    private void showVerticalSideBars(boolean visible) {
        int visibility = View.VISIBLE;
        if (!visible) {
            visibility = View.GONE;
        }
        this.leftSide.getView().setVisibility(visibility);
        this.rightSide.getView().setVisibility(visibility);
    }

    private void showHorizontalSideBars(boolean visible) {
        int visibility = View.VISIBLE;
        if (!visible) {
            visibility = View.GONE;
        }
        this.topSide.getView().setVisibility(visibility);
        this.bottomSide.getView().setVisibility(visibility);
    }

    private int mapping(int from, int to, int value) {
        double ration = (double) from / to;
        return (int) Math.round(value / ration);
    }

    public MyRectangle getRectangle() {
        return rectangle;
    }

    public Component getInnerRectangle() {
        return innerRectangle;
    }

    public Component getTopLeftCorner() {
        return topLeftCorner;
    }

    public Component getTopRightCorner() {
        return topRightCorner;
    }

    public Component getBottomLeftCorner() {
        return bottomLeftCorner;
    }

    public Component getBottomRightCorner() {
        return bottomRightCorner;
    }

    public Component getTopSide() {
        return topSide;
    }

    public Component getLeftSide() {
        return leftSide;
    }

    public Component getBottomSide() {
        return bottomSide;
    }

    public Component getRightSide() {
        return rightSide;
    }

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

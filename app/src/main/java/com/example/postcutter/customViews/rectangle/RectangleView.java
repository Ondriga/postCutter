package com.example.postcutter.customViews.rectangle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.example.postcutter.R;

import postCutter.geometricShapes.Coordinate;
import postCutter.geometricShapes.rectangle.MyRectangle;

public class RectangleView extends FrameLayout {

    private MyRectangle rectangle = null;

    private Component innerRectangle;
    private Component topLeftCorner;
    private Component topRightCorner;
    private Component bottomLeftCorner;
    private Component bottomRightCorner;

    public static int SHIFT;

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
        this.rectangle.getCornerA().setX((int) this.getX() + SHIFT);
        this.rectangle.getCornerA().setY((int) this.getY() + SHIFT);
        this.rectangle.getCornerB().setX(this.getWidth() - SHIFT);
        this.rectangle.getCornerB().setY(this.getHeight() - SHIFT);
    }

    public void setRectangle() {
        updateRectangle();
        prepairViews();
    }

    private void prepairViews() {
        innerRectangle = new Component(findViewById(R.id.rectangle_innerRectangle));
        topLeftCorner = new Component(findViewById(R.id.rectangle_topLeftCorner));
        topRightCorner = new Component(findViewById(R.id.rectangle_topRightCorner));
        bottomLeftCorner = new Component(findViewById(R.id.rectangle_bottomLeftCorner));
        bottomRightCorner = new Component(findViewById(R.id.rectangle_bottomRightCorner));
    }

    public void changeInnerRectanglePosition(CoordinateFloat newCoordinate) {
        this.setX(newCoordinate.getX());
        this.setY(newCoordinate.getY());
        updateRectangle();
        int diffX = (int) (this.rectangle.getCornerA().getX() - newCoordinate.getX());
        int diffY = (int) (this.rectangle.getCornerA().getY() - newCoordinate.getY());
        this.rectangle.getCornerA().setX((int) newCoordinate.getX() + SHIFT);
        this.rectangle.getCornerA().setY((int) newCoordinate.getY() + SHIFT);
        this.rectangle.getCornerB().setX(this.rectangle.getCornerB().getX() + diffX);
        this.rectangle.getCornerB().setY(this.rectangle.getCornerB().getY() + diffY);
        changeViewDimensions();
    }

    public void changeTopLeftPosition(CoordinateFloat newCoordinate) {
        this.rectangle.getCornerA().setX((int) newCoordinate.getX() + SHIFT);
        this.rectangle.getCornerA().setY((int) newCoordinate.getY() + SHIFT);
        changeViewDimensions();
    }

    public void changeBottomLeftPosition(CoordinateFloat newCoordinate) {
        this.rectangle.getCornerA().setX((int) newCoordinate.getX() + SHIFT);
        this.rectangle.getCornerB().setY((int) newCoordinate.getY() - SHIFT);
        changeViewDimensions();
    }

    public void changeTopRightPosition(CoordinateFloat newCoordinate) {
        this.rectangle.getCornerB().setX((int) newCoordinate.getX() - SHIFT);
        this.rectangle.getCornerA().setY((int) newCoordinate.getY() + SHIFT);
        changeViewDimensions();
    }

    public void changeBottomRightPosition(CoordinateFloat newCoordinate) {
        this.rectangle.getCornerB().setX((int) newCoordinate.getX() - SHIFT);
        this.rectangle.getCornerB().setY((int) newCoordinate.getY() - SHIFT);
        changeViewDimensions();
    }

    private void changeViewDimensions() {
        System.out.println(this.rectangle);//TODO debug
        this.setX(this.rectangle.getCornerA().getX() - SHIFT);
        this.setY(this.rectangle.getCornerA().getY() - SHIFT);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
        params.height = this.rectangle.getHeight() + 2 * SHIFT;
        params.width = this.rectangle.getWidth() + 2 * SHIFT;
        this.setLayoutParams(params);
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
}

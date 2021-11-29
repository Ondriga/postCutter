package com.example.postcutter.customViews.rectangle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.postcutter.R;

import postCutter.geometricShapes.Coordinate;
import postCutter.geometricShapes.rectangle.MyRectangle;

public class RectangleView extends FrameLayout {

    private static final int MIN_RECTANGLE_SIDE = 80;

    private MyRectangle rectangle;

    private InnerComponent innerRectangle;
    private Component topLeftCorner;
    private Component topRightCorner;
    private Component bottomLeftCorner;
    private Component bottomRightCorner;

    public static int SHIFT;

    private int realImageWidth;
    private int realImageHeight;
    private int showedImageWidth;
    private int showedImageHeight;

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

    public void prepare(int realImageWidth, int realImageHeight, int showedImageWidth, int showedImageHeight) {
        this.realImageWidth = realImageWidth;
        this.realImageHeight = realImageHeight;
        this.showedImageWidth = showedImageWidth;
        this.showedImageHeight = showedImageHeight;
        updateRectangle();
        prepairViews();
    }

    private void prepairViews() {
        innerRectangle = new InnerComponent(findViewById(R.id.rectangle_innerRectangle));
        topLeftCorner = new Component(findViewById(R.id.rectangle_topLeftCorner));
        topRightCorner = new Component(findViewById(R.id.rectangle_topRightCorner));
        bottomLeftCorner = new Component(findViewById(R.id.rectangle_bottomLeftCorner));
        bottomRightCorner = new Component(findViewById(R.id.rectangle_bottomRightCorner));
    }

    public void changeInnerRectanglePosition(CoordinateFloat newCoordinate) {
        this.rectangle.move((int) newCoordinate.getX(), (int) newCoordinate.getY());
        changeViewDimensions();
    }

    public void changeTopLeftPosition(CoordinateFloat newCoordinate) {
        int newX = (this.rectangle.getCornerB().getX() - SHIFT) - newCoordinate.getX() < MIN_RECTANGLE_SIDE ?
                (int) (this.rectangle.getCornerB().getX() - MIN_RECTANGLE_SIDE) :
                (int) newCoordinate.getX() + SHIFT;

        int newY = (this.rectangle.getCornerB().getY() - SHIFT) - newCoordinate.getY() < MIN_RECTANGLE_SIDE ?
                (int) (this.rectangle.getCornerB().getY() - MIN_RECTANGLE_SIDE) :
                (int) newCoordinate.getY() + SHIFT;

        this.rectangle.getCornerA().setX(newX);
        this.rectangle.getCornerA().setY(newY);
        changeViewDimensions();
    }

    public void changeBottomLeftPosition(CoordinateFloat newCoordinate) {
        int newX = (this.rectangle.getCornerB().getX() - SHIFT) - newCoordinate.getX() < MIN_RECTANGLE_SIDE ?
                (int) (this.rectangle.getCornerB().getX() - MIN_RECTANGLE_SIDE) :
                (int) newCoordinate.getX() + SHIFT;

        int newY = newCoordinate.getY() - (this.rectangle.getCornerA().getY() + SHIFT) < MIN_RECTANGLE_SIDE ?
                (int) (this.rectangle.getCornerA().getY() + MIN_RECTANGLE_SIDE) :
                (int) newCoordinate.getY() - SHIFT;

        this.rectangle.getCornerA().setX(newX);
        this.rectangle.getCornerB().setY(newY);
        changeViewDimensions();
    }

    public void changeTopRightPosition(CoordinateFloat newCoordinate) {
        int newX = newCoordinate.getX() - (this.rectangle.getCornerA().getX() + SHIFT) < MIN_RECTANGLE_SIDE ?
                (int) (this.rectangle.getCornerA().getX() + MIN_RECTANGLE_SIDE) :
                (int) newCoordinate.getX() - SHIFT;

        int newY = (this.rectangle.getCornerB().getY() - SHIFT) - newCoordinate.getY() < MIN_RECTANGLE_SIDE ?
                (int) (this.rectangle.getCornerB().getY() - MIN_RECTANGLE_SIDE) :
                (int) newCoordinate.getY() + SHIFT;

        this.rectangle.getCornerB().setX(newX);
        this.rectangle.getCornerA().setY(newY);
        changeViewDimensions();
    }

    public void changeBottomRightPosition(CoordinateFloat newCoordinate) {
        int newX = newCoordinate.getX() - (this.rectangle.getCornerA().getX() + SHIFT) < MIN_RECTANGLE_SIDE ?
                (int) (this.rectangle.getCornerA().getX() + MIN_RECTANGLE_SIDE) :
                (int) newCoordinate.getX() - SHIFT;

        int newY = newCoordinate.getY() - (this.rectangle.getCornerA().getY() + SHIFT) < MIN_RECTANGLE_SIDE ?
                (int) (this.rectangle.getCornerA().getY() + MIN_RECTANGLE_SIDE) :
                (int) newCoordinate.getY() - SHIFT;

        this.rectangle.getCornerB().setX(newX);
        this.rectangle.getCornerB().setY(newY);
        changeViewDimensions();
    }

    private void changeViewDimensions() {
        this.setX(this.rectangle.getCornerA().getX() - SHIFT);
        this.setY(this.rectangle.getCornerA().getY() - SHIFT);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
        params.height = this.rectangle.getHeight() + 2 * SHIFT;
        params.width = this.rectangle.getWidth() + 2 * SHIFT;
        this.setLayoutParams(params);
    }

    private int mapping(int from, int to, int value){
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

    public MyRectangle getRectangleInNormalSize() {
        int x = mapping(showedImageWidth, realImageWidth, rectangle.getCornerA().getX());
        int y = mapping(showedImageHeight, realImageHeight, rectangle.getCornerA().getY());
        Coordinate cornerA = new Coordinate(x, y);
        x = mapping(showedImageWidth, realImageWidth, rectangle.getCornerB().getX());
        y = mapping(showedImageHeight, realImageHeight, rectangle.getCornerB().getY());
        Coordinate cornerB = new Coordinate(x, y);
        return MyRectangle.createRectangle(cornerA, cornerB);
    }

    public void setRectangleInNormalSize(MyRectangle rectangle) {
        int x = mapping(realImageWidth, showedImageWidth, rectangle.getCornerA().getX());
        int y = mapping(realImageHeight, showedImageHeight, rectangle.getCornerA().getY());
        Coordinate cornerA = new Coordinate(x, y);
        x = mapping(realImageWidth, showedImageWidth, rectangle.getCornerB().getX());
        y = mapping(realImageHeight, showedImageHeight, rectangle.getCornerB().getY());
        Coordinate cornerB = new Coordinate(x, y);
        this.rectangle = MyRectangle.createRectangle(cornerA, cornerB);
        changeViewDimensions();
    }
}

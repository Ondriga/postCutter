package com.example.postcutter.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.example.postcutter.R;

import postCutter.geometricShapes.Coordinate;
import postCutter.geometricShapes.rectangle.MyRectangle;

public class RectangleView extends FrameLayout {

    private MyRectangle rectangle = null;

    private final View topLeftCorner = findViewById(R.id.rectangle_topLeftCorner);
    private final View topRightCorner = findViewById(R.id.rectangle_topRightCorner);
    private final View bottomLeftCorner = findViewById(R.id.rectangle_bottomLeftCorner);
    private final View bottomRightCorner = findViewById(R.id.rectangle_bottomRightCorner);

    public RectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.rectangle_view, this);
    }

    public void setRectangle(Coordinate cornerA, Coordinate cornerB) {
        int shift = (int) getResources().getDimension(R.dimen.rectangle_innerRectangleMargin);
        shift += (int) getResources().getDimension(R.dimen.rectangle_border_width);
        cornerA.setX(cornerA.getX() + shift);
        cornerA.setY(cornerA.getY() + shift);
        cornerB.setX(cornerB.getX() - shift);
        cornerB.setY(cornerB.getY() - shift);
        this.rectangle = MyRectangle.createRectangle(cornerA, cornerB);
    }

    public MyRectangle getRectangle() {
        return rectangle;
    }

    public View getTopLeftCorner() {
        return topLeftCorner;
    }

    public View getTopRightCorner() {
        return topRightCorner;
    }

    public View getBottomLeftCorner() {
        return bottomLeftCorner;
    }

    public View getBottomRightCorner() {
        return bottomRightCorner;
    }
}

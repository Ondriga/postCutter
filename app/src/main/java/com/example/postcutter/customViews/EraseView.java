package com.example.postcutter.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.postcutter.R;
import com.example.postcutter.customViews.rectangle.CoordinateFloat;
import com.example.postcutter.customViews.rectangle.RectangleView;

import postCutter.geometricShapes.rectangle.MyRectangle;

public class EraseView extends FrameLayout {
    private final RectangleView rectangleView;
    private final RelativeLayout mainLayout;
    private final ImageView imageView;

    public EraseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.erase_view, this);

        rectangleView = findViewById(R.id.erase_view_rectangleView);
        mainLayout = findViewById(R.id.erase_view_mainLayout);
        imageView = findViewById(R.id.erase_view_image);

        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (imageView.getDrawable() != null) {
                    imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    rectangleView.prepare(
                            imageView.getDrawable().getIntrinsicWidth(),
                            imageView.getDrawable().getIntrinsicHeight(),
                            imageView.getWidth(),
                            imageView.getHeight());
                    prepareComponentsForMove();
                }
            }
        });
    }

    private void makeRectangleInLayout(CoordinateFloat newCoordinate) {
        if (RectangleView.SHIFT > rectangleView.getRectangle().getCornerA().getX() + newCoordinate.getX()) {
            newCoordinate.setX(RectangleView.SHIFT - rectangleView.getRectangle().getCornerA().getX());
        }
        if (RectangleView.SHIFT > rectangleView.getRectangle().getCornerA().getY() + newCoordinate.getY()) {
            newCoordinate.setY(RectangleView.SHIFT - rectangleView.getRectangle().getCornerA().getY());
        }
        if (mainLayout.getWidth() - RectangleView.SHIFT < rectangleView.getRectangle().getCornerB().getX() + newCoordinate.getX()) {
            newCoordinate.setX(mainLayout.getWidth() - RectangleView.SHIFT - rectangleView.getRectangle().getCornerB().getX());
        }
        if (mainLayout.getHeight() - RectangleView.SHIFT < rectangleView.getRectangle().getCornerB().getY() + newCoordinate.getY()) {
            newCoordinate.setY(mainLayout.getHeight() - RectangleView.SHIFT - rectangleView.getRectangle().getCornerB().getY());
        }
    }

    private void prepareComponentsForMove() {
        rectangleView.getInnerRectangle().getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CoordinateFloat oldCoordinate = new CoordinateFloat(
                        rectangleView.getRectangle().getCornerA().getX() - RectangleView.SHIFT,
                        rectangleView.getRectangle().getCornerA().getY() - RectangleView.SHIFT);
                CoordinateFloat newCoordinate = rectangleView.getInnerRectangle().moveAction(event, oldCoordinate);
                if (newCoordinate != null) {
                    makeRectangleInLayout(newCoordinate);
                    rectangleView.changeInnerRectanglePosition(newCoordinate);
                }
                return true;
            }
        });
        rectangleView.getTopLeftCorner().getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CoordinateFloat oldCoordinate = new CoordinateFloat(
                        rectangleView.getRectangle().getCornerA().getX() - RectangleView.SHIFT,
                        rectangleView.getRectangle().getCornerA().getY() - RectangleView.SHIFT);
                CoordinateFloat newCoordinate = rectangleView.getTopLeftCorner().moveAction(event, oldCoordinate);
                if (newCoordinate != null) {
                    newCoordinate.setX(Math.max(newCoordinate.getX(), 0));
                    newCoordinate.setY(Math.max(newCoordinate.getY(), 0));
                    rectangleView.changeTopLeftPosition(newCoordinate);
                }
                return true;
            }
        });
        rectangleView.getBottomLeftCorner().getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CoordinateFloat oldCoordinate = new CoordinateFloat(
                        rectangleView.getRectangle().getCornerA().getX() - RectangleView.SHIFT,
                        rectangleView.getRectangle().getCornerB().getY() + RectangleView.SHIFT);
                CoordinateFloat newCoordinate = rectangleView.getBottomLeftCorner().moveAction(event, oldCoordinate);
                if (newCoordinate != null) {
                    newCoordinate.setX(Math.max(newCoordinate.getX(), 0));
                    newCoordinate.setY(Math.min(newCoordinate.getY(), mainLayout.getHeight()));
                    rectangleView.changeBottomLeftPosition(newCoordinate);
                }
                return true;
            }
        });
        rectangleView.getTopRightCorner().getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CoordinateFloat oldCoordinate = new CoordinateFloat(
                        rectangleView.getRectangle().getCornerB().getX() + RectangleView.SHIFT,
                        rectangleView.getRectangle().getCornerA().getY() - RectangleView.SHIFT);
                CoordinateFloat newCoordinate = rectangleView.getTopRightCorner().moveAction(event, oldCoordinate);
                if (newCoordinate != null) {
                    newCoordinate.setX(Math.min(newCoordinate.getX(), mainLayout.getWidth()));
                    newCoordinate.setY(Math.max(newCoordinate.getY(), 0));
                    rectangleView.changeTopRightPosition(newCoordinate);
                }
                return true;
            }
        });
        rectangleView.getBottomRightCorner().getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CoordinateFloat oldCoordinate = new CoordinateFloat(
                        rectangleView.getRectangle().getCornerB().getX() + RectangleView.SHIFT,
                        rectangleView.getRectangle().getCornerB().getY() + RectangleView.SHIFT);
                CoordinateFloat newCoordinate = rectangleView.getBottomLeftCorner().moveAction(event, oldCoordinate);
                if (newCoordinate != null) {
                    newCoordinate.setX(Math.min(newCoordinate.getX(), mainLayout.getWidth()));
                    newCoordinate.setY(Math.min(newCoordinate.getY(), mainLayout.getHeight()));
                    rectangleView.changeBottomRightPosition(newCoordinate);
                }
                return true;
            }
        });
    }

    public ImageView getImageView() {
        return imageView;
    }

    public MyRectangle getRectangle() {
        return rectangleView.getRectangleInNormalSize();
    }
}

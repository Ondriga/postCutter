package com.example.postcutter.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.postcutter.R;
import com.example.postcutter.customViews.rectangle.CoordinateFloat;
import com.example.postcutter.customViews.rectangle.RectangleView;

import postCutter.geometricShapes.Coordinate;

public class EraseView extends FrameLayout {
    private final RectangleView rectangleView;

    public EraseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.erase_view, this);

        rectangleView = findViewById(R.id.erase_view_rectangleView);

        RelativeLayout mainLayout = findViewById(R.id.erase_view_mainLayout);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        rectangleView.setRectangle();
                        prepareComponentsForMove();
                    }
                }
        );
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
                    rectangleView.changeBottomRightPosition(newCoordinate);
                }
                return true;
            }
        });
    }

}

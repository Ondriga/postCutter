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
import com.squareup.picasso.Picasso;

import postCutter.geometricShapes.rectangle.MyRectangle;

public class EraseView extends FrameLayout {
    private final RectangleView rectangleView;
    private final RelativeLayout mainLayout;
    private final ImageView imageView;

    private final View shadowTop;
    private final View shadowLeft;
    private final View shadowBottom;
    private final View shadowRight;

    public EraseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.erase_view, this);

        rectangleView = findViewById(R.id.erase_view_rectangleView);
        mainLayout = findViewById(R.id.erase_view_mainLayout);
        imageView = findViewById(R.id.erase_view_image);

        shadowTop = findViewById(R.id.erase_top_shadow);
        shadowLeft = findViewById(R.id.erase_left_shadow);
        shadowBottom = findViewById(R.id.erase_bottom_shadow);
        shadowRight = findViewById(R.id.erase_right_shadow);

        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (imageView.getDrawable() != null) {
                    imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    setShadows();
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

    public void loadPicture(String imagePath) {
        Picasso.get().load("file:" + imagePath).into(this.imageView);
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
        rectangleView.addOnLayoutChangeListener( new View.OnLayoutChangeListener()
        {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                setShadows();
            }
        });
    }

    private void setViewHeight(View view, int height) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }

    private void setViewWidth(View view, int width) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
    }

    private void setShadows() {
        MyRectangle rectangle = rectangleView.getRectangle();

        setViewHeight(shadowTop, rectangle.getCornerA().getY());

        shadowLeft.setY(rectangle.getCornerA().getY());
        setViewWidth(shadowLeft, rectangle.getCornerA().getX());
        setViewHeight(shadowLeft, rectangle.getHeight());

        shadowBottom.setY(rectangle.getCornerB().getY() + 1);
        setViewHeight(shadowBottom, this.getHeight() - rectangle.getCornerB().getY() - 1);

        shadowRight.setX(rectangle.getCornerB().getX());
        shadowRight.setY(rectangle.getCornerA().getY());
        setViewWidth(shadowRight, this.getWidth() - rectangle.getCornerB().getX());
        setViewHeight(shadowRight, rectangle.getHeight());
    }

    public MyRectangle getRectangle() {
        return this.rectangleView.getRectangleInNormalSize();
    }

    public void setRectangle(MyRectangle rectangle) {
        this.rectangleView.setRectangleInNormalSize(rectangle);
    }
}

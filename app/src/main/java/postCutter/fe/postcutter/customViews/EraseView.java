package postCutter.fe.postcutter.customViews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import postCutter.fe.postcutter.R;
import postCutter.fe.postcutter.customViews.rectangle.CoordinateFloat;
import postCutter.fe.postcutter.customViews.rectangle.RectangleView;

import java.util.List;

import postCutter.geometricShapes.Coordinate;
import postCutter.geometricShapes.line.MyLine;
import postCutter.geometricShapes.rectangle.MyRectangle;

public class EraseView extends FrameLayout {
    private final RectangleView rectangleView;
    private final ImageView imageView;
    private MyRectangle imgOnScreen;

    private final View shadowTop;
    private final View shadowLeft;
    private final View shadowBottom;
    private final View shadowRight;

    public EraseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.erase_view, this);

        rectangleView = findViewById(R.id.erase_view_rectangleView);
        imageView = findViewById(R.id.erase_view_image);

        shadowTop = findViewById(R.id.erase_top_shadow);
        shadowLeft = findViewById(R.id.erase_left_shadow);
        shadowBottom = findViewById(R.id.erase_bottom_shadow);
        shadowRight = findViewById(R.id.erase_right_shadow);
    }

    public void loadPicture(Bitmap imageBitmap) {
        this.imageView.setImageBitmap(imageBitmap);

        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (imageView.getDrawable() != null) {
                    Coordinate cornerA = new Coordinate(
                            imageView.getLeft(),
                            imageView.getTop()
                    );
                    Coordinate cornerB = new Coordinate(
                            imageView.getRight(),
                            imageView.getBottom()
                    );
                    imgOnScreen = MyRectangle.createRectangle(cornerA, cornerB);

                    setShadows();
                    rectangleView.prepare(
                            imageView.getDrawable().getIntrinsicWidth(),
                            imageView.getDrawable().getIntrinsicHeight(),
                            imgOnScreen
                    );
                    prepareComponentsForMove();

                    // remove listener for image change
                    imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    private void makeRectangleInLayout(CoordinateFloat newCoordinate) {
        if (imgOnScreen.getCornerA().getX() > rectangleView.getRectangle().getCornerA().getX() + newCoordinate.getX()) {
            newCoordinate.setX(imgOnScreen.getCornerA().getX() - rectangleView.getRectangle().getCornerA().getX());
        }
        if (imgOnScreen.getCornerA().getY() > rectangleView.getRectangle().getCornerA().getY() + newCoordinate.getY()) {
            newCoordinate.setY(imgOnScreen.getCornerA().getY() - rectangleView.getRectangle().getCornerA().getY());
        }
        if (imgOnScreen.getCornerB().getX() < rectangleView.getRectangle().getCornerB().getX() + newCoordinate.getX()) {
            newCoordinate.setX(imgOnScreen.getCornerB().getX() - rectangleView.getRectangle().getCornerB().getX());
        }
        if (imgOnScreen.getCornerB().getY() < rectangleView.getRectangle().getCornerB().getY() + newCoordinate.getY()) {
            newCoordinate.setY(imgOnScreen.getCornerB().getY() - rectangleView.getRectangle().getCornerB().getY());
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
                    newCoordinate.setX(Math.max(newCoordinate.getX(), imgOnScreen.getCornerA().getX() - RectangleView.SHIFT));
                    newCoordinate.setY(Math.max(newCoordinate.getY(), imgOnScreen.getCornerA().getY() - RectangleView.SHIFT));
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
                    newCoordinate.setX(Math.max(newCoordinate.getX(), imgOnScreen.getCornerA().getX() - RectangleView.SHIFT));
                    newCoordinate.setY(Math.min(newCoordinate.getY(), imgOnScreen.getCornerB().getY() + RectangleView.SHIFT));
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
                    newCoordinate.setX(Math.min(newCoordinate.getX(), imgOnScreen.getCornerB().getX() + RectangleView.SHIFT));
                    newCoordinate.setY(Math.max(newCoordinate.getY(), imgOnScreen.getCornerA().getY() - RectangleView.SHIFT));
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
                    newCoordinate.setX(Math.min(newCoordinate.getX(), imgOnScreen.getCornerB().getX() + RectangleView.SHIFT));
                    newCoordinate.setY(Math.min(newCoordinate.getY(), imgOnScreen.getCornerB().getY() + RectangleView.SHIFT));
                    rectangleView.changeBottomRightPosition(newCoordinate);
                }
                return true;
            }
        });
        rectangleView.getTopSide().getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CoordinateFloat oldCoordinate = new CoordinateFloat(
                        0,
                        rectangleView.getRectangle().getCornerA().getY() - RectangleView.SHIFT);
                CoordinateFloat newCoordinate = rectangleView.getTopSide().moveAction(event, oldCoordinate);
                if (newCoordinate != null) {
                    float y = Math.max(newCoordinate.getY(), imgOnScreen.getCornerA().getY() - RectangleView.SHIFT);
                    rectangleView.changeTopPosition(y);
                }
                return true;
            }
        });
        rectangleView.getLeftSide().getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CoordinateFloat oldCoordinate = new CoordinateFloat(
                        rectangleView.getRectangle().getCornerA().getX() - RectangleView.SHIFT,
                        0);
                CoordinateFloat newCoordinate = rectangleView.getLeftSide().moveAction(event, oldCoordinate);
                if (newCoordinate != null) {
                    float x = Math.max(newCoordinate.getX(), imgOnScreen.getCornerA().getX() - RectangleView.SHIFT);
                    rectangleView.changeLeftPosition(x);
                }
                return true;
            }
        });
        rectangleView.getBottomSide().getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CoordinateFloat oldCoordinate = new CoordinateFloat(
                        0,
                        rectangleView.getRectangle().getCornerB().getY() + RectangleView.SHIFT);
                CoordinateFloat newCoordinate = rectangleView.getBottomSide().moveAction(event, oldCoordinate);
                if (newCoordinate != null) {
                    float y = Math.min(newCoordinate.getY(), imgOnScreen.getCornerB().getY() + RectangleView.SHIFT);
                    rectangleView.changeBottomPosition(y);
                }
                return true;
            }
        });
        rectangleView.getRightSide().getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CoordinateFloat oldCoordinate = new CoordinateFloat(
                        rectangleView.getRectangle().getCornerB().getX() + RectangleView.SHIFT,
                        0);
                CoordinateFloat newCoordinate = rectangleView.getRightSide().moveAction(event, oldCoordinate);
                if (newCoordinate != null) {
                    float x = Math.min(newCoordinate.getX(), imgOnScreen.getCornerB().getX() + RectangleView.SHIFT);
                    rectangleView.changeRightPosition(x);
                }
                return true;
            }
        });
        rectangleView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
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
        setViewWidth(shadowTop, this.getWidth());

        shadowLeft.setY(rectangle.getCornerA().getY());
        setViewWidth(shadowLeft, rectangle.getCornerA().getX());
        setViewHeight(shadowLeft, rectangle.getHeight());

        shadowBottom.setY(rectangle.getCornerB().getY() + 1);
        setViewHeight(shadowBottom, this.getHeight() - rectangle.getCornerB().getY() - 1);
        setViewWidth(shadowBottom, this.getWidth());

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

    public void activateBreakpoints(
            List<MyLine> horizontalBreakpoints,
            List<MyLine> verticalBreakpoints,
            Activity activity) {
        this.rectangleView.activateBreakpoints(horizontalBreakpoints, verticalBreakpoints, activity);
    }

    public View getRectangleView() {
        return this.rectangleView;
    }

    public void setMaxRectangle(int maxWidth, int maxHeight) {
        rectangleView.setMaxRectangle(maxWidth, maxHeight);
    }
}
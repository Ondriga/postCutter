package com.example.postcutter.customViews.rectangle;

import android.view.MotionEvent;
import android.view.View;

public class InnerComponent extends Component{
    public InnerComponent(View view) {
        super(view);
    }

    @Override
    public CoordinateFloat moveAction(MotionEvent event, CoordinateFloat oldCoordinate) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                this.coordinate.setX(event.getX());
                this.coordinate.setY(event.getY());
                return null;
            case MotionEvent.ACTION_MOVE:
                float newY = this.getYDistance(event.getY());
                float newX = this.getXDistance(event.getX());
                return new CoordinateFloat(newX, newY);
        }
        return null;
    }
}

package postCutter.fe.postcutter.customViews.rectangle;

import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

public class InnerComponent extends Component {
    private final static int CLICK_TIME = 200;

    private long startEventTime;
    private View parent;

    public InnerComponent(View view) {
        super(view);
        parent = (View) view.getParent();
        parent = (View) parent.getParent();
    }

    @Override
    public CoordinateFloat moveAction(MotionEvent event, CoordinateFloat oldCoordinate) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                this.coordinate.setX(event.getX());
                this.coordinate.setY(event.getY());
                this.startEventTime = Calendar.getInstance().getTimeInMillis();
                return null;
            case MotionEvent.ACTION_MOVE:
                float newY = this.getYDistance(event.getY());
                float newX = this.getXDistance(event.getX());
                return new CoordinateFloat(newX, newY);
            case MotionEvent.ACTION_UP:
                long duration = Calendar.getInstance().getTimeInMillis() - this.startEventTime;
                if (duration < CLICK_TIME) {
                    parent.performClick();
                }
                return null;
        }
        return null;
    }
}

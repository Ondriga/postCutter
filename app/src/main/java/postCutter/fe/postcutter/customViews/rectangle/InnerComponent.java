/*
 * Source code for the frontend of Bachelor thesis.
 * InnerComponent class
 *
 * (C) Patrik Ondriga (xondri08)
 */

package postCutter.fe.postcutter.customViews.rectangle;

import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

/**
 * Representing inner rectangle of crop/replace frame. Extending the Component class.
 */
public class InnerComponent extends Component {
    /// Max time constant for touche to be consider as short click.
    private final static int CLICK_TIME = 200;

    /// Time when the component was touched.
    private long startEventTime;
    /// Parent View of this component View.
    private View parent;

    /**
     * Constructor.
     *
     * @param view UI View of this component.
     */
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

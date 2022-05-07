/*
 * Source code for the frontend of Bachelor thesis.
 * BarAnimationHandler class
 *
 * (C) Patrik Ondriga (xondri08)
 */

package postCutter.fe.postcutter.functions;

import android.view.View;
import android.view.ViewGroup;

/**
 * Representing handler for animating bar showing and hiding.
 */
public class BarAnimationHandler {
    /// Top bar.
    private final ViewGroup topBar;
    /// Bottom bar.
    private final ViewGroup bottomBar;
    /// Center section for blocking components when bars are showed.
    private final View screenBlocker;
    /// Flag if bars are showed.
    private boolean isShowed;

    /**
     * Builder class.
     */
    public static class Builder {
        /// Top bar.
        private ViewGroup topBar = null;
        /// Bottom bar.
        private ViewGroup bottomBar = null;
        /// Center section for blocking components when bars are showed.
        private View screenBlocker = null;
        /// Flag if bars are showed.
        private boolean isShowed = true;

        /**
         * Setter for top bar.
         *
         * @param topBar top bar.
         * @return builder.
         */
        public Builder topBar(ViewGroup topBar) {
            this.topBar = topBar;
            return this;
        }

        /**
         * Setter for bottom bar.
         *
         * @param bottomBar bottom bar.
         * @return builder.
         */
        public Builder bottomBar(ViewGroup bottomBar) {
            this.bottomBar = bottomBar;
            return this;
        }

        /**
         * Setter for screen blocker.
         *
         * @param screenBlocker screen blocker.
         * @return builder.
         */
        public Builder screenBlocker(View screenBlocker) {
            this.screenBlocker = screenBlocker;
            return this;
        }

        /**
         * Setter for flag if bars are showed.
         *
         * @param isShowed flag if showed.
         * @return builder.
         */
        public Builder showed(boolean isShowed) {
            this.isShowed = isShowed;
            return this;
        }

        /**
         * Build BarAnimationHandler object.
         *
         * @return BarAnimationHandler object.
         */
        public BarAnimationHandler build() {
            return new BarAnimationHandler(this);
        }
    }

    /**
     * Constructor.
     *
     * @param builder builder object.
     */
    private BarAnimationHandler(Builder builder) {
        this.topBar = builder.topBar;
        this.bottomBar = builder.bottomBar;
        this.isShowed = builder.isShowed;
        this.screenBlocker = builder.screenBlocker;

        if (isShowed) {
            show();
        } else {
            hide();
        }
    }

    /**
     * Toggle between show and hide bars.
     */
    public void showHide() {
        if (!isShowed) {
            show();
        } else {
            hide();
        }
        isShowed = !isShowed;
    }

    /**
     * Show bars and screen blocker.
     */
    private void show() {
        if (topBar != null) {
            topBar.animate().translationY(0).setDuration(500);
            topBar.setVisibility(View.VISIBLE);
        }
        if (bottomBar != null) {
            bottomBar.animate().translationY(0).setDuration(500);
            bottomBar.setVisibility(View.VISIBLE);
        }
        if (screenBlocker != null) {
            screenBlocker.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Hide bars and screen blocker.
     */
    private void hide() {
        if (topBar != null) {
            topBar.animate().translationY(0).setDuration(0);
            topBar.animate().translationY(-500).setDuration(500);
            topBar.setVisibility(View.INVISIBLE);
        }
        if (bottomBar != null) {
            bottomBar.animate().translationY(0).setDuration(0);
            bottomBar.animate().translationY(500).setDuration(500);
            bottomBar.setVisibility(View.INVISIBLE);
        }
        if (screenBlocker != null) {
            screenBlocker.setVisibility(View.GONE);
        }
    }
}

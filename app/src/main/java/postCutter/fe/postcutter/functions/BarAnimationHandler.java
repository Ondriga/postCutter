package postCutter.fe.postcutter.functions;

import android.view.View;
import android.view.ViewGroup;

public class BarAnimationHandler {
    private final ViewGroup topBar;
    private final ViewGroup bottomBar;
    private final View screenBlocker;
    private boolean isShowed;

    public static class Builder {
        private ViewGroup topBar = null;
        private ViewGroup bottomBar = null;
        private View screenBlocker = null;
        private boolean isShowed = true;

        public Builder topBar(ViewGroup topBar) {
            this.topBar = topBar;
            return this;
        }

        public Builder bottomBar(ViewGroup bottomBar) {
            this.bottomBar = bottomBar;
            return this;
        }

        public Builder screenBlocker(View screenBlocker) {
            this.screenBlocker = screenBlocker;
            return this;
        }

        public Builder showed(boolean isShowed) {
            this.isShowed = isShowed;
            return this;
        }

        public BarAnimationHandler build() {
            return new BarAnimationHandler(this);
        }
    }

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

    public void showHide() {
        if (!isShowed) {
            show();
        } else {
            hide();
        }
        isShowed = !isShowed;
    }

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

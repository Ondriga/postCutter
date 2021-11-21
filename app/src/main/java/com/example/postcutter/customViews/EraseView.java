package com.example.postcutter.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.postcutter.R;

public class EraseView extends FrameLayout {
    public EraseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.erase_view, this);
    }
}

package com.sheepyang.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by SheepYang on 2016/6/9 11:19.
 */
public class HomeTextView extends TextView{
    public HomeTextView(Context context) {
        super(context);
    }

    public HomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}

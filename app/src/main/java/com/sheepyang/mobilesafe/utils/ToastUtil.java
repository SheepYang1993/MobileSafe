package com.sheepyang.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by SheepYang on 2016/6/2 10:26.
 */
public class ToastUtil {
    private static Toast mToast = null;
    public static void show(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }
}

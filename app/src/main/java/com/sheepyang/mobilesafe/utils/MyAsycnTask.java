package com.sheepyang.mobilesafe.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by SheepYang on 2016/6/10 15:43.
 */
public abstract class MyAsycnTask {
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            postTask();
        }
    };
    /**
     * 在子线程之前执行的代码
     */
    public abstract void preTask();

    /**
     * 在子线程之中执行的代码
     */
    public abstract void doInBack();

    /**
     * 在子线程之后执行的代码
     */
    public abstract void postTask();

    /**
     * 执行
     */
    public void execute() {
        preTask();
        new Thread(){
            @Override
            public void run() {
                doInBack();
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    };
}

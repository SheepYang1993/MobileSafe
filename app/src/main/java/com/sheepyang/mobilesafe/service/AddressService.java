package com.sheepyang.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.sheepyang.mobilesafe.R;
import com.sheepyang.mobilesafe.db.dao.AddressDao;

/**
 * Created by SheepYang on 2016/6/11 22:07.
 */
public class AddressService extends Service{
    private TelephonyManager telephonyManager;
    private MyPhoneStateListener myPhoneStateListener;
    private WindowManager windowManager;
    private View view;
    private MyOutGoingCallReceiver myOutGoingCallReceiver;
    private SharedPreferences sp;
    private int width;
    private int height;
    private WindowManager.LayoutParams params;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("config", MODE_PRIVATE);

        //代码注册监听外拨电话的广播接受者
        //需要的元素:1.广播接受者,2.设置监听的广播事件
        //1.设置广播接受者
        myOutGoingCallReceiver = new MyOutGoingCallReceiver();
        //2.设置接受的广播事件
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");//设置接受的广播事件
        //3.注册广播接受者
        registerReceiver(myOutGoingCallReceiver, intentFilter);

        telephonyManager =  (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener =  new MyPhoneStateListener();
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);


        // 获取屏幕的宽度
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // windowManager.getDefaultDisplay().getWidth();
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        width = outMetrics.widthPixels;
        height = outMetrics.heightPixels;
    }
    /**
     * 外拨电话的广播接受者
     * @author Administrator
     *
     */
    private class MyOutGoingCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //查询外拨电话的号码归属地
            //1.获取外拨电话
            String phone = getResultData();
            //2.查询号码归属地
            String queryAddress = AddressDao.queryAddress(getApplicationContext(), phone);
            //3.判断号码归属地是否为空
            if (!TextUtils.isEmpty(queryAddress)) {
                //显示toast
                showToast(queryAddress);
            }
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch(state) {
                case TelephonyManager.CALL_STATE_IDLE://空闲，挂断状态
                    hideToast();
                    break;
                case TelephonyManager.CALL_STATE_RINGING://响铃状态
                    String queryAddress = AddressDao.queryAddress(getApplicationContext(), incomingNumber);
                    if (TextUtils.isEmpty(queryAddress)) {
                        showToast(queryAddress);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://通话中状态
                    break;
                default:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    @Override
    public void onDestroy() {
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        //注销外拨电话广播接受者
        unregisterReceiver(myOutGoingCallReceiver);
        super.onDestroy();
    }

    /**
     * 显示toast
     */
    public void showToast(String queryAddress) {

        int[] bgcolor = new int[] {
                R.drawable.call_locate_white,
                R.drawable.call_locate_orange, R.drawable.call_locate_blue,
                R.drawable.call_locate_gray, R.drawable.call_locate_green };

        //1.获取windowmanager
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //将布局文件转化成view对象
        view = View.inflate(getApplicationContext(), R.layout.toast_custom, null);
        //初始化控件
        //view.findViewById表示去toast_custom找控件
        TextView tv_toastcustom_address = (TextView) view.findViewById(R.id.tv_toastcustom_address);
        tv_toastcustom_address.setText(queryAddress);

        //根据归属地提示框风格中设置的风格索引值设置toast显示的背景风格
        view.setBackgroundResource(bgcolor[sp.getInt("which", 0)]);

		/*textView = new TextView(getApplicationContext());
		textView.setText(queryAddress);
		textView.setTextSize(100);
		textView.setTextColor(Color.RED);*/

        //3.设置toast的属性
        //layoutparams是toast的属性,控件要添加到那个父控件中,父控件就要使用那个父控件的属性,表示控件的属性规则符合父控件的属性规则
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;//高度包裹内容
        params.width = WindowManager.LayoutParams.WRAP_CONTENT; //宽度包裹内容
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  //没有焦点
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE  // 不可触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; // 保持当前屏幕
        params.format = PixelFormat.TRANSLUCENT; // 透明
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE; // 执行toast的类型

        //设置toast位置
        //效果冲突,以默认的效果为主
        params.gravity = Gravity.LEFT | Gravity.TOP;

        params.x = sp.getInt("x", 100);//不是坐标,表示的距离边框的距离,根据gravity来设置的,如果gravity是left表示距离左边框的距离,如果是right表示距离有边框的距离
        params.y = sp.getInt("y", 100);//跟x的含义
        setTouch();
        //2.将view对象添加到windowManager中
        //params : layoutparams  控件的属性
        //将params属性设置给view对象,并添加到windowManager中
        windowManager.addView(view, params);
    }


    /**
     * toast触摸监听事件
     */
    private void setTouch() {
        view.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;

            //v : 当前的控件
            //event : 控件执行的事件
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //event.getAction() : 获取控制的执行的事件
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下的事件
                        System.out.println("按下了....");
                        //1.按下控件,记录开始的x和y的坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //移动的事件
                        System.out.println("移动了....");
                        //2.移动到新的位置记录新的位置的x和y的坐标
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();
                        //3.计算移动的偏移量
                        int dX = newX-startX;
                        int dY = newY-startY;
                        //4.移动相应的偏移量,重新绘制控件
                        params.x+=dX;
                        params.y+=dY;
                        //控制控件的坐标不能移出外拨电话界面
                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y=0;
                        }
                        if (params.x > width-view.getWidth()) {
                            params.x = width-view.getWidth();
                        }
                        if (params.y > height - view.getHeight() - 25) {
                            params.y = height - view.getHeight() - 25;
                        }

                        windowManager.updateViewLayout(view, params);//更新windowmanager中的控件
                        //5.更新开始的坐标
                        startX=newX;
                        startY=newY;
                        break;
                    case MotionEvent.ACTION_UP:
                        //抬起的事件
                        System.out.println("抬起了....");
                        //保存控件的坐标,保存的是控件的坐标不是手指坐标
                        //获取控件的坐标
                        int x = params.x;
                        int y = params.y;

                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("x", x);
                        edit.putInt("y", y);
                        edit.commit();
                        break;
                }
                //True if the listener has consumed the event, false otherwise.
                //true:事件消费了,执行了,false:表示事件被拦截了
                return true;
            }
        });
    }

    /**
     * 隐藏toast
     */
    public void hideToast(){
        if (windowManager != null && view!= null) {
            windowManager.removeView(view);//移出控件
            windowManager= null;
            view=null;
        }
    }
}

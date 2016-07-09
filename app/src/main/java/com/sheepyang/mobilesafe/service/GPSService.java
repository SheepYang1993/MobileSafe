package com.sheepyang.mobilesafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;

import java.util.List;

/**
 * Created by SheepYang on 2016/6/10 23:01.
 */
public class GPSService extends Service {
    private LocationManager locationManager;
    private MyLocationListener myLocationListener;
    private SharedPreferences sp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        // 1.获取位置的管理者
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 2.获取定位方式
        // 2.1获取所有的定位方式
        // enabledOnly : true : 返回所有可用的定位方式
        List<String> providers = locationManager.getProviders(true);
        for (String string : providers) {
            System.out.println(string);
        }
        // 2.2获取最佳的定位方式
        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(true);// 设置是否可以定位海拔,true:可以定位海拔,一定返回gps定位
        // criteria : 设置定位的属性,决定使用什么定位方式的
        // enabledOnly : true : 定位可用的就返回
        String bestProvider = locationManager.getBestProvider(criteria, true);
        System.out.println("最佳的定位方式:" + bestProvider);
        // 3.定位
        myLocationListener = new MyLocationListener();
        // provider : 定位方式
        // minTime : 定位的最小时间间隔
        // minDistance : 定位的最小距离间隔
        // listener : LocationListener
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(bestProvider, 0, 0, myLocationListener);
    }

    @Override
    public void onDestroy() {
        //关闭gps定位,高版本中已经不能这么做了,高版本中规定关闭和开启gps必须交由用户自己去实现
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(myLocationListener);
        super.onDestroy();
    }

    private class MyLocationListener implements LocationListener {
        //当定位位置改变的时候调用
        //location : 当前的位置
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();//获取纬度,平行
            double longitude = location.getLongitude();//获取经度
            //给安全号码发送一个包含经纬度坐标的短信
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sp.getString("safenum", ""), null, "longitude:"+longitude+"  latitude:"+latitude, null, null);
            //停止服务
            stopSelf();
        }
        //当定位状态改变的时候调用
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
        //当定位可用的时候调用
        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }
        //当定位不可用的时候调用
        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

    }
}

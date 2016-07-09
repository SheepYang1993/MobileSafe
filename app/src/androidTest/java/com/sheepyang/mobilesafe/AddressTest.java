package com.sheepyang.mobilesafe;

import android.content.SyncStatusObserver;
import android.test.AndroidTestCase;
import android.util.Log;

import com.sheepyang.mobilesafe.db.dao.AddressDao;
import com.sheepyang.mobilesafe.utils.Constants;

/**
 * Created by SheepYang on 2016/6/11 13:00.
 */
public class AddressTest extends AndroidTestCase{
    public void testAddress() {
        String local = AddressDao.queryAddress(getContext(), "13808526761");
        assertEquals("福建泉州移动",local);
    }
}

package com.sheepyang.mobilesafe;

import android.test.AndroidTestCase;

import com.sheepyang.mobilesafe.db.dao.AddressDao;
import com.sheepyang.mobilesafe.engine.TaskEngine;
import com.sheepyang.mobilesafe.entity.TaskInfo;

import java.util.List;

/**
 * Created by SheepYang on 2016/6/11 13:00.
 */
public class taskTest extends AndroidTestCase{
    private List<TaskInfo> list;

    public void testTask() {
        list = TaskEngine.getTaskAllInfo(getContext());
        assertEquals(1, list.size());
    }
}

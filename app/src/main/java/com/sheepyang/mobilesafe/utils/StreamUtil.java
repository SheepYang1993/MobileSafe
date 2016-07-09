package com.sheepyang.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by SheepYang on 2016/6/8 21:45.
 */
public class StreamUtil {
    public static String parserStreamUtil(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringWriter sw = new StringWriter();
        String str = null;
        while ((str = br.readLine()) != null) {
            sw.write(str);
        }
        sw.close();
        br.close();
        return sw.toString();
    }
}

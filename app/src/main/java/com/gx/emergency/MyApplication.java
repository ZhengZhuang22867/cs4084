package com.gx.emergency;

import android.app.Application;

import com.gx.emergency.bean.Goods;
import com.gx.emergency.util.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);//初始化LitePal数据库

    }
}

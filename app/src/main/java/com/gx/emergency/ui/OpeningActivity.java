package com.gx.emergency.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gx.emergency.R;
import com.gx.emergency.bean.AppendOptionVo;
import com.gx.emergency.bean.Goods;
import com.gx.emergency.bean.Symptom;
import com.gx.emergency.bean.User;
import com.gx.emergency.util.MPermissionUtils;
import com.gx.emergency.util.SPUtils;
import com.gx.emergency.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 开屏页面
 */
public class OpeningActivity extends AppCompatActivity {
    private Activity myActivity;
    private final int REQUEST_EXTERNAL_STORAGE = 1;
    //权限组（读写权限）
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        //设置页面布局
        setContentView(R.layout.activity_opening);
        try {
            initView();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
    private void initView() throws IOException, JSONException {
        StatusBarUtil.setStatusBar(myActivity,true);//设置当前界面是否是全屏模式（状态栏）
        StatusBarUtil.setStatusBarLightMode(myActivity,true);//状态栏文字颜色
        //请求权限
        MPermissionUtils.requestPermissionsResult(myActivity,
                REQUEST_EXTERNAL_STORAGE,
                PERMISSIONS_STORAGE,
                new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
                                    finish();
                                    return;
                                }
                                Boolean isFirst= (Boolean) SPUtils.get(myActivity,SPUtils.IF_FIRST,true);
                                if (isFirst){//第一次进来  初始化本地数据
                                    try {
                                        SPUtils.put(myActivity,SPUtils.IF_FIRST,false);//第一次
                                        //初始化数据
                                        //获取json数据
                                        String rewardJson = "";
                                        String rewardJsonLine;
                                        //assets文件夹下db.json文件的路径->打开db.json文件
                                        BufferedReader bufferedReader = null;
                                        bufferedReader = new BufferedReader(new InputStreamReader(myActivity.getAssets().open("db.json")));
                                        while (true) {
                                            if (!((rewardJsonLine = bufferedReader.readLine()) != null)) break;
                                            rewardJson += rewardJsonLine;
                                        }
                                        JSONObject jsonObject = new JSONObject(rewardJson);
                                        JSONArray goods = jsonObject.getJSONArray("goods");//获得物品列表
                                        JSONArray goodsTypes = jsonObject.getJSONArray("goodsType");//获得物品类型列表
                                        JSONArray symptoms = jsonObject.getJSONArray("symptom");//获得症状列表
                                        //遍历jsonArray，将每个JsonObject转换为对应的Goods对象
                                        //把物品列表保存到本地
                                        for (int i = 0, length = goods.length(); i < length; i++) {
                                            JSONObject o = goods.getJSONObject(i);
                                            Goods good = new Goods(o.getString("name"),
                                                    o.getString("type"),
                                                    o.getString("producedDate"),
                                                    o.getInt("expirationDate"),
                                                    o.getInt("count"),
                                                    o.getString("location")
                                            );
                                            good.save();//保存到本地
                                        }
                                        //把物品类型列表保存到本地
                                        for (int i = 0, length = goodsTypes.length(); i < length; i++) {
                                            JSONObject o = goodsTypes.getJSONObject(i);
                                            AppendOptionVo appendOptionVo = new AppendOptionVo(
                                                    o.getInt("id"),
                                                    o.getString("name")
                                            );
                                            appendOptionVo.save();//保存到本地
                                        }
                                        //把症状列表保存到本地
                                        for (int i = 0, length = symptoms.length(); i < length; i++) {
                                            JSONObject o = symptoms.getJSONObject(i);
                                            JSONArray steps= o.getJSONArray("steps");
                                            Symptom symptom=new Symptom(o.optString("name"),
                                                    o.getString("symptom"),
                                                    o.getString("tools"),o.getString("notice"),
                                                    o.getString("forbid"),steps.toString());
                                            symptom.save();//保存到本地
                                        }  //初始化用户信息
                                        User user = new User("123","123");
                                        user.save();
                                    } catch (IOException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                //两秒后跳转到主页面
                                Intent intent2 = new Intent();
                                intent2.setClass(OpeningActivity.this, LoginActivity.class);
                                startActivity(intent2);
                                finish();
                            }
                        }, 2000);
                    }

                    @Override
                    public void onPermissionDenied() {//拒绝的话
                        finish();//退出
                    }
                });
    }


    //权限请求的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {

    }
}

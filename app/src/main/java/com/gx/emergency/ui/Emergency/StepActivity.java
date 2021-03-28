package com.gx.emergency.ui.Emergency;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gx.emergency.R;
import com.gx.emergency.dialog.InfoDialog;
import com.gx.emergency.util.StatusBarUtil;
import com.gx.emergency.widget.MyActionBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Locale;

public class StepActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private Activity myActivity;
    private MyActionBar myActionBar;
    private TextView tvStep;//步骤
    private ImageView ivVoice;//语音播报图标
    private TextView tvContent;//内容
    private FloatingActionButton btnNotice;//注意
    private FloatingActionButton btnForbid;//禁止
    private FloatingActionButton btnExit;//退出
    private FloatingActionButton btnNextStep;//下一步
    private JSONArray steps;//步骤
    private String notice;//注意事项
    private String forbid;//禁止
    private boolean isVoice;//是否正在播报语音
    private int i=0;//步骤标志
    //定义一个tts对象
    private TextToSpeech tts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity=this;
        setContentView(R.layout.activity_step);
        tvStep=findViewById(R.id.tv_step);
        tvContent=findViewById(R.id.tv_content);
        ivVoice=findViewById(R.id.iv_voice);
        btnNotice=findViewById(R.id.btn_notice);
        btnForbid=findViewById(R.id.btn_forbid);
        btnExit=findViewById(R.id.btn_exit);
        btnNextStep=findViewById(R.id.btn_next_step);
        myActionBar = findViewById(R.id.myActionBar);
        myActionBar.setData(myActivity,"detailed steps",R.drawable.ic_back, 0, 1, 0, new MyActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                if (i==0){
                    finish();
                }else {
                    nextStep(--i);
                }
            }

            @Override
            public void onRightClick() {
            }
        });
        initView();//初始化页面
        setViewLineListener();//监听事件
    }

    /**
     * 初始化
     */
    private void initView() {
        StatusBarUtil.setStatusBar(myActivity,true);//设置当前界面是否是全屏模式（状态栏）
        StatusBarUtil.setStatusBarLightMode(myActivity,true);//状态栏文字颜色
        //初始化tts监听对象
        tts = new TextToSpeech(this, this);
        try {
            steps=new JSONArray(getIntent().getStringExtra("steps"));//获取步骤数据
            notice=getIntent().getStringExtra("notice");//获取注意事项数据
            forbid=getIntent().getStringExtra("forbid");//获取禁止数据
            nextStep(0);//默认第一个步骤
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听事件
     */
    private void setViewLineListener(){
        //语言播报
        ivVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    isVoice=!isVoice;
                    if (isVoice){//正在播报语音
                        ivVoice.setImageResource(R.drawable.ic_close_voice);//切换关闭语音图标
                        tts.speak(steps.getString(i), TextToSpeech.QUEUE_FLUSH, null);//开始语音
                    }else {//
                        ivVoice.setImageResource(R.drawable.ic_voice_broadcast);//切换开始语音图标
                        tts.stop();//停止语音
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //注意
        btnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoDialog infoDialog=new InfoDialog(myActivity,"matters need attention",notice);
                infoDialog.show(getSupportFragmentManager(),"");//显示弹窗
            }
        });
        //禁止
        btnForbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoDialog infoDialog=new InfoDialog(myActivity,"Prohibited items",forbid);
                infoDialog.show(getSupportFragmentManager(),"");//显示弹窗
            }
        });
        //退出
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//关闭当前页面
            }
        });
        //开始
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep(++i);//下一个步骤
            }
        });
    }

    /**
     * 下一步
     * @param index
     */
    private void nextStep(int index){

        try {
            if (isVoice){//正在播报语音
                tts.speak(steps.getString(i), TextToSpeech.QUEUE_FLUSH, null);//开始语音播报
            }
            tvStep.setText(String.format(Locale.getDefault(),"step%s",index+1));//设置步骤标题
            tvContent.setText(steps.getString(index));//设置步骤内容
            if (index==steps.length()-1){//最后一个步骤
                btnNextStep.setVisibility(View.GONE);//隐藏下一步按钮
            }else {
                btnNextStep.setVisibility(View.VISIBLE);//显示下一步按钮
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {//返回
        if (i==0){
            super.onBackPressed();
        }else {
            nextStep(--i);//下一个步骤
        }
    }

    @Override
    public void onInit(int status) {
        // 判断是否转化成功
        if (status == TextToSpeech.SUCCESS){
            //默认设定语言为中文，原生的android貌似不支持中文。
            int result = tts.setLanguage(Locale.CHINESE);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(myActivity, "", Toast.LENGTH_SHORT).show();
            }else{
                //不支持中文就将语言设置为英文
                tts.setLanguage(Locale.US);
            }
        }
    }
}

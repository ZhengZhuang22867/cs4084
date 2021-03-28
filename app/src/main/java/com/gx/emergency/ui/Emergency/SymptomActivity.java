package com.gx.emergency.ui.Emergency;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gx.emergency.R;
import com.gx.emergency.adapter.ToolsAdapter;
import com.gx.emergency.bean.Symptom;
import com.gx.emergency.bean.Tool;
import com.gx.emergency.util.StatusBarUtil;
import com.gx.emergency.widget.MyActionBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;


/**
 * 症状页面
 */
public class SymptomActivity extends AppCompatActivity {
    private Activity myActivity;
    //控件
    private MyActionBar myActionBar;//标题栏
    private TextView tvName;//名称
    private TextView tvSymptom;//症状
    private LinearLayout llEmpty;//空标志
    private RecyclerView rvToolList;//工具列表
    private ToolsAdapter toolsAdapter;//工具适配器
    private FloatingActionButton btnActive;//开始
    private List<Tool> list;//工具列表数据
    private JSONArray steps;//步骤
    private Symptom symptom;//症状
    private Gson gson=new Gson().newBuilder().setDateFormat("yyyy-MM-dd").create();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity=this;
        setContentView(R.layout.activity_symptom);
        tvName=findViewById(R.id.tv_name);
        tvSymptom=findViewById(R.id.tv_symptom);
        llEmpty=findViewById(R.id.ll_empty);
        rvToolList=findViewById(R.id.rv_tool_list);
        btnActive=findViewById(R.id.btn_active);
        myActionBar = findViewById(R.id.myActionBar);
        myActionBar.setData(myActivity,"Symptom Tool Information",R.drawable.ic_back, 0, 1, 0, new MyActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        try {
            initView();//初始化页面
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setViewLineListener();//监听事件
    }

    /**
     * 初始化页面
     */
    private void initView() throws JSONException {
        StatusBarUtil.setStatusBar(myActivity,true);//设置当前界面是否是全屏模式（状态栏）
        StatusBarUtil.setStatusBarLightMode(myActivity,true);//状态栏文字颜色
        String symptomStr =  myActivity.getIntent().getStringExtra("symptom");//获取页面传递参数 症状信息
        Type symptomType=new TypeToken<Symptom>(){}.getType();//转换类型
        symptom=gson.fromJson(symptomStr,symptomType);//转成实体类
        if (symptom!=null){//症状信息不为空
            tvName.setText(symptom.getName());//设置症状名称
            if (!"".equals(symptom.getSymptom())){//如果症状不为空
                tvSymptom.setText(String.format(Locale.getDefault(),"(%s)",symptom.getSymptom()));//设置症状
            }
            Type toolType=new TypeToken<List<Tool>>(){}.getType();//
            String toolStr = symptom.getTools();//获取工具数据
            list=gson.fromJson(toolStr,toolType);//字符串转实体类
            steps=new JSONArray(symptom.getSteps());//把步骤信息转换JSONArray
            //=1.1、创建布局管理器
            LinearLayoutManager layoutManager=new LinearLayoutManager(myActivity);
            //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            //=1.3、设置recyclerView的布局管理器
            rvToolList.setLayoutManager(layoutManager);
            //==2、实例化适配器
            //=2.1、初始化适配器
            toolsAdapter=new ToolsAdapter(myActivity);
            //=2.3、设置recyclerView的适配器
            rvToolList.setAdapter(toolsAdapter);
            if (list.size()>0){//工具列表不为空
                rvToolList.setVisibility(View.VISIBLE);//显示列表
                llEmpty.setVisibility(View.GONE);//隐藏空标志
                toolsAdapter.addItem(list);//添加数据
            }else {
                rvToolList.setVisibility(View.GONE);//隐藏列表
                llEmpty.setVisibility(View.VISIBLE);//显示空标志
            }
            if (steps.length()>0){//存在步骤
               btnActive.setVisibility(View.VISIBLE);//显示开始按钮
            }
        }
    }

    /**
     * 监听事件
     */
    private void setViewLineListener(){
        //开始
        btnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (steps.length()>0){
                    Intent intent=new Intent(myActivity,StepActivity.class);
                    intent.putExtra("steps",steps.toString());//步骤数据
                    intent.putExtra("notice",symptom.getNotice());//注意事项
                    intent.putExtra("forbid",symptom.getForbid());//禁止
                    startActivity(intent);//跳转到步骤页面
                }
            }
        });
    }
}

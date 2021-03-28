package com.gx.emergency.ui.MedicineBag;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.TimePickerView;
import com.gx.emergency.R;
import com.gx.emergency.bean.AppendOptionVo;
import com.gx.emergency.bean.Goods;
import com.gx.emergency.dialog.BottomOptionDialog;
import com.gx.emergency.util.KeyBoardUtil;
import com.gx.emergency.util.StatusBarUtil;
import com.gx.emergency.widget.MyActionBar;

import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 添加物品页面
 */
public class AddActivity extends AppCompatActivity {
    private Activity myActivity;
    //控件
    private MyActionBar myActionBar;//标题栏
    private LinearLayout llName;//名称
    private LinearLayout llType;//类型
    private LinearLayout llProduction;//生产日期
    private LinearLayout llExpiration;//保质期
    private LinearLayout llLocation;//存放地点
    private EditText etName;//名称
    private TextView tvType;//类型
    private TextView tvProduction;//生产日期
    private EditText etExpiration;//保质期
    private EditText etCount;//数量
    private EditText etLocation;//存放地点
    private Button btnSave;//保存
    private List<AppendOptionVo> typeList;//类型列表
    private BottomOptionDialog typeDialog;//类型弹窗
    private SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity=this;
        setContentView(R.layout.activity_add);
        llName=findViewById(R.id.ll_name);
        llType=findViewById(R.id.ll_type);
        llProduction=findViewById(R.id.ll_production);
        llExpiration=findViewById(R.id.ll_expiration);
        llLocation=findViewById(R.id.ll_location);
        etName=findViewById(R.id.et_name);
        tvType=findViewById(R.id.tv_type);
        tvProduction=findViewById(R.id.tv_production);
        etExpiration=findViewById(R.id.et_expiration);
        etCount=findViewById(R.id.et_count);
        etLocation=findViewById(R.id.et_location);
        btnSave=findViewById(R.id.btn_save);
        myActionBar = findViewById(R.id.myActionBar);
        myActionBar.setData(myActivity,"：Add drugs/items",R.drawable.ic_back, 0, 1, 0, new MyActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        initView();//初始化页面
        setViewListener();//设置监听事件
    }

    /**
     * 初始化
     */
    private void initView() {
        StatusBarUtil.setStatusBar(myActivity,true);//设置当前界面是否是全屏模式（状态栏）
        StatusBarUtil.setStatusBarLightMode(myActivity,true);//状态栏文字颜色
        typeDialog =new BottomOptionDialog(myActivity,"Category");//声明一个底部弹窗
        typeList= DataSupport.findAll(AppendOptionVo.class);//获取类型列表数据
    }
    /**
     * 设置监听事件
     */
    private void setViewListener() {
        //类型
        llType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtil.hideKeyboard(v);
                typeDialog.setData(typeList, new BottomOptionDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(PopupWindow popupWindow, AppendOptionVo appendOptionVo) {
                        tvType.setText(appendOptionVo.getName());//设置车辆文本
                        popupWindow.dismiss();//关闭弹窗
                    }
                });
                typeDialog.show();//显示
            }
        });
        //生产日期
        llProduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtil.hideKeyboard(v);//关闭键盘
                Calendar startDate = Calendar.getInstance();//设置起始年份
                Calendar endDate = Calendar.getInstance();//实例化
                endDate.set(2050,1,1);//设置结束年份
                Calendar selectedCalendar =Calendar.getInstance();//实例化
                TimePickerView pvTime = new TimePickerView.Builder(myActivity, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvProduction.setText(sf.format(date));
                    }
                }).setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                        .setCancelText("cancel")//取消按钮文字
                        .setSubmitText("confirm")//确认按钮文字
                        .setContentSize(15)//滚轮文字大小
                        .setTitleSize(20)//标题文字大小
                        .setTitleText("date in produced")//标题文字
                        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                        .setSubmitColor(Color.parseColor("#1296db"))//确定按钮文字颜色
                        .setCancelColor(Color.parseColor("#585858"))//取消按钮文字颜色*/
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .setRangDate(null,startDate)
                        .setDate(selectedCalendar)// 如果不设置的话，默认是系统时间*/
                        .setLabel("year","month","day",null,null,null)
                        .build();
                pvTime.show();
            }
        });
        //保存
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=etName.getText().toString();//获取名称值
                String type=tvType.getText().toString();//获取类型值
                String production=tvProduction.getText().toString();//获取生产日期值
                String expiration=etExpiration.getText().toString();//获取保质期值
                String count=etCount.getText().toString();//获取数量值
                String location=etLocation.getText().toString();//获取地点值
                if ("".equals(name)){//物品名称值为空
                    Toast.makeText(myActivity,"Please enter item name",Toast.LENGTH_LONG).show();
                    return;
                }
                if ("Please Select".equals(type)){//未选择
                    Toast.makeText(myActivity,"Please select a type",Toast.LENGTH_LONG).show();
                    return;
                }
                if ("Please Select".equals(production)){//未选择
                    Toast.makeText(myActivity,"Please enter the production date",Toast.LENGTH_LONG).show();
                    return;
                }
                if ("".equals(expiration)){//保质期值为空
                    Toast.makeText(myActivity,"Please enter the shelf life",Toast.LENGTH_LONG).show();
                    return;
                }
                if ("".equals(count)){//数量值为空
                    Toast.makeText(myActivity,"Please enter quantity",Toast.LENGTH_LONG).show();
                    return;
                }
                Goods goods=new Goods(name,type,production,Integer.valueOf(expiration),Integer.valueOf(count),location);//物品实例化
                goods.save();//保存数据到本地
                finish();//关闭当前页面
            }
        });
    }


}

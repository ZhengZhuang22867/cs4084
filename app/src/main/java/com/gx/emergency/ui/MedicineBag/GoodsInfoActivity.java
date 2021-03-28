package com.gx.emergency.ui.MedicineBag;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gx.emergency.R;
import com.gx.emergency.bean.Goods;
import com.gx.emergency.dialog.MessageDialog;
import com.gx.emergency.util.StatusBarUtil;
import com.gx.emergency.widget.MyActionBar;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GoodsInfoActivity extends AppCompatActivity {
    private Activity myActivity;
    //控件
    private MyActionBar myActionBar;//标题栏
    private TextView tvName;//名称
    private TextView tvType;//类型
    private TextView tvProduction;//生产日期
    private TextView tvExpiration;//保质期
    private TextView tvExpired;//是否已过期
    private TextView tvCount;//数量
    private TextView tvLocation;//存放地点
    private Button btnDelete;//删除
    private Goods goods;//物品数据
    private SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
    private Gson gson=new Gson().newBuilder().setDateFormat("yyyy-MM-dd").create();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity=this;
        setContentView(R.layout.activity_goods_info);
        tvName=findViewById(R.id.tv_name);
        tvType=findViewById(R.id.tv_type);
        tvProduction=findViewById(R.id.tv_production);
        tvExpiration=findViewById(R.id.tv_expiration);
        tvExpired=findViewById(R.id.tv_expired);
        tvCount=findViewById(R.id.tv_count);
        tvLocation=findViewById(R.id.tv_location);
        btnDelete=findViewById(R.id.btn_delete);
        myActionBar = findViewById(R.id.myActionBar);
        myActionBar.setData(myActivity,"Drug details",R.drawable.ic_back, 0, 1, 0, new MyActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        initView();//初始化页面
        //删除按钮点击事件
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //询问框是否删除
                MessageDialog messageDialog=new MessageDialog(myActivity, new MessageDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm){
                            goods.delete();//删除当前物品
                            finish();
                        }
                    }
                });
                messageDialog.setTitle("Delete the drug");
                messageDialog.show();//显示弹窗
            }
        });
    }

    /**
     * 初始化页面
     */
    private void initView() {
        StatusBarUtil.setStatusBar(myActivity,true);//设置当前界面是否是全屏模式（状态栏）
        StatusBarUtil.setStatusBarLightMode(myActivity,true);//状态栏文字颜色
        String goodsStr =  myActivity.getIntent().getStringExtra("goods");//获取传递过来的数据
        Type type=new TypeToken<Goods>(){}.getType();//转换类型
        goods=gson.fromJson(goodsStr,type);//转换为实体类
        if (goods!=null){//不为空
            String producedDate=goods.getProducedDate();//设置生成日期
            int expiration=goods.getExpirationDate();//设置保质期
            tvName.setText(goods.getName());//设置名称
            tvType.setText(goods.getType());//设置类型
            tvProduction.setText(producedDate);//设置生产日期
            tvCount.setText(String.format(Locale.getDefault(),"%s piece",goods.getCount()));//设置数量
            tvExpiration.setText(String.format(Locale.getDefault(),"%s six months",expiration));//设置保质期
            tvLocation.setText(goods.getLocation());//设置地点
            Date nowDate=new Date();//当前时间
            Calendar calendar=Calendar.getInstance();//实例化
            try {
                calendar.setTime(sf.parse(producedDate));//设置时间
                calendar.set(Calendar.MONTH,expiration+1);//设置月份
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (calendar.getTime().before(nowDate)){//已过期
                tvExpired.setText("Expired");//设置已过期
                tvExpired.setTextColor(getColor(R.color.colorRed));//文本为红色
            }else {
                tvExpired.setText("Not Expired");//设置未过期
            }
        }
    }
}

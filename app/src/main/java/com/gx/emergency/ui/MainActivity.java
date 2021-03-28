package com.gx.emergency.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.gx.emergency.R;
import com.gx.emergency.ui.Emergency.EmergencyFragment;
import com.gx.emergency.ui.Escape.EscapeFragment;
import com.gx.emergency.ui.MedicineBag.MedicineBagFragment;
import com.gx.emergency.widget.MyActionBar;

public class MainActivity extends AppCompatActivity {
    private Activity myActivity;
    //控件
    private MyActionBar myActionBar;//标题栏
    private LinearLayout llContent; //内容容器
    private RadioButton rbMedicineBag;//急救包
    private RadioButton rbEmergency;//紧急处理
    private RadioButton rbEscape;//逃生
    private Fragment[] fragments = new Fragment[]{null, null, null};//存放Fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity=this;
        setContentView(R.layout.activity_main);//设置页面布局
        myActionBar = findViewById(R.id.myActionBar);//标题栏
        llContent=findViewById(R.id.ll_main_content);//内容
        rbMedicineBag=findViewById(R.id.rb_main_medicine_bag);//急救包
        rbEmergency=findViewById(R.id.rb_main_emergency);//紧急处理
        rbEscape=findViewById(R.id.rb_main_escape);//逃生
        myActionBar.setData(myActivity,"medicine bag",0, 0, 0, getResources().getColor(R.color.colorPrimary), new MyActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
            }
        });
        initView();//初始化页面
        setViewListener();//设置监听事件
    }



    /*页面初始化*/
    private void initView() {
        //设置导航栏图标样式
        Drawable iconMedicineBag=getResources().getDrawable(R.drawable.selector_main_rb_medicine_bag);//设置主页图标样式
        iconMedicineBag.setBounds(0,0,60,60);//设置图标边距 大小
        rbMedicineBag.setCompoundDrawables(null,iconMedicineBag,null,null);//设置图标位置
        rbMedicineBag.setCompoundDrawablePadding(5);//设置文字与图片的间距
        Drawable iconEmergency=getResources().getDrawable(R.drawable.selector_main_rb_emergency);//设置主页图标样式
        iconEmergency.setBounds(0,0,60,60);//设置图标边距 大小
        rbEmergency.setCompoundDrawables(null,iconEmergency,null,null);//设置图标位置
        rbEmergency.setCompoundDrawablePadding(5);//设置文字与图片的间距
        Drawable iconEscape=getResources().getDrawable(R.drawable.selector_main_rb_escape);//设置主页图标样式
        iconEscape.setBounds(0,0,60,60);//设置图标边距 大小
        rbEscape.setCompoundDrawables(null,iconEscape,null,null);//设置图标位置
        rbEscape.setCompoundDrawablePadding(5);//设置文字与图片的间距
        switchFragment(0);
        rbMedicineBag.setChecked(true);
    }

    /**
     * 设置监听事件
     */
    private void setViewListener() {
        //急救包
        rbMedicineBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myActionBar.setTitle("Medicine bag");//设置标题
                switchFragment(0);
            }
        });
        //紧急处理
        rbEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myActionBar.setTitle("Emergency handling");//设置标题
                switchFragment(1);
            }
        });
        //逃生
        rbEscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myActionBar.setTitle("Escape");//设置标题
                switchFragment(2);
            }
        });
    }
    /**
     * 方法 - 切换Fragment
     *
     * @param fragmentIndex 要显示Fragment的索引
     */
    private void switchFragment(int fragmentIndex) {
        //在Activity中显示Fragment
        //1、获取Fragment管理器 FragmentManager
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        //2、开启fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //懒加载 - 如果需要显示的Fragment为null，就new。并添加到Fragment事务中
        if (fragments[fragmentIndex] == null) {
            switch (fragmentIndex) {
                case 0://MedicineBagFragment
                    fragments[fragmentIndex] = new MedicineBagFragment();
                    break;
                case 1://EmergencyFragment
                    fragments[fragmentIndex] = new EmergencyFragment();
                    break;
                case 2://EscapeFragment
                    fragments[fragmentIndex] = new EscapeFragment();
                    break;
            }
            //==添加Fragment对象到Fragment事务中
            //参数：显示Fragment的容器的ID，Fragment对象
            transaction.add(R.id.ll_main_content, fragments[fragmentIndex]);
        }

        //隐藏其他的Fragment
        for (int i = 0; i < fragments.length; i++) {
            if (fragmentIndex != i && fragments[i] != null) {
                //隐藏指定的Fragment
                transaction.hide(fragments[i]);
            }
        }
        //4、显示Fragment
        transaction.show(fragments[fragmentIndex]);

        //5、提交事务
        transaction.commit();
    }
    private boolean isExit;

    /**
     * 双击返回键退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                this.finish();
            } else {
                Toast.makeText(this, "Press exit again", Toast.LENGTH_SHORT).show();
                isExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit= false;

                    }
                }, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

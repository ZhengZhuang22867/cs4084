package com.gx.emergency.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.gx.emergency.R;
import com.gx.emergency.bean.User;
import com.gx.emergency.widget.MyActionBar;

import org.litepal.crud.DataSupport;

/**
 * 注册页面
 */
public class RegisterActivity extends AppCompatActivity {
    private Activity activity;
    private MyActionBar mTitleBar;//标题栏
    private EditText etAccount;//手机号
    private EditText etPassword;//密码
    private EditText etPasswordSure;//确认密码
    private TextView tvLogin;//登录
    private Button btnRegister;//注册按钮
    Gson gson =new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        setContentView(R.layout.activity_register);//加载页面
        etAccount =findViewById(R.id.et_account);//获取手机号
        etPassword=findViewById(R.id.et_password);//获取密码
        etPasswordSure=findViewById(R.id.et_password_sure);//获取确认密码
        tvLogin=findViewById(R.id.tv_login);//登录
        btnRegister=findViewById(R.id.btn_register);//获取注册按钮
        mTitleBar = findViewById(R.id.myActionBar);
        mTitleBar.setData(activity,"Register", R.drawable.ic_back, 0, 0, getColor(R.color.colorPrimary), new MyActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到登录页面
                Intent intent=new Intent(activity, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //设置注册点击按钮
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭虚拟键盘
                InputMethodManager inputMethodManager= (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                //获取请求参数
                String account= etAccount.getText().toString();
                String password=etPassword.getText().toString();
                String passwordSure=etPasswordSure.getText().toString();
                if ("".equals(account)){//账号不能为空
                    Toast.makeText(activity,"The account cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if ("".equals(password)){//密码为空
                    Toast.makeText(activity,"The password cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.equals(passwordSure)){//密码不一致
                    Toast.makeText(activity,"Entered passwords differ", Toast.LENGTH_LONG).show();
                    return;
                }
                User user = DataSupport.where("account = ?", account).findFirst(User.class);
                if (user == null) {
                    user = new User(account,password);
                    user.save();//保存用户信息
                    Intent intent = new Intent(activity, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(activity, "registered successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(activity, "The account has been registered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

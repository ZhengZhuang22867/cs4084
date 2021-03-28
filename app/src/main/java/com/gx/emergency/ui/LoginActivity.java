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
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private Activity activity;
    private MyActionBar mTitleBar;//标题栏
    private EditText etAccount;//手机号
    private EditText etPassword;//密码
    private TextView tvRegister;//注册
    private Button btnLogin;//登录按钮
    private Gson gson =new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        setContentView(R.layout.activity_login);//加载页面
        etAccount =findViewById(R.id.et_account);//获取手机号
        etPassword=findViewById(R.id.et_password);//获取密码
        tvRegister=findViewById(R.id.tv_register);//获取注册
        btnLogin=findViewById(R.id.btn_login);//获取登录

        mTitleBar = findViewById(R.id.myActionBar);
        mTitleBar.setData(activity,"Login",0, 0, 0, getColor(R.color.colorPrimary), new MyActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });

        //手机号注册
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册页面
                Intent intent=new Intent(activity,RegisterActivity.class);
                startActivity(intent);
            }
        });
        //设置注册点击按钮
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭虚拟键盘
                InputMethodManager inputMethodManager= (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                //获取请求参数
                String account= etAccount.getText().toString();
                String password=etPassword.getText().toString();
                if ("".equals(account)){//账号不能为空
                    Toast.makeText(activity,"The account cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if ("".equals(password)){//密码为空
                    Toast.makeText(activity,"The password cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                User user = DataSupport.where("account = ?", account).findFirst(User.class);
                if (user != null) {
                    if (!password.equals(user.getPassword())) {
                        Toast.makeText(activity, "wrong password", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(activity, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }else{
                    Toast.makeText(activity, "membernameno", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

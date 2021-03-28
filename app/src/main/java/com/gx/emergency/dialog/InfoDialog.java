package com.gx.emergency.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gx.emergency.R;

/**
 * 信息弹窗
 */
public class InfoDialog extends DialogFragment {
    private Context context;
    private Dialog dialog;//弹窗
    private View view;
    private String title;
    private String content;
    private ImageView ivClose;//关闭
    public InfoDialog(@NonNull Context context , String title,String content){
        this.context=context;
        this.title=title;
        this.content=content;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog=new Dialog(context, R.style.dialog);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//软键盘就会把dialog弹起，有的手机则会遮住dialog布局。
        view = View.inflate(getActivity(),R.layout.dialog_info,null);
        dialog.setContentView(view);
        initView();//初始化
        setViewListener();//事件监听
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.alpha = 1;
        lp.dimAmount = 0.5f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.windowAnimations=R.style.dialog_bottom_top;//设置弹窗动画
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return dialog;
    }

    /**
     * 初始化页面
     */
    private void initView(){
        TextView tvTitle=view.findViewById(R.id.tv_title);
        TextView tvContent=view.findViewById(R.id.tv_content);
        ivClose=view.findViewById(R.id.iv_close);
        tvTitle.setText(title);
        if ("".equals(content)){
            tvContent.setText("no data");
            tvContent.setGravity(Gravity.CENTER);
        }else {
            tvContent.setText(content);
        }
    }

    /**
     * 设置监听事件
     */
    private void setViewListener(){
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}

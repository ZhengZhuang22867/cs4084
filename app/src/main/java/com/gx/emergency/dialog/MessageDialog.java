package com.gx.emergency.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gx.emergency.R;

/**
 * 询问框
 */
public class MessageDialog extends Dialog implements View.OnClickListener{
    private TextView contentTxt;//内容
    private TextView titleTxt;//标题
    private TextView submitTxt;//确定
    private TextView cancelTxt;//取消
 
    
    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
 
 
    public MessageDialog(Context context) {
      super(context);
      this.mContext = context;
   }
   public MessageDialog(Context context, OnCloseListener listener) {
      super(context);
      this.mContext = context;
      this.listener = listener;
   }

   public MessageDialog(Context context, int themeResId, String content) {
      super(context, themeResId);
      this.mContext = context;
      this.content = content;
   }
 
 
   public MessageDialog(Context context, int themeResId, String content, OnCloseListener listener) {
      super(context, themeResId);
      this.mContext = context;
      this.content = content;
      this.listener = listener;
   }
 
 
   protected MessageDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
      super(context, cancelable, cancelListener);
      this.mContext = context;
   }
 
 
   public MessageDialog setTitle(String title){
      this.title = title;
      return this;
   }
 
 
   public MessageDialog setPositiveButton(String name){
      this.positiveName = name;
      return this;
   }
 
 
   public MessageDialog setNegativeButton(String name){
      this.negativeName = name;
      return this;
   }
 
 
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.dialog_message);
      setCanceledOnTouchOutside(false);
      initView();
   }
 
 
   private void initView(){
      contentTxt = (TextView)findViewById(R.id.content);
      titleTxt = (TextView)findViewById(R.id.title);
      submitTxt = (TextView)findViewById(R.id.submit);
      submitTxt.setOnClickListener(this);
      cancelTxt = (TextView)findViewById(R.id.cancel);
      cancelTxt.setOnClickListener(this);
 
 
      contentTxt.setText(content);
      if(!TextUtils.isEmpty(positiveName)){
         submitTxt.setText(positiveName);
      }
 
 
      if(!TextUtils.isEmpty(negativeName)){
         cancelTxt.setText(negativeName);
      }
 
 
      if(!TextUtils.isEmpty(title)){
         titleTxt.setText(title);
      }
    }
 
 
   @Override
   public void onClick(View v) {
      switch (v.getId()){
         case R.id.cancel:
            if(listener != null){
               listener.onClick(this, false);
            }
            this.dismiss();
            break;
         case R.id.submit:
            if(listener != null){
               listener.onClick(this, true);
            }
            break;
      }
   }
 
 
   public interface OnCloseListener{
      void onClick(Dialog dialog, boolean confirm);
   }
}

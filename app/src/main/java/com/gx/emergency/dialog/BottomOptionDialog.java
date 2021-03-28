package com.gx.emergency.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gx.emergency.R;
import com.gx.emergency.bean.AppendOptionVo;
import com.gx.emergency.util.KeyBoardUtil;

import java.util.List;

/**
 * 底部选项
 */
public class BottomOptionDialog {
    private Activity activity;//上下文
    private RadioGroup rgSelect;
    private LinearLayout llCancel;//取消
    private ScrollView svList;//列表数据
    private LinearLayout llEmpty;//空列表
    private PopupWindow popupWindow;//弹窗
    private List<AppendOptionVo> lists=null;
    private View view;
    private int rgOptionId=-1;

    public BottomOptionDialog(final Activity activity, String title){
        this.activity=activity;
        view = LayoutInflater.from(activity).inflate(R.layout.dialog_option_bottom, null);
        TextView tvTitle=view.findViewById(R.id.tv_title);//标题
        rgSelect = view.findViewById(R.id.rg_select);//RadioGroup
        svList=view.findViewById(R.id.sv_list);//列表数据
        llEmpty = view.findViewById(R.id.ll_empty);//空列表
        llCancel=view.findViewById(R.id.ll_cancel);
        tvTitle.setText(title);//设置标题
        popupWindow = new PopupWindow(view);
        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();//隐藏
            }
        });
    }

    /**
     * 设置数据和外部点击事件
     * @param list
     * @param onItemClickListener
     */
    public void setData(List<AppendOptionVo> list, final OnItemClickListener onItemClickListener){
        //获取原来选择的数据Id
        if(rgOptionId!=0){
            rgOptionId = rgSelect.getCheckedRadioButtonId();
        }
        rgSelect.removeAllViews();//清空原来的数据
        if (list!=null && list.size()>0){
            lists = list;
            svList.setVisibility(View.VISIBLE);//显示
            llEmpty.setVisibility(View.GONE);//隐藏
            for (final AppendOptionVo appendOptionVo:list){
                final RadioButton button=new RadioButton(activity);
                RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                button.setId(appendOptionVo.getId());//设置ID
                button.setText(appendOptionVo.getName());//设置名称
                button.setButtonDrawable(null);
                button.setBackgroundResource(R.drawable.selector_background);//设置选中样式
                button.setTextSize(16);//设置字体大小
                button.setPadding(50,50,50,30);//内边距
                if (appendOptionVo.getId() == rgOptionId ) {//判断是否是原来的数据
                    button.setChecked(true);//勾选原来的数据
                }
                rgSelect.addView(button,lp);//添加到rg视图
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener!=null){
                            onItemClickListener.onItemClick(popupWindow,appendOptionVo);
                        }
                    }
                });
            }
        }else {
            llEmpty.setVisibility(View.VISIBLE);//显示
            svList.setVisibility(View.GONE);//隐藏
            removeSaveId();//清空上次选项保存的勾勾
        }
    }
    /**
     * 判断列表是否有数据
     * @return
     */
    public boolean isHasData(){
        if (lists!=null && lists.size()>0){
            return true;
        }
        return false;
    }

    /**
     * 清空上次选项保存的勾勾
     */
    public void removeSaveId(){
        rgOptionId =0;
    }


    /**
     * 显示
     * @return
     */
    public void show(){
        KeyBoardUtil.hideKeyboard(view);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.dialog_bottom_top);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.5f;//设置阴影透明度
        activity.getWindow().setAttributes(lp);
        // 设置可以获取焦点
        popupWindow.setFocusable(true);
        // 设置可以触摸弹出框以外的区域
        popupWindow.setOutsideTouchable(false);
        //关闭时调用事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //背景还原 设置阴影透明度
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });
        // 显示方式，
        popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
    }

    /**
     * 隐藏
     */
    public void hide(){
        popupWindow.dismiss();
    }
    //外部接口
    public interface OnItemClickListener{
        void onItemClick(PopupWindow popupWindow, AppendOptionVo appendOptionVo);
    }
}

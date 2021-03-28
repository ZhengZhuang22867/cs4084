package com.gx.emergency.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gx.emergency.R;
import com.gx.emergency.bean.Goods;
import com.gx.emergency.ui.MedicineBag.GoodsInfoActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 物品适配器
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder>{
    private Activity activity;
    private List<Goods> list;//数据
    private SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");//时间格式
    private Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd").create();//gson
    public GoodsAdapter(Activity activity){
        this.activity=activity;//赋值
        this.list=new ArrayList<>();//初始化对象
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //绑定页面
        View view= LayoutInflater.from(activity).inflate(R.layout.item_rv_goods_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Goods goods=list.get(position);//获取单个物品
        if (goods!=null){//不为空
            holder.tvName.setText(goods.getName());//名称
            String producedDate=goods.getProducedDate();//生成日期
            int expiration=goods.getExpirationDate();//保质期
            Date nowDate=new Date();//当前时间
            Calendar calendar=Calendar.getInstance();//实例化Calendar
            try {
                calendar.setTime(sf.parse(producedDate));//设置时间
                calendar.set(Calendar.MONTH,expiration+1);//设置月份
            } catch (ParseException e) {
                e.printStackTrace();
            }
           if (calendar.getTime().before(nowDate)){//已过期
               holder.ivExpired.setVisibility(View.VISIBLE);//显示已过期标志
           }else {
               holder.ivExpired.setVisibility(View.GONE);//隐藏已过期标志
           }
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(activity, GoodsInfoActivity.class);
                   intent.putExtra("goods",gson.toJson(goods));//设置参数
                   activity.startActivity(intent);//跳转到物品信息页面
               }
           });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();//数据大小
    }
    public void addItem(List<Goods> listAdd) {
        //如果是加载第一页，需要先清空数据列表
        this.list.clear();
        if (listAdd!=null){
            //添加数据
            this.list.addAll(listAdd);
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;//名称
        private ImageView ivExpired;//已过期标志
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_name);
            ivExpired=itemView.findViewById(R.id.iv_expired);
        }
    }
}

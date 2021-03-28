package com.gx.emergency.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gx.emergency.R;
import com.gx.emergency.bean.Symptom;
import com.gx.emergency.ui.Emergency.SymptomActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 症状适配器
 */
public class SymptomAdapter extends RecyclerView.Adapter<SymptomAdapter.ViewHolder>{
    private Activity activity;
    private List<Symptom> list;//症状数据
    private Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd").create();//gson
    public SymptomAdapter(Activity activity){
        this.activity=activity;//赋值
        this.list=new ArrayList<>();//初始化
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //绑定页面
        View view= LayoutInflater.from(activity).inflate(R.layout.item_rv_symptom_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Symptom symptom=list.get(position);//获取单个症状信息
        if (symptom!=null){//不为空
            holder.tvName.setText(symptom.getName());//设置名称
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(activity, SymptomActivity.class);
                    intent.putExtra("symptom",gson.toJson(symptom));//传递参数
                    activity.startActivity(intent);//调整页面
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();//数据大小
    }
    public void addItem(List<Symptom> listAdd) {
        //如果是加载第一页，需要先清空数据列表
        this.list.clear();
        if (listAdd!=null){
            //添加数据
            this.list.addAll(listAdd);
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;//，名称
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_name);
        }
    }
}

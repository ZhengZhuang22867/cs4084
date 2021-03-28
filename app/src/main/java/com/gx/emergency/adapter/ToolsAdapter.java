package com.gx.emergency.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gx.emergency.R;
import com.gx.emergency.bean.Goods;
import com.gx.emergency.bean.Tool;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具适配器
 */
public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ViewHolder> {
    private Activity activity;
    private List<Tool> list;//工具数据
    private List<Goods> goodsList;//物品数据
    public ToolsAdapter(Activity activity){
        this.activity=activity;//赋值
        this.list=new ArrayList<>();//初始化
        this.goodsList= DataSupport.findAll(Goods.class);//获取本地物品信息
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //绑定页面
        View view= LayoutInflater.from(activity).inflate(R.layout.item_rv_tools_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tool tool=list.get(position);//获取单个工具
        if (tool!=null){//不为空
            holder.tvName.setText(tool.getName());//设置名称
            if (tool.isMust()){//必要
                holder.ivEssential.setVisibility(View.VISIBLE);//显示必要标志
            }else {//不必要
                holder.ivEssential.setVisibility(View.GONE);//隐藏必要标志
            }
            for (final Goods goods:goodsList) {//循环判断物品列表
                if (tool.getName().equals(goods.getName())){//存在
                    holder.ivLocation.setVisibility(View.VISIBLE);//显示地点标志
                    //点击地点标志
                    holder.ivLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //显示地点位置
                            Toast.makeText(activity,goods.getLocation(),Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                }else {//不存在
                    holder.ivLocation.setVisibility(View.GONE);//隐藏地点标志
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();//数据大小
    }
    public void addItem(List<Tool> listAdd) {
        //如果是加载第一页，需要先清空数据列表
        this.list.clear();
        if (listAdd!=null){
            //添加数据
            this.list.addAll(listAdd);
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged();
    }
    static class ViewHolder extends GoodsAdapter.ViewHolder {
        private TextView tvName;//名称
        private ImageView ivEssential;//必要
        private ImageView ivLocation;//地点
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_name);
            ivEssential=itemView.findViewById(R.id.iv_essential);
            ivLocation=itemView.findViewById(R.id.iv_location);
        }
    }
}

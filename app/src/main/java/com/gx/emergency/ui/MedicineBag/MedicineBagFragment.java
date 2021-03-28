package com.gx.emergency.ui.MedicineBag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gx.emergency.R;
import com.gx.emergency.adapter.GoodsAdapter;
import com.gx.emergency.bean.AppendOptionVo;
import com.gx.emergency.bean.Goods;
import com.gx.emergency.dialog.BottomOptionDialog;
import com.gx.emergency.util.KeyBoardUtil;
import com.gx.emergency.util.RecyclerViewSpaces;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;

/**
 * 急救包页面
 */
public class MedicineBagFragment extends Fragment {
    private Activity myActivity;
    private EditText etQuery;//搜索内容
    private ImageView ivSearch;//搜索图标
    private TextView tvType;//类型
    private LinearLayout llEmpty;//空数据
    private LinearLayout llType;//类型
    private RecyclerView rvMedicineList;//物品列表
    private FloatingActionButton btnAdd;//添加
    private GoodsAdapter goodsAdapter;//适配器
    private List<Goods> list;//物品列表
    private List<AppendOptionVo> typeList;//类型列表
    private BottomOptionDialog typeDialog;//类型弹窗

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myActivity= (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_medicine_bag,container,false);
        etQuery=view.findViewById(R.id.et_query);
        ivSearch=view.findViewById(R.id.iv_search);
        llEmpty=view.findViewById(R.id.ll_empty);
        llType=view.findViewById(R.id.ll_type);
        tvType=view.findViewById(R.id.tv_type);
        rvMedicineList=view.findViewById(R.id.rv_medicine_list);
        btnAdd=view.findViewById(R.id.btn_add);
        initView();
        setViewLineListener();
        return view;
    }
    /**
     * 初始化
     */
    private void initView() {
        //=1.1、创建布局管理器
        GridLayoutManager layoutManager = new GridLayoutManager(myActivity,3);//三列
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvMedicineList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        goodsAdapter=new GoodsAdapter(myActivity);
        //=2.3、设置recyclerView的适配器
        rvMedicineList.setAdapter(goodsAdapter);
        HashMap<String, Integer> mapSpaces = new HashMap<>();//间距
        mapSpaces.put(RecyclerViewSpaces.TOP_DECORATION, 10);//上间距
        mapSpaces.put(RecyclerViewSpaces.BOTTOM_DECORATION, 10);//下间距
        mapSpaces.put(RecyclerViewSpaces.LEFT_DECORATION, 10);//左间距
        mapSpaces.put(RecyclerViewSpaces.RIGHT_DECORATION, 10);//右间距
        rvMedicineList.addItemDecoration(new RecyclerViewSpaces(mapSpaces));//设置间距
        list= DataSupport.findAll(Goods.class);//获取物品列表
        if (list.size()>0){//如果存在物品
            rvMedicineList.setVisibility(View.VISIBLE);//显示列表
            llEmpty.setVisibility(View.GONE);//隐藏空标志
            goodsAdapter.addItem(list);//添加数据到列表
        }else {
            rvMedicineList.setVisibility(View.GONE);//隐藏列表
            llEmpty.setVisibility(View.VISIBLE);//显示空列表
        }
        typeDialog =new BottomOptionDialog(myActivity,"Category");//声明底部弹窗
        typeList=DataSupport.findAll(AppendOptionVo.class);//获取类型列表数据
    }
    /**
     * 设置事件
     */
    private void setViewLineListener() {
        //类型
        llType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //为类型弹窗设置数据
                typeDialog.setData(typeList, new BottomOptionDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(PopupWindow popupWindow, AppendOptionVo appendOptionVo) {
                        tvType.setText(appendOptionVo.getName());//设置类型文本
                        list=DataSupport.where("type = ?",appendOptionVo.getName()).find(Goods.class);
                        loadData(false);
                        popupWindow.dismiss();//关闭弹窗
                    }
                });
                typeDialog.show();//显示
            }
        });
        //添加
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到添加页面
                Intent intent=new Intent(myActivity,AddActivity.class);
                startActivity(intent);
            }
        });
        //软键盘搜索
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(true);//加载数据
            }
        });
        //点击软键盘中的搜索
        etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtil.hideKeyboard(v);//隐藏软键盘
                    loadData(true);//加载数据
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * 加载数据
     */
    private void loadData(boolean isQuery){
        if (isQuery){
            String name=etQuery.getText().toString();//获取搜索内容
            if ("".equals(name)){
                list= DataSupport.findAll(Goods.class);//查询全部
            }else {
                list=DataSupport.where("name like ?","%"+name+"%").find(Goods.class);//通过名称模糊查询物品
            }
            tvType.setText("Category");//设置类型文本
        }
        if (list.size()>0){//物品存在
            rvMedicineList.setVisibility(View.VISIBLE);//显示列表
            llEmpty.setVisibility(View.GONE);//隐藏空标志
            goodsAdapter.addItem(list);//添加数据
        }else {//物品不存在
            rvMedicineList.setVisibility(View.GONE);//隐藏列表
            llEmpty.setVisibility(View.VISIBLE);//显示空标志
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(true);//加载数据
    }
}

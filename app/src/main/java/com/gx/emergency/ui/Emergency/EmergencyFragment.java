package com.gx.emergency.ui.Emergency;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gx.emergency.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 紧急处理页面
 */
public class EmergencyFragment extends Fragment {
    private Activity myActivity;//上下文
    private TabLayout tlTitle;//状态标题
    private int[] stateId = {0, 1};
    private List<String> title =new ArrayList<>();//状态标题

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myActivity= (Activity) context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_emergency,container,false);
        //获取控件
        tlTitle = view.findViewById(R.id.tl_title);
        ViewPager2 vpOrderContainer = view.findViewById(R.id.vp_container);
        initView();
        //=========TableLayout+ViewPager2+Fragemnt初始化=============
        //=实例化适配器
        MyFragmentStateAdapter myFragmentStateAdapter = new MyFragmentStateAdapter(getActivity());
        //给ViewPager2设置适配器
        vpOrderContainer.setAdapter(myFragmentStateAdapter);
        //设置ViewPager2的预加载数，传入大于1的值来设置预加载数量,默认不预加载
        vpOrderContainer.setOffscreenPageLimit(1);
        //TabLayout与ViewPager2关联
        new TabLayoutMediator(tlTitle, vpOrderContainer, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                //设置TabLayout的显示
                // tab:当前处于选中状态的Tab对象
                // position:当前Tab所处的位置

                tab.setText(title.get(position));
            }
        }).attach(); // 不要忘记，否则没效果



        return view;
    }

    /**
     * 初始化页面
     */
    private void initView() {
        title.add("Emergency events");
        title.add("CPR");
    }
    class MyFragmentStateAdapter extends FragmentStateAdapter {
        //存放Fragment
        Fragment[] fragments;

        public MyFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
            fragments = new Fragment[title.size()];
        }

        //创建Fragment
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (fragments[position] == null) {
                //创建传递参数的Bundle
                Bundle bundle = new Bundle();
                bundle.putInt("stateId", stateId[position]);
                //创建Fragment
                EmergencyStateFragment emergencyStateFragment = new EmergencyStateFragment();
                //设置参数
                emergencyStateFragment.setArguments(bundle);
                fragments[position] = emergencyStateFragment;

            }
            return fragments[position];
        }

        //获取Fragment的数量
        @Override
        public int getItemCount() {
            return fragments.length;
        }
    }
}

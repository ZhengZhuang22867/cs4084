package com.gx.emergency.ui.Emergency;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gx.emergency.R;
import com.gx.emergency.adapter.SymptomAdapter;
import com.gx.emergency.bean.Symptom;
import com.gx.emergency.util.KeyBoardUtil;
import com.gx.emergency.util.RecyclerViewSpaces;

import org.json.JSONArray;
import org.json.JSONException;
import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * 紧急处理页面
 */
public class EmergencyStateFragment extends Fragment implements TextToSpeech.OnInitListener {
    private Activity myActivity;//上下文
    private int stateId;
    private EditText etQuery;//搜索内容
    private ImageView ivSearch;//搜索图标
    private LinearLayout llEmpty;//空数据
    private RecyclerView rvEmergencyList;//列表
    private SymptomAdapter symptomAdapter;
    private List<Symptom> list;//数据列表
    private TextView tvStep;//步骤
    private ImageView ivVoice;//语音播报图标
    private ImageView ivCpr;//心肺复苏图片
    private TextView tvContent;//心肺复苏内容
    private FloatingActionButton btnLastStep;//上一步
    private FloatingActionButton btnNextStep;//下一步
    private JSONArray title;//心肺复苏步骤标题
    private JSONArray steps;//心肺复苏步骤内容
    private boolean isVoice;//是否正在播报语音
    private int i=0;//步骤标志
    //定义一个tts对象
    private TextToSpeech tts;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myActivity=(Activity) context;//设置上下文
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        //获取传递的参数 orderStateId
        Bundle bundle=getArguments();
        stateId = bundle.getInt("stateId");
        if (stateId==0){//急救事件
            view=inflater.inflate(R.layout.fragment_event,container,false);
            etQuery=view.findViewById(R.id.et_query);
            ivSearch=view.findViewById(R.id.iv_search);
            llEmpty=view.findViewById(R.id.ll_empty);
            rvEmergencyList=view.findViewById(R.id.rv_emergency_list);
            initEventView();//加载
            setViewEventListener();//监听事件
        } else if (stateId==1){//心肺复苏
            view=inflater.inflate(R.layout.fragment_cpr,container,false);
            tvStep=view.findViewById(R.id.tv_step);
            tvContent=view.findViewById(R.id.tv_content);
            ivVoice=view.findViewById(R.id.iv_voice);
            ivCpr=view.findViewById(R.id.iv_cpr);
            btnLastStep=view.findViewById(R.id.btn_last_step);
            btnNextStep=view.findViewById(R.id.btn_next_step);
            initCprView();
            setViewCprListener();
        }
        return view;
    }
    //急救事件事件
    private void setViewEventListener() {
        //软键盘搜索
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEventData(true);//加载数据
            }
        });
        //点击软键盘中的搜索
        etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtil.hideKeyboard(v);//隐藏键盘
                    loadEventData(true);//加载数据
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 急救事件加载
     */
    private void initEventView() {
        //=1.1、创建布局管理器
        GridLayoutManager layoutManager = new GridLayoutManager(myActivity,3);//三列
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvEmergencyList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        symptomAdapter=new SymptomAdapter(myActivity);
        //=2.3、设置recyclerView的适配器
        rvEmergencyList.setAdapter(symptomAdapter);
        HashMap<String, Integer> mapSpaces = new HashMap<>();//间距
        mapSpaces.put(RecyclerViewSpaces.TOP_DECORATION, 10);//上间距
        mapSpaces.put(RecyclerViewSpaces.BOTTOM_DECORATION, 10);//下间距
        mapSpaces.put(RecyclerViewSpaces.LEFT_DECORATION, 10);//左间距
        mapSpaces.put(RecyclerViewSpaces.RIGHT_DECORATION, 10);//右间距
        rvEmergencyList.addItemDecoration(new RecyclerViewSpaces(mapSpaces));//设置间距
        list= DataSupport.findAll(Symptom.class);//查询全部症状信息
        if (list.size()>0){//不为空
            rvEmergencyList.setVisibility(View.VISIBLE);//显示列表
            llEmpty.setVisibility(View.GONE);//隐藏空标志
            symptomAdapter.addItem(list);//添加数据到列表
        }else {
            rvEmergencyList.setVisibility(View.GONE);//隐藏列表
            llEmpty.setVisibility(View.VISIBLE);//显示空标志
        }
    }
    /**
     * 急救事件加载数据
     */
    private void loadEventData(boolean isQuery) {
        if (isQuery) {
            String name = etQuery.getText().toString();//获取搜索内容值
            if ("".equals(name)) {
                list = DataSupport.findAll(Symptom.class);//查询全部
            } else {
                list = DataSupport.where("name like ?", "%" + name + "%").find(Symptom.class);//通过症状名称模糊查询症状列表
            }
        }
        if (list.size() > 0) {//症状数据存在
            rvEmergencyList.setVisibility(View.VISIBLE);//显示列表
            llEmpty.setVisibility(View.GONE);//隐藏空标志
            symptomAdapter.addItem(list);//添加数据
        } else {//症状数据不存在
            rvEmergencyList.setVisibility(View.GONE);//隐藏列表
            llEmpty.setVisibility(View.VISIBLE);//显示空标志
        }
    }

    /**
     * 心肺复苏加载
     */
    private void initCprView(){
        //初始化tts监听对象
        tts = new TextToSpeech(myActivity, this);
        title=new JSONArray();
        steps=new JSONArray();
        //设置当前页面需要的数据
        title.put("1、Judgment of consciousness");
        title.put("2、CHECK PULSE RESPIRATION");
        title.put("3、call for help");
        title.put("4、Determine if there is a carotid pulse");
        title.put("5、Loosen collar and belt");
        title.put("6、closed cardiac massage");
        title.put("7、airway open");
        title.put("8、artificial respiration");
        title.put("9、2 minutes of intensive CPR");
        title.put("10、Determine whether the recovery is working");
        title.put("11、Arrange the patient");
        steps.put("Pat the patient's shoulders with both hands and ask: \"Hello! What's wrong with you?\" No response to notification.");
        steps.put("Watch client's chest rise and fall for 5-10 seconds (1001, 1002, 1003, 1004, 1005...) Notify no breathing.");
        steps.put("Come on! Call the doctor! Push the rescue truck! Defibrillation apparatus!");
        steps.put("The middle finger and index finger of the right hand were used to draw from the median cricoid cartilage of the trachea to the place where the proximal carotid artery pulsed. No pulsation was reported (number 1001,1002,1003,1004,1005... Judge more than five seconds and less than 10 seconds).");
        steps.put("Loosen collar and belt。");
        steps.put("At the midpoint of the two nipples (middle and lower 1/3 of the sternum), press the patient's chest with the left palm, two hands overlapping, five fingers of the left hand upturned, arms straight, and 30 times with upper body strength (press frequency at least 100 times/min, press depth at least 125px).");
        steps.put("Raise the head and raise the jaw. No oral secretions, no dentures.");
        steps.put("Simple breathing apparatus was applied. One hand was fixed with \"CE\" maneuver, and the other hand was extruded into the simple breathing apparatus. The air was delivered 400-600ml per time, with a frequency of 10-12 times/min.");
        steps.put("2 minutes of high-efficiency CPR: compressions to mouth-to-mouth breathing =30:2 over 5 cycles. (Cardiac compressions start to ventilate and end).");
        steps.put("Listen for breathing sounds and feel for carotid pulses.");
        steps.put("advanced life suPport。");
        nextStep(0);
    }

    /**
     * 设置心肺复苏事件
     */
    private void setViewCprListener(){
        //语言播报
        ivVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    isVoice=!isVoice;
                    if (isVoice){//播放语音时
                        ivVoice.setImageResource(R.drawable.ic_close_voice);//切换关闭语音图标
                        tts.speak(steps.getString(i), TextToSpeech.QUEUE_FLUSH, null);//开始语音播报
                    }else {
                        ivVoice.setImageResource(R.drawable.ic_voice_broadcast);//切换开始语音图标
                        tts.stop();//关闭语音播报
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //上一步
        btnLastStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep(--i);
            }
        });
        //下一步
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep(++i);
            }
        });
    }

    @Override
    public void onInit(int status) {
        // 判断是否转化成功
        if (status == TextToSpeech.SUCCESS){
            //默认设定语言为中文，原生的android貌似不支持中文。
            int result = tts.setLanguage(Locale.CHINESE);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(myActivity, "", Toast.LENGTH_SHORT).show();
            }else{
                //不支持中文就将语言设置为英文
                tts.setLanguage(Locale.US);
            }
        }
    }
    /**
     * 下一步
     * @param index
     */
    private void nextStep(int index){

        try {
            if (isVoice){
                tts.speak(steps.getString(i), TextToSpeech.QUEUE_FLUSH, null);//开始语音播报
            }
            tvStep.setText(String.format(title.getString(index)));//设置当前步骤标题
            /*if (index==5){//图片
                ivCpr.setVisibility(View.VISIBLE);//显示图片
            }else {
                ivCpr.setVisibility(View.GONE);//隐藏图片
            }*/
            tvContent.setText(steps.getString(index));//设置当前步骤内容值
            if (index==steps.length()-1){//最后一步骤
                btnNextStep.setVisibility(View.GONE);//隐藏下一页按钮
            }else {
                btnNextStep.setVisibility(View.VISIBLE);//显示下一页按钮
            }
            if (index==0){//第一步骤
                btnLastStep.setVisibility(View.GONE);//隐藏上一页按钮
            }else {
                btnLastStep.setVisibility(View.VISIBLE);//显示下一页按钮
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

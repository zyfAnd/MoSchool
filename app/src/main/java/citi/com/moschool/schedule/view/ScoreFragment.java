package citi.com.moschool.schedule.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.library.LoopView.AlertView;
import com.example.library.LoopView.OnConfirmeListener;
import com.google.gson.JsonObject;
import com.melnykov.fab.FloatingActionButton;


import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import citi.com.moschool.R;
import citi.com.moschool.bean.Score;
import citi.com.moschool.schedule.adapter.ScoreAdapter;
import citi.com.moschool.util.JsonUtil;
import citi.com.moschool.util.LoadScorceListUtil;
import citi.com.moschool.util.OkHttpUtil;
import okhttp3.Call;

public class ScoreFragment extends Fragment implements OnConfirmeListener,View.OnClickListener {
    private String TAG = "ScoreFragment";
    private View view;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private LinearLayoutManager mLinearLayoutManager;
    private int start;
    private  String GET_GRADE_HOST = "http://139.199.210.131/getJsonOfGrade";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_score, null);
        initView();
        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        boolean flag = pref.getBoolean("scorceFlag", false);
        Log.i(TAG, "onCreateView: scorceFlag " + flag);
        SharedPreferences pre = getActivity().getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        String json = pre.getString("currentScore","");
        if(json!=null&&!json.equals(""))
        {
            initRecycler(json);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: --scorce");
    }

    private void initView()
    {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_score_recyclerview);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.score_fab);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mFloatingActionButton.setOnClickListener(this);
    }
    @Override
    public void result(String s) {
        Log.i(TAG, "result: " + s);
        String[] split = s.split(" ");
        String schoolYear = split[0].replace("学年", "");
        String schoolTerm = split[1].replace("学期", "").replace("第", "");
        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        String json = pref.getString("scorce:"+schoolYear + ":" + schoolTerm, "");
        initRecycler(json);
        Log.i(TAG, "result: json" + json);
    }
    private void initRecycler(String json)
    {
        List<Score> scores = LoadScorceListUtil.readBeans(json);
        if(scores!=null)
        {
            mRecyclerView.setAdapter(new ScoreAdapter(scores));
        }else {
            Toast.makeText(getActivity(), "成绩查询失败", Toast.LENGTH_SHORT).show();
        }
    }
    private void getGrade(int num,final String xqd)
    {
        final String years = "20" + num + "-" + "20" + (num + 1);
        Map<String,String> bodys = new HashMap<>();
        bodys.put("ddlxn",years);
        bodys.put("ddlxq",xqd);
        bodys.put("btn_zcj","btn_xq");
        OkHttpUtil.postAsync(GET_GRADE_HOST, new OkHttpUtil.ResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onSuccess(byte[] response) {
                String json = new String(response);
                List<Score> scores = LoadScorceListUtil.readBeans(json);
                try {
                    if (scores!=null) {
                        Log.i(TAG, "onSuccess: scorcejson: " + json);
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", Context.MODE_MULTI_PROCESS).edit();
                        editor.putBoolean("scorceFlag", true);
                        editor.putString("scorce:"+years + ":" + xqd, json);
                        editor.commit();
                    }
                } catch (Exception e) {

                }
                Log.i(TAG, "json:" + json);
             }

        },bodys,null);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.score_fab:
                SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
                String years = pref.getString("start", "");
                if(years!=null&&!years.equals("")) {
                    int startYears = Integer.valueOf(years);
                    new AlertView("选择当前学期", getContext(), startYears + 2000, 2000 + startYears + 4, this).show();
                }
                break;
        }
    }
}

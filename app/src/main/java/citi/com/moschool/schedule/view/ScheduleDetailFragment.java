package citi.com.moschool.schedule.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.library.LoopView.AlertView;
import com.example.library.LoopView.OnConfirmeListener;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import citi.com.moschool.R;
import citi.com.moschool.bean.Score;
import citi.com.moschool.main.view.LoginActivity;
import citi.com.moschool.schedule.adapter.RecyclerViewAdapter;
import citi.com.moschool.util.Constants;
import citi.com.moschool.util.JsonUtil;
import citi.com.moschool.util.LoadScorceListUtil;
import citi.com.moschool.util.OkHttpUtil;
import okhttp3.Call;

import com.melnykov.fab.FloatingActionButton;

/**
 * 课表详情页
 */

public class ScheduleDetailFragment extends Fragment implements View.OnClickListener, OnConfirmeListener {
    private static final String TAG = "ScheduleDetailFragment";
//    private String HOST = "http://139.199.210.131/";
    private View view;
    private static ImageView mImageView;
    private FloatingActionButton mFloatingActionButton;
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private int start;
    private String searchYear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ScheduleDetailFragment enter *************");
        view = inflater.inflate(R.layout.fragment_schedule_detail, null);
        initViews();
        Calendar c = Calendar.getInstance();
        final int currentYear = c.get(Calendar.YEAR);
        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
//        String json = pref.getString("start", "");
        searchYear = (currentYear-1)+"-"+currentYear;
        String json = pref.getString( searchYear+":" + "1", "");
        Log.i(TAG, "onCreateView: enter"+json);
        if(json!=null&&!json.equals(""))
        {
            getCourseJson(json);
        }
        return view;
    }



    private void initViews() {
        mImageView = (ImageView) view.findViewById(R.id.id_login_image);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        mImageView.setOnClickListener(this);
        mFloatingActionButton.setOnClickListener(this);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void getCourseJson(String json) {
        mImageView.setVisibility(View.GONE);
        List<String> courseJson = JsonUtil.getCourseJson(json);
        Log.i(TAG, "getCourseJson: json "+json);
        if (courseJson != null) {
            mRecyclerView.setAdapter(new RecyclerViewAdapter(getContext(), courseJson));
        } else {
            Log.i(TAG, "getCourseJson: 课表查询失败");
            Toast.makeText(getActivity(), "课表查询失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void searchScheduleOperation(final int num, final String xqd) {
        final String years = "20" + num + "-" + "20" + (num + 1);
        Log.i(TAG, "searchScheduleOperation: years" + years);
        Map<String, String> headers = new HashMap<>();
        headers.put("xnd", years);
        headers.put("xqd", xqd);
        headers.put("iscurrent", "false");
        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);


        OkHttpUtil.postAsync(Constants.GET_SCHEDULE_HOST, new OkHttpUtil.ResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onSuccess(byte[] response) {
                String json = new String(response);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    boolean flag = jsonObject.getBoolean("succ");
                    if (flag) {
                        if(searchYear!=null&&searchYear.equals(years))
                        {
                            Log.i(TAG, "onSuccess:searchYear "+year);
                            Log.i(TAG, "get_current 课表: "+json);
                            getCourseJson(json);
                        }
                        Log.i(TAG, "onSuccess: json: " + json);
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", Context.MODE_MULTI_PROCESS).edit();
                        editor.putBoolean("flag", true);
                        editor.putString(years + ":" + xqd, json);
                        editor.commit();
                    }
                } catch (Exception e) {

                }
                Log.i(TAG, "json:" + json);
            }
        }, headers, null);
    }
    @Override
    public void result(String s) {
        Log.i(TAG, "result: " + s);
        String[] split = s.split(" ");
        String schoolYear = split[0].replace("学年", "");
        String schoolTerm = split[1].replace("学期", "").replace("第", "");
        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        String json = pref.getString(schoolYear + ":" + schoolTerm, "");
        getCourseJson(json);
        Log.i(TAG, "result: json" + json);
    }
    /**
     * 点击事件监听
     *
     * @param v
     */

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: " + v.getId() + "");
        switch (v.getId()) {
            case R.id.id_login_image:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                getParentFragment().startActivityForResult(intent, 33);
                  startActivityForResult(intent, 33);
                break;
            case R.id.fab:
                SharedPreferences pref = getActivity().getSharedPreferences("data",Context.MODE_MULTI_PROCESS);
                String years = pref.getString("start", "");
                boolean flag = pref.getBoolean("flag", false);
                if(flag)
                {
                    if (years!=null&&!years.equals(""))
                    {
                        int startYears = Integer.valueOf(years);
                        new AlertView("选择当前学期", getContext(), startYears + 2000, 2000 + startYears + 4, this).show();
                    }

                }else{
                    Toast.makeText(getContext(),"请导入课表",Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: enter "+resultCode);
        if(resultCode==LoginActivity.RESULT_CODE)
        {
            Log.i(TAG, "onActivityResult: enetr"+resultCode);
            if(data!=null)
            {
                String years = data.getStringExtra("years");
                Log.i(TAG, "years: "+years);
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", Context.MODE_MULTI_PROCESS).edit();
                editor.putString("start", years);
                editor.commit();
                SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
                boolean flag = pref.getBoolean("flag", false);
                Log.i(TAG, "onActivityResult flag " + flag);
                if (!flag) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int current = year % 100;
                Log.i(TAG, "onCreateView: " + years);
                     if(years!=null&&!years.equals(""))
                     {
                         start = Integer.valueOf(years);
                         if (current - start <= 4) {
                             for (int i = 0; i < current - start; i++) {
                                 Log.i(TAG, "onActivityResult: search "+start + i);
                                 searchScheduleOperation(start + i, "1");
                                 searchScheduleOperation(start + i, "2");
                                 getGrade(start + i,"1");
                                 getGrade(start + i,"2");
                             }
                         }
                     }else{
                         Toast.makeText(getContext(),"years is null 查询失败",Toast.LENGTH_LONG).show();
                     }
                }
          }
        }

    }
    private void getGrade(final int num,final String xqd)
    {
        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final String years = "20" + num + "-" + "20" + (num + 1);
        Map<String,String> bodys = new HashMap<>();
        bodys.put("ddlxn",years);
        bodys.put("ddlxq",xqd);
        bodys.put("btn_zcj","btn_xq");
        OkHttpUtil.postAsync(Constants.GET_GRADE_HOST, new OkHttpUtil.ResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onSuccess(byte[] response) {
                String json = new String(response);
                List<Score> scores = LoadScorceListUtil.readBeans(json);
                try {
                    if (scores!=null) {
                      }
                        Log.i(TAG, "onSuccess: scorcejson: " + json);
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", Context.MODE_MULTI_PROCESS).edit();
                        editor.putBoolean("scorceFlag", true);
                        editor.putString("scorce:"+years + ":" + xqd, json);
                    if(searchYear!=null&&!searchYear.equals(year))
                    {
                        Log.i(TAG, "onSuccess:year "+year);
                        Log.i(TAG, "get_current: "+json);
//                        getCourseJson(json);
                        editor.putString("currentScore", json);
                        editor.commit();
                    }
                } catch (Exception e) {

                }
                Log.i(TAG, "json:" + json);
            }
        },bodys,null);
    }
}

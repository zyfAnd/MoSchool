package citi.com.moschool.schedule.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.Set;

import citi.com.moschool.R;
import citi.com.moschool.main.view.LoginActivity;

public class ScheduleDetailFragment extends Fragment implements View.OnClickListener {
    private static ImageView mImageView;
    private  static final String TAG = "ScheduleDetailFragment";
    private static  TextView courseTxt;
    private List<Map<String,String>> courses;
    private static int REQUESTCODE = 0x11;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_detail, null);
        Log.i(TAG, "onCreateView: *********************************************");
        mImageView = (ImageView) view.findViewById(R.id.id_login_image);
//        courseTxt = (TextView) view.findViewById(R.id.id_course);
        mImageView.setOnClickListener(this);
        return view;
    }


    public  void getCourseList(List<Map<String, String>> courseList) {
//        mImageView.setVisibility(View.GONE);
        Log.i(TAG, "getCourseList: "+courseList.size());
        StringBuffer buf = new StringBuffer();
        for(int i=0;i<courseList.size();i++)
        {
            Map<String, String> map = courseList.get(i);
            Set<String> keys = map.keySet();
            for (String key: keys)
            {
                buf.append(key+" "+map.get(key));
            }
        }
        mImageView.setVisibility(View.GONE);
//        courseTxt.setText(buf.toString());
//        courseTxt.setVisibility(View.VISIBLE);
//        Toast.makeText(getContext().getApplicationContext(),"")
        Log.i(TAG, "getCourseList: "+ buf.toString());
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: " + v.getId() + "");
        switch (v.getId()) {

            case R.id.id_login_image:
//                SharedPreferences.Editor login_data = getActivity().getSharedPreferences("login_data", Context.MODE_PRIVATE).edit();
                SharedPreferences pref = getActivity().getSharedPreferences("login_data", Context.MODE_PRIVATE);
                String username = pref.getString("username", "");
                Log.i(TAG, "onClick: username");
                if (username.equals("") || username == null) {
                    Log.i(TAG, "onClick: username");
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
//                    startActivityForResult(intent,REQUESTCODE);
                    startActivity(intent);
                }

                break;
        }
    }
}

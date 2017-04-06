package citi.com.moschool.util;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import citi.com.moschool.main.view.LoginActivity;
import citi.com.moschool.schedule.view.ScheduleDetailFragment;
import okhttp3.Call;

/**
 * Created by zhang on 2017/3/15.
 */

public class SearchDataUtil {
//    public  void searchScheduleOperation(final Context context,Map<String,String> )
//    {
//        String newScheduleUrl   =
//                Constants.SCHEDULE_BODY_URL.replace(Constants.LOGIN_BODY_NAME_USERNAME,userName).
//                        replace(Constants.STUDENTNAME,urlEncodeStudentName);
//        requestHeadersMap.put(Constants.HEADER_NAME_REFERER,newScheduleUrl);
//
//        Log.i(TAG, "searchScheduleOperation: ");
//
//        OkHttpUtil.postAsync(newScheduleUrl, new OkHttpUtil.ResultCallback() {
//            @Override
//            public void onError(Call call, Exception e) {
//                Log.i(TAG, "searchSchedule onError: --------------------");
//            }
//            @Override
//            public void onSuccess(byte[] response) {
//                String result = null;
//                try {
//                    if (response != null) {
//                        result = new String(response, "gb2312");
//                        List<Map<String, String>> courseList = JsoupUtil.getCourseList(result);
////                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
////                        intent.putExtra("Course",(Serializable) courseList);
////                        startActivity(intent);
//                        new ScheduleDetailFragment().getCourseList(courseList);
////                        context.t.finish();
//                    }
//                }catch (UnsupportedEncodingException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        },scheduleRequestBody,requestHeadersMap);
//    }
//    public static void
}

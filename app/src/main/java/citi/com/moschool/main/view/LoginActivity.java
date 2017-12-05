package citi.com.moschool.main.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import citi.com.moschool.R;
import citi.com.moschool.mine.CustomDialog;
import citi.com.moschool.util.Constants;
import citi.com.moschool.util.OkHttpUtil;
import okhttp3.Call;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public static final String RESPONSE_SCHEDULE = "ResponseJSON";
    public static final String RESPONSE_YEARS = "years";
    private String TAG = "LoginActivity";
    private AutoCompleteTextView usernameEt;
    private EditText passwordEt;
    private Button loginBtn;
    private CustomDialog dialog;
    public static final int RESULT_CODE = 200;
    //学号
    private static String userName;
    /**
     * 学生姓名
     */
    private String studentName;
    /**
     * url编码的学生姓名
     */
    private static String urlEncodeStudentName = null;

    private boolean isLogin = false;
//    private String HOST = "http://139.199.210.131/";
    //    private String HOST = "http://192.168.3.110/";
    private static Map<String, String> requestHeadersMap;
    private static Map<String, String> loginReqeustBody;
    private final static Map<String, String> scheduleRequestBody = new LinkedHashMap<>();
    private static Map<String, String> scoreRequestBody;

    private String str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dialog = new CustomDialog(this, R.style.CustomDialog);
        initView();
        initRequestData();
    }

    private void initView() {
        usernameEt = (AutoCompleteTextView) findViewById(R.id.id_username_et);
        passwordEt = (EditText) findViewById(R.id.id_password_et);
    }


    private void initRequestData() {
        String username = usernameEt.getText().toString();
        String pwd = passwordEt.getText().toString();
        requestHeadersMap = new LinkedHashMap<>();
        loginReqeustBody = new LinkedHashMap<>();

        loginReqeustBody = new LinkedHashMap<>();
        loginReqeustBody.put("usernumber", username);
        loginReqeustBody.put("pwd", pwd);
        loginReqeustBody.put("role", "1");
//        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_MULTI_PROCESS).edit();
//        editor.putString("pwd", pwd);
//        editor.putString("username", username);
//        editor.commit();
    }

    public void loginBtn(View view) {
        initRequestData();
        dialog.show();
        OkHttpUtil.postAsync(Constants.LOGIN_SCHOOL, new OkHttpUtil.ResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, "系统异常，请稍候重试！", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSuccess(byte[] response) {
                dialog.dismiss();
                String json = new String(response);
                Log.i(TAG, "onSuccess: " + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    boolean flag = jsonObject.getBoolean("succ");
                    if (flag) {

                        String usernumber = jsonObject.getString("usernumber");
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        int num = Integer.valueOf(usernumber.substring(0, 2));
//                        searchScheduleOperation(num);
                        Intent intent = new Intent();
                        intent.putExtra("years",num+"");
//                        LoginActivity.this.setIntent(intent);
                        LoginActivity.this.setResult(RESULT_CODE,intent);
                        LoginActivity.this.finish();

                    } else {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "json解析异常", Toast.LENGTH_SHORT).show();
                }
            }
        }, loginReqeustBody, null);
    }

    public void searchScheduleOperation(final int num) {
        final String years = "20" + num + "-" + "20" + (num + 1);
        Log.i(TAG, "searchScheduleOperation: enter----" + years);
        Map<String, String> headers = new HashMap<>();
        headers.put("xnd", years);
        headers.put("xqd", "1");
        headers.put("iscurrent", "false");
        OkHttpUtil.postAsync(Constants.GET_SCHEDULE_HOST, new OkHttpUtil.ResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(LoginActivity.this, "课表查询失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(byte[] response) {
                String json = new String(response);
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_MULTI_PROCESS).edit();
                editor.putBoolean("loginFlag", true);
                editor.putString("json", json);
                editor.putString("years", num+"");
                editor.commit();
                Log.i(TAG, "json:" + json);

                finish();
//                new ScheduleDetailFragment().getCourseJson(json, years);
//                LoginActivity.this.finish();
            }
        }, headers, null);
    }
}


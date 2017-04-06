package citi.com.moschool.main.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.LoopView.AlertView;
import com.example.library.LoopView.OnConfirmeListener;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import citi.com.moschool.R;
import citi.com.moschool.schedule.view.ScheduleDetailFragment;
import citi.com.moschool.util.Constants;
import citi.com.moschool.util.JsoupUtil;
import citi.com.moschool.util.OkHttpUtil;
import okhttp3.Call;

import static android.Manifest.permission.LOCATION_HARDWARE;
import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnConfirmeListener {

    private String TAG = "LoginActivity";
    private TextView dateTxt;
    private EditText usernameEt;
    private EditText passwordEt;
    private Button loginBtn;
    private EditText verificationCodeEt;
    private ImageView verificationCodeIv;
    private TextView refreshTxt;
    private byte[] verificationCode;
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

    private String schoolYear;
    private String schoolTerm;
    private static Map<String, String> requestHeadersMap;
    private static Map<String, String> loginReqeustBody;
    private final static Map<String, String> scheduleRequestBody = new LinkedHashMap<>();
    private static Map<String, String> scoreRequestBody;

    private String str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initRequestData();
        initLoginRequestBody();
        initScheduleRequestBody();
        initEvents();
        requestVerificationCode();
    }

    private void initView() {
        verificationCodeEt = (EditText) findViewById(R.id.id_verificationCode_et);
        verificationCodeIv = (ImageView) findViewById(R.id.id_verificationCode_iv);
        dateTxt = (TextView) findViewById(R.id.text);
        loginBtn = (Button) findViewById(R.id.id_login_btn);
        usernameEt = (EditText) findViewById(R.id.id_username_et);
        passwordEt = (EditText) findViewById(R.id.id_password_et);
        refreshTxt = (TextView) findViewById(R.id.id_refresh_txt);
        setVerificationCode();
    }

    public void selectDate(View view) {
        new AlertView("选择当前学期", LoginActivity.this, 2009, 2100, LoginActivity.this).show();
    }

    @Override
    public void result(String s) {
        dateTxt.setText(s);
        String yearAndTerm = dateTxt.getText().toString();
        //2016-2017学年 第1学期
        if (yearAndTerm != null && !yearAndTerm.equals("")) {
            String[] split = yearAndTerm.split(" ");
            schoolYear = split[0];
            schoolYear = schoolYear.replace("学年", "");
            schoolTerm = split[1].replace("学期", "").replace("第", "");
            Log.i(TAG, "initScheduleRequestBody: " + schoolYear + " " + schoolTerm);
            scheduleRequestBody.put(Constants.SCHEDULE_BODY_NAME_YEAR, schoolYear);
            scheduleRequestBody.put(Constants.SCHEDULE_BODY_NAME_TERM, schoolTerm);
        } else {
            scheduleRequestBody.put(Constants.SCHEDULE_BODY_NAME_YEAR, "2015-2016");
            scheduleRequestBody.put(Constants.SCHEDULE_BODY_NAME_TERM, "1");
        }
    }

    private void initRequestData() {
        requestHeadersMap = new LinkedHashMap<>();
        loginReqeustBody = new LinkedHashMap<>();

        scoreRequestBody = new LinkedHashMap<>();
        requestHeadersMap.put(Constants.HEADER_NAME_HOST, Constants.HEADER_VALUE_HOST);
        requestHeadersMap.put(Constants.HEADER_NAME_AGENT, Constants.HEADER_VALUE_AGENT);
    }

    private void initLoginRequestBody() {
        OkHttpUtil.getAsync(Constants.EDUCATION_SYSTEM_LOGIN_URL, new OkHttpUtil.ResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.i(TAG, "initLoginRequest onError: ---");
                loginReqeustBody.put(Constants.LOGIN_BODY_NAME_VIEWSTATE, Constants.LOGIN_BODY_VALUE_VIEWSTATE);
            }

            @Override
            public void onSuccess(byte[] response) {
                if (response == null) {
                    loginReqeustBody.put(Constants.LOGIN_BODY_NAME_VIEWSTATE, Constants.LOGIN_BODY_VALUE_VIEWSTATE);
                }
                String viewState = null;
                String result = null;
                try {
                    result = new String(response, "gb2312");
                    Log.i(TAG, "onSuccess: result " + result);
                    Map<String, String> viewStateValue = JsoupUtil.getViewStateValue(result);
                    viewState = viewStateValue.get(Constants.LOGIN_BODY_NAME_VIEWSTATE);
                    Log.i(TAG, "onSuccess: viewState " + viewState);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (viewState == null) {
                        loginReqeustBody.put(Constants.LOGIN_BODY_NAME_VIEWSTATE, Constants.LOGIN_BODY_VALUE_VIEWSTATE);
                    } else {
                        loginReqeustBody.put(Constants.LOGIN_BODY_NAME_VIEWSTATE, viewState);
                        Log.i(TAG, "initLoginRequestBody:" + viewState);
                    }
                }
            }
        });

        Log.i(TAG, "initLoginRequestBody: " + loginReqeustBody.get(Constants.LOGIN_BODY_NAME_VIEWSTATE));
        loginReqeustBody.put(Constants.LOGIN_BODY_NAME_BUTTON1, Constants.LOGIN_BODY_VALUE_BUTTON1);
        loginReqeustBody.put(Constants.LOGIN_BODY_NAME_HIDPDRS, Constants.LOGIN_BODY_VALUE_HIDPDRS);
        loginReqeustBody.put(Constants.LOGIN_BODY_NAME_LBLANGUAGE, Constants.LOGIN_BODY_VALUE_LBLANGUAGE);
        loginReqeustBody.put(Constants.LOGIN_BODY_NAME_HIDSC, Constants.LOGIN_BODY_VALUE_HIDSC);
        loginReqeustBody.put(Constants.LOGIN_BODY_NAME_TYPE, Constants.LOGIN_BODY_VALUE_TYPE);
        Set<String> strings = loginReqeustBody.keySet();
        for (String key : strings) {
            Log.i(TAG, "initLoginRequestBody: key " + key + " value:" + loginReqeustBody.get(key));
        }
    }

    //查询课表的一系列
    private void initScheduleRequestBody() {
        scheduleRequestBody.put(Constants.SCHEDULE_BODY_NAME_EVENTARGUMENT, Constants.SCHEDULE_BODY_VALUE_EVENTARGUMENT);
        scheduleRequestBody.put(Constants.SCHEDULE_BODY_NAME_EVENTTARGET, Constants.SCHEDULE_BODY_VALUE_EVENTTARGET);
    }

    private void initEvents() {
        Log.i(TAG, "initEvents: " + verificationCode);
        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = usernameEt.getText().toString();
                Log.i(TAG, "onClick: userName  " + userName);
                requestHeadersMap.put(Constants.HEADER_NAME_REFERER, Constants.HEADER_VALUE_REFERER + userName);

                loginReqeustBody.put(Constants.LOGIN_BODY_NAME_USERNAME, userName);
                loginReqeustBody.put(Constants.LOGIN_BODY_NAME_PASSWORD, passwordEt.getText().toString());
                loginReqeustBody.put(Constants.LOGIN_BODY_NAME_SECRETCODE, verificationCodeEt.getText().toString());
                Log.i(TAG, "onClick: userName:" + userName + "  password: " + passwordEt.getText().toString() + " secrercode :" + verificationCodeEt.getText().toString());
                OkHttpUtil.postAsync(Constants.EDUCATION_SYSTEM_LOGIN_URL, new OkHttpUtil.ResultCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i(TAG, "onError: ");
                    }

                    @Override
                    public void onSuccess(byte[] response) {
                        if (response != null) {
//                        Log.i(TAG, "onSuccess: login button ----"+response.toString());
                            parseResponseFromLogin(response);
                            if (isLoginSuccessful()) {
                                searchScheduleOperation(LoginActivity.this);
                            }

                        }
                    }
                }, loginReqeustBody, requestHeadersMap);
            }
        });
        refreshTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                requestVerificationCode();
            }
        });
    }

    private void setVerificationCode() {
        Log.i(TAG, "setVerificationCode: ---verificationCode " + verificationCode);
        if (verificationCode != null && verificationCode.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(verificationCode, 0, verificationCode.length);
            Bitmap changeBitmap = changeBitmapSize(bitmap, 140, 60);
            verificationCodeIv.setBackground(new BitmapDrawable((getResources()), changeBitmap));

            //*************************************
            verificationCode = null;
        } else {
            verificationCodeIv.setBackgroundResource(R.mipmap.loading_failed);
        }
    }

    private void requestVerificationCode() {
        OkHttpUtil.getAsync(Constants.VERIFICATION_CODE_URL, new OkHttpUtil.ResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onSuccess(byte[] response) {
                verificationCode = response;
                setVerificationCode();
            }
        });
    }

    private Bitmap changeBitmapSize(Bitmap bitmap, float width, float height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleX = (float) width / w;
        float scaleY = (float) height / h;
        matrix.postScale(scaleX, scaleY);
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return resizeBitmap;
    }

    //设置登录状态
    private void setLoginState(boolean loginState) {
        isLogin = loginState;
    }

    //返回当前登录状态
    private boolean isLoginSuccessful() {
        return isLogin;
    }

    /**
     * 通过解析post请求返回的数据，判断是否登录成功了
     */
    private void parseResponseFromLogin(byte[] response) {
        setLoginState(false);
        String result = null;
        String outputInfo = null;
        try {
            result = new String(response, "gb2312");
            Log.e("test", result);
            Map<String, String> returnInfo = JsoupUtil.getNameOrFailedInfo(result);
            studentName = returnInfo.get(Constants.STUDENTNAME);
            if (null != studentName) {
                // 将gb2312编码的学生姓名转为url编码的字符串
                urlEncodeStudentName = URLEncoder.encode(studentName, "gb2312");
                outputInfo = "登录成功";
                setLoginState(true);
            } else {
                String failedInfo = returnInfo.get(Constants.FAILEDINFO);
                if (null != failedInfo) {
                    outputInfo = failedInfo;
                } else {
                    outputInfo = "服务器错误，请重试!";
                    Log.i(TAG, "parseResponseFromLogin: else ");
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            outputInfo = "服务器错误，请重试!";
            Log.i(TAG, "parseResponseFromLogin: catch");
        } finally {
            Toast.makeText(LoginActivity.this, outputInfo, Toast.LENGTH_SHORT).show();
        }
    }

    public void searchScheduleOperation(final Context context) {
       final String newScheduleUrl =
                Constants.SCHEDULE_BODY_URL.replace(Constants.LOGIN_BODY_NAME_USERNAME, userName)
                        .replace(Constants.STUDENTNAME, urlEncodeStudentName);
        requestHeadersMap.put(Constants.HEADER_NAME_REFERER, newScheduleUrl);
        Map<String, String> headers = new HashMap<>();
        headers.put("Referer", Constants.HEADER_VALUE_REFERER + userName);
        OkHttpUtil.postAsync(newScheduleUrl, new OkHttpUtil.ResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.i(TAG, "onError: searchScheduleOperation ");
            }

            @Override
            public void onSuccess(byte[] response) {

                String result = null;
                String viewState = null;
                try {
                    result = new String(response, "gb2312");
                    Log.i(TAG, "onSuccess: searchScheduleOperation result " + result);
                    Map<String, String> viewStateValue = JsoupUtil.getViewStateValue(result);
                    viewState = viewStateValue.get(Constants.SCHEDULE_BODY_NAME_VIEWSTATE);
                    Log.i(TAG, "onSuccess: searchScheduleOperation  viewState " + viewState);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Log.i(TAG, "onSuccess: finally " + viewState);
                    str = viewState;
                    scheduleRequestBody.put(Constants.SCHEDULE_BODY_NAME_VIEWSTATE, viewState);
                    OkHttpUtil.postAsync(newScheduleUrl, new OkHttpUtil.ResultCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            Log.i(TAG, "searchSchedule onError: --------------------");
                        }

                        @Override
                        public void onSuccess(byte[] response) {
                            String result = null;
                            try {
                                if (response != null) {
                                    result = new String(response, "gb2312");
                                    List<Map<String, String>> courseList = JsoupUtil.getCourseList(result);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("Course", (Serializable) courseList);
//                        startActivity(intent);
                                    new ScheduleDetailFragment().getCourseList(courseList);
//                        setResult(RESULT_OK,intent);
                                    LoginActivity.this.finish();
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }, scheduleRequestBody, requestHeadersMap);
                }
            }
        }, null, headers);


    }
}


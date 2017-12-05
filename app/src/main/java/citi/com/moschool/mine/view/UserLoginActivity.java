package citi.com.moschool.mine.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import citi.com.moschool.R;
import citi.com.moschool.mine.CustomDialog;
import citi.com.moschool.util.Constants;
import citi.com.moschool.util.OkHttpUtil;
import okhttp3.Call;

import static android.Manifest.permission.READ_CONTACTS;

public class UserLoginActivity extends AppCompatActivity implements OnClickListener {

    private final String TAG = "UserLoginActivity";
    public static  final int LOGIN_RESULT_CODE = 201;
    private final String REGISTER_URL = "http://zhangyanfu.com/web/register";
    private static final int REQUEST_READ_CONTACTS = 0;
    private String email;
    private CustomDialog dialog;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView mSignup;
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        dialog = new CustomDialog(this, R.style.CustomDialog);
        initView();
        mSignup.setOnClickListener(this);
    }

    private void initView() {
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mSignup = (TextView) findViewById(R.id.link_signup);
    }

    public void userLogin(View view) {
        dialog.show();
        String password = mPasswordView.getText().toString();
        email = mEmailView.getText().toString();
        if (password != null && !password.equals("") && email != null && !email.equals("")) {
            MyCallbcak calbcak = new MyCallbcak();
            Map<String, String> headers = new HashMap<>();
            Map<String, String> body = new HashMap<>();
            body.put("email", email);
            body.put("password", password);
            OkHttpUtil.postAsync(Constants.LOGIN_WEB, calbcak, body, headers);
        } else {
            dialog.dismiss();
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.link_signup:
//                startActivity();
                break;
        }
    }

    class MyCallbcak implements OkHttpUtil.ResultCallback {
        @Override
        public void onError(Call call, Exception e) {
            dialog.dismiss();
            Log.i(TAG, "onError: --");
            Toast.makeText(UserLoginActivity.this, "系统异常,请稍后重试!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(byte[] response) {
            dialog.dismiss();
            String res = new String(response);
            if(res!=null&&!res.equals(""))
            {
                Toast.makeText(getApplicationContext(), res.substring(0,4), Toast.LENGTH_SHORT).show();
                String flag = res.substring(0,4);
                String nickName;
                if (res != null && flag.equals("登录成功")) {
                    nickName = res.replace("登录成功","");
                    Log.i(TAG, "onSuccess: " + new String(response));
                    SharedPreferences.Editor editor = getSharedPreferences("data", Context.MODE_MULTI_PROCESS).edit();
                    if (email != null) {
                        Log.i(TAG, "onSuccess: email " + email);
                        editor.putString("email", email);
                        editor.putString("nickName", nickName);
                        editor.commit();
                    }
                    SharedPreferences pref = getSharedPreferences("data",  Context.MODE_MULTI_PROCESS);
                    String value = pref.getString("email", "");
                    Log.i(TAG, "onSuccess: get email value  " + value);
                    Intent intent = new Intent();
                    intent.putExtra("nickName",nickName);
                    UserLoginActivity.this.setResult(LOGIN_RESULT_CODE,intent);
                    UserLoginActivity.this.finish();
                }
          }
        }
    }
}


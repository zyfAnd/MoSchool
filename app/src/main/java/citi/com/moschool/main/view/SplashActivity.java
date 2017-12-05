package citi.com.moschool.main.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;


import citi.com.moschool.R;
import citi.com.moschool.service.AutoUpdateService;
import citi.com.moschool.util.OkHttpUtil;
import okhttp3.Call;

public class SplashActivity extends AppCompatActivity {

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT>=21)
        {
            View decorview =  getWindow().getDecorView();
            decorview.setSystemUiVisibility
                    (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
        final ProgressBar bar = (ProgressBar) findViewById(R.id.id_progressBar);
        bar.setVisibility(View.VISIBLE);
        // 闪屏的核心代码
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,
                        MainActivity.class); // 从启动动画ui跳转到主ui
                startActivity(intent);
//                overridePendingTransition(R.anim.in_from_right,
//                        R.anim.out_to_left);
                bar.setVisibility(View.INVISIBLE);
                SplashActivity.this.finish(); // 结束启动动画界面

            }
        }, 2000); // 启动动画持续2秒钟

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        imageView  = (ImageView) findViewById(R.id.id_bingPic);
        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(imageView);
        } else {
            loadBingPic();
        }
    }
    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        OkHttpUtil.getAsync(requestBingPic, new OkHttpUtil.ResultCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onSuccess(byte[] response) {
                final String bingPic =new String(response);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(SplashActivity.this).load(bingPic).into(imageView);
                    }
                });
            }
        });
    }
}

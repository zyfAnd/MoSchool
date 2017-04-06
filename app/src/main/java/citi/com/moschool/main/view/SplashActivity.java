package citi.com.moschool.main.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import citi.com.moschool.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
    }
}

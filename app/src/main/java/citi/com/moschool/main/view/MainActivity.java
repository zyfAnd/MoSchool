package citi.com.moschool.main.view;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

import citi.com.moschool.R;
import citi.com.moschool.found.FoundFragment;
import citi.com.moschool.home.HomeFragment;
import citi.com.moschool.main.presenter.MainPresenter;
import citi.com.moschool.main.presenter.MainPresenterImpl;
import citi.com.moschool.main.presenter.MainView;
import citi.com.moschool.mine.MineFragment;
import citi.com.moschool.schedule.view.ScheduleFragment;

public class MainActivity extends AppCompatActivity implements MainView{

    //课程表
    //失物招领
    //校园新闻
    //校园通知
    //网址导航
    public String TAG = "MainActivity";
    private MainPresenter mMainPresenter;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        setContentView(R.layout.activity_main);
        initView();
        mMainPresenter = new MainPresenterImpl(this);
        mRadioGroup.check(R.id.id_home_rbtn);
        mMainPresenter.switchNavigation(R.id.id_home_rbtn);
        setupNavigation();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.id_radio_groub);
    }

    /**
     * 切换菜单
     */
    private void setupNavigation()
    {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Log.i(TAG, "onCheckedChanged: "+checkedId);
                mMainPresenter.switchNavigation(checkedId);
            }
        });
    }

    @Override
    public void switchHome() {
        getSupportFragmentManager().beginTransaction().replace(R.id.id_frame_content,new HomeFragment()).commit();
    }

    @Override
    public void switchSchedule() {
        getSupportFragmentManager().beginTransaction().replace(R.id.id_frame_content,new ScheduleFragment()).commit();

    }

    @Override
    public void switchFound() {
       getSupportFragmentManager().beginTransaction().replace(R.id.id_frame_content,new FoundFragment()).commit();
    }

    @Override
    public void switchMine() {
       getSupportFragmentManager().beginTransaction().replace(R.id.id_frame_content,new MineFragment()).commit();
    }
}

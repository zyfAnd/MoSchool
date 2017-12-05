package citi.com.moschool.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import citi.com.moschool.R;
import citi.com.moschool.base.BaseFragment;
import citi.com.moschool.found.FoundFragment;
import citi.com.moschool.home.HomeFragment;
import citi.com.moschool.main.presenter.MainPresenter;
import citi.com.moschool.main.presenter.MainPresenterImpl;
import citi.com.moschool.main.presenter.MainView;
import citi.com.moschool.mine.MineFragment;
import citi.com.moschool.schedule.view.ScheduleFragment;

public class MainActivity extends FragmentActivity {

    //课程表
    //失物招领
    //校园新闻
    //校园通知
    //网址导航
    public String TAG = "MainActivity";
    private MainPresenter mMainPresenter;
    private RadioGroup mRadioGroup;
    private List<BaseFragment> mBaseFragments;
    private BaseFragment mFragment;//刚显示的Fragment
    private int position; //当前选中的位置


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化UI
        initView();
        //初始化数据
        initData();
        //设置监听
        setListener();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: activity.."+resultCode);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    private void initData()
    {
        mBaseFragments = new ArrayList<>();
        mBaseFragments.add(new HomeFragment());
        mBaseFragments.add(new ScheduleFragment());
        mBaseFragments.add(new FoundFragment());
        mBaseFragments.add(new MineFragment());
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.id_radio_groub);
    }

    private void setListener() {
        mRadioGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //默认选中第一个
        mRadioGroup.check(R.id.id_home_rbtn);
    }

    private void replaceFragment(BaseFragment lastFragment, BaseFragment currentFragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        //如果两个不相等,说明切换了Fragment
        if(lastFragment != currentFragment){
            mFragment = currentFragment;

            //隐藏刚显示的Fragment
            if(lastFragment != null){
                transaction.hide(lastFragment);
            }
            /**
             * 显示 或者 添加当前要显示的Fragment
             *
             * 如果当前要显示的Fragment没添加过 则 添加
             * 如果当前要显示的Fragment被添加过 则 隐藏
             */
            if(!currentFragment.isAdded()){
                if(currentFragment != null){
                    transaction.add(R.id.id_frame_content,currentFragment).commit();
                }
            }else {
                if (currentFragment != null){
                    transaction.show(currentFragment).commit();
                }
            }
        }
    }

    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.id_home_rbtn:
                    position = 0;
                    break;
                case R.id.id_schedule_rbtn:
                    position = 1;
                    break;
                case R.id.id_found_rbtn:
                    position = 2;
                    break;
                case R.id.id_mine_rbtn:
                    position = 3;
                    break;
                default:
                    position = 0;
                    break;
            }

            //根据位置得到对应的Fragment
            BaseFragment currentFragment = getFragment();

            //替换fragment
            //replaceFragment(currentFragment);

            replaceFragment(mFragment,currentFragment);
        }
    }

    /**
     * 根据返回到对应的Fragment
     * @return
     */
    private BaseFragment getFragment() {
        return mBaseFragments.get(position);
    }
}

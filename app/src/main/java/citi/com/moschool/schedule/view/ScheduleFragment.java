package citi.com.moschool.schedule.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import citi.com.moschool.R;
import citi.com.moschool.base.BaseFragment;
import citi.com.moschool.main.view.LoginActivity;


public class ScheduleFragment extends BaseFragment {
    private final String TAG = "ScheduleFragment";
    private ScheduleDetailFragment scheduleDetailFragment = new ScheduleDetailFragment();
    private View view;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;


    @Override
    protected View initView() {
        view = View.inflate(mContext,R.layout.fragment_schedule, null);
        mTabLayout = (TabLayout) view.findViewById(R.id.id_tablayout);
        mViewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: parent fragemnt"+resultCode);
    }

    @Override
    protected void initData() {
        super.initData();
        initTabLaout();
        setupViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initTabLaout() {
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.schedule));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.score));
    }
    private void setupViewPager()
    {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragment(scheduleDetailFragment,getString(R.string.schedule));
        myPagerAdapter.addFragment(new ScoreFragment(),getString(R.string.score));
        mViewPager.setAdapter(myPagerAdapter);
    }
    public static class MyPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();
        public void addFragment(Fragment fragment,String title)
        {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}

package citi.com.moschool.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import citi.com.moschool.R;
import citi.com.moschool.home.view.RecommendFragment;
import citi.com.moschool.home.view.RecruitFragment;

public class HomeFragment extends Fragment {
    private View view;
private TabLayout mTablayout;
    private ViewPager mViewPager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        initView();
        initTabLayout();
        setupViewPager();
        mTablayout.setupWithViewPager(mViewPager);
        return view;
    }
    private void initView()
    {
        mTablayout = (TabLayout) view.findViewById(R.id.id_tablayout);
        mViewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
    }
    private void initTabLayout()
    {
        mTablayout.addTab(mTablayout.newTab().setText(R.string.recommend));
        mTablayout.addTab(mTablayout.newTab().setText(R.string.recruit));
    }
    private void setupViewPager()
    {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragment(new RecommendFragment(),getString(R.string.recommend));
        myPagerAdapter.addFragment(new RecruitFragment(),getString(R.string.recruit));
        mViewPager.setAdapter(myPagerAdapter);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
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

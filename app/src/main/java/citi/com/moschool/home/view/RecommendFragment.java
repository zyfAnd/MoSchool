package citi.com.moschool.home.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import citi.com.moschool.R;
import citi.com.moschool.bean.ArticleBean;
import citi.com.moschool.home.RecruitAdapter;
import citi.com.moschool.home.presenter.RecruitPresenter;
import citi.com.moschool.home.presenter.RecruitPresenterImpl;
import citi.com.moschool.main.view.BannerActivity;
import citi.com.moschool.main.view.DetailActivity;
import citi.com.moschool.util.Constants;
import citi.com.moschool.util.GlideImageLoader;

public class RecommendFragment extends Fragment implements RecruitView ,SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = "RecommendFragment";
    private View view;
    private List<ArticleBean> mData;
    private RecruitAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecruitPresenter mRecruitPresenter;
    private LinearLayoutManager mLayoutManager;
//    String url = "http://139.199.210.131/mobile/recommend";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView: enter");
        if(view==null)
        {
            view = inflater.inflate(R.layout.fragment_recommend, null);
        }
        ViewGroup  parent = (ViewGroup) view.getParent();
        if(parent!=null)
        {
            parent.removeView(view);
        }

        initBanner();
        initViews();

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecruitPresenter = new RecruitPresenterImpl(getContext(),this, Constants.RECOMMEND_ARTICLE);
        mRecruitPresenter.loadArticles();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        mRecyclerView.setOnClickListener(mOnScrollListener);
        return view;
    }

    private void initViews() {
        Log.i(TAG, "initViews: enter");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        mSwipeRefreshLayout  = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
    }
    private void initBanner()
    {
        Banner banner = (Banner)view.findViewById(R.id.banner);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        //
        List<String> images = new ArrayList<>();
        images.add("http://139.199.210.131/article/ee85d8f8-ec88-4a4b-b447-0ab73596af2a.jpg");
        images.add("http://139.199.210.131/article/b3a05d05-43dd-404c-9aaf-f1d6f997ed18.jpg");
        images.add("http://139.199.210.131/article/eae6d1b8-e6b3-425c-912e-de60e3ee3f33.jpg");

        final List<String> details = new ArrayList<>();
        details.add("http://139.199.210.131/web/article/detail/30");
        details.add("http://139.199.210.131/web/article/detail/31");//http://139.199.210.131/web/article/detail/31
        details.add("http://139.199.210.131/web/article/detail/32");//http://139.199.210.131/web/article/detail/32
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        List<String> titles = new ArrayList<>();
        titles.add("连大风景之图书馆");
        titles.add("连大风景之伊甸园");
        titles.add("连大风景之人工湖");
        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(getActivity(), BannerActivity.class);
                intent.putExtra("bannerUrl",details.get(position));
                startActivity(intent);
            }
        });
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount() ) {
                //加载更多
                Snackbar.make(view, R.string.article_hit, Snackbar.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    public void onRefresh() {
        if(mData!=null)
        {
            mData.clear();
        }
//        Log.i(TAG, "onRefresh: -"+mData.size());
        mRecruitPresenter.loadArticles();
    }
    @Override
    public void addRecruits(List<ArticleBean> beans) {
        Log.i(TAG, "addRecruits: enter");
//        mRecruitPresenter.loadArticles();
        if(mData==null)
        {
            mData = new ArrayList<ArticleBean>();
        }
        mData.addAll(beans);
        mAdapter = new RecruitAdapter(getContext(),mData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecruitAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                ArticleBean bean = mData.get(position);
                intent.putExtra("ArticleBean",bean);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    @Override
    public void showProgress() {
        Log.i(TAG, "showProgress: enter");
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        Log.i(TAG, "hideProgress: enter");
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {
        Log.i(TAG, "showLoadFailMsg: enter");
        mSwipeRefreshLayout.setRefreshing(false);
    }
    @Override
    public void showErrorToast(String msg) {
        Snackbar.make(getActivity().findViewById(R.id.activity_main),msg,Snackbar.LENGTH_LONG).show();
    }
}

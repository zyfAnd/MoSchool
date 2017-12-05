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

import java.util.ArrayList;
import java.util.List;

import citi.com.moschool.R;
import citi.com.moschool.bean.ArticleBean;
import citi.com.moschool.home.RecruitAdapter;
import citi.com.moschool.home.presenter.RecruitPresenter;
import citi.com.moschool.home.presenter.RecruitPresenterImpl;
import citi.com.moschool.main.view.DetailActivity;
import citi.com.moschool.util.Constants;

public class RecruitFragment extends Fragment implements RecruitView ,SwipeRefreshLayout.OnRefreshListener{
    private final String TAG = "RecruitFragment";
    private View view;
    private List<ArticleBean> mData;
    private RecruitAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecruitPresenter mRecruitPresenter;
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        String url = "http://139.199.210.131/mobile/recruit";
        Log.i(TAG, "onCreateView: enter");
        view = inflater.inflate(R.layout.fragment_recruit, null);
        initViews();

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecruitPresenter = new RecruitPresenterImpl(getContext(),this, Constants.RECRUIT_ARTICLE);
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
        Log.i(TAG, "onRefresh: -"+mData.size());
        mRecruitPresenter.loadArticles();
    }

    @Override
    public void addRecruits(List<ArticleBean> beans) {
        Log.i(TAG, "addRecruits: enter");
//        mRecruitPresenter.loadArticles();
//        if(mData==null)
//        {
        mData = new ArrayList<ArticleBean>();
//        }
        if(mData==null)
        {
            Log.i(TAG, "addRecruits: null");
        }
        mData.addAll(beans);
        mAdapter = new RecruitAdapter(getContext(),mData);
        mRecyclerView.setAdapter(mAdapter);
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

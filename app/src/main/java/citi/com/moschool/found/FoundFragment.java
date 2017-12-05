package citi.com.moschool.found;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import citi.com.moschool.R;
import citi.com.moschool.adapter.ArtcileAdapter;
import citi.com.moschool.base.BaseFragment;
import citi.com.moschool.bean.ArticleBean;
import citi.com.moschool.home.RecruitAdapter;
import citi.com.moschool.home.presenter.RecruitPresenter;
import citi.com.moschool.home.presenter.RecruitPresenterImpl;
import citi.com.moschool.home.view.RecruitView;
import citi.com.moschool.main.view.DetailActivity;
import citi.com.moschool.mine.view.UserLoginActivity;
import citi.com.moschool.util.Constants;

import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.melnykov.fab.FloatingActionButton;
public class FoundFragment extends BaseFragment implements RecruitView,SwipeRefreshLayout.OnRefreshListener {
    private FloatingActionButton mFloatingActionButton;
    private TabLayout mTabLayout;
    private final String TAG = "FoundFragment";
    private View view;
    private List<ArticleBean> mData;
    private ArtcileAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecruitPresenter mRecruitPresenter;
    private LinearLayoutManager mLayoutManager;
//    String url = "http://139.199.210.131/mobile/find";


    @Override
    protected View initView() {
        Log.i(TAG, "initViews: enter");
        view = View.inflate(mContext,R.layout.fragment_found, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        mSwipeRefreshLayout  = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.found_fab);
        mTabLayout = (TabLayout) view.findViewById(R.id.id_found_tablayout);
        mTabLayout.addTab(mTabLayout.newTab().setText("发现"));
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                String email = pref.getString("email", "");
                Log.i(TAG, "onClick: enter "+email);
                if(email!=null&&!email.equals(""))
                {
                    Log.i(TAG, "onClick: email != null "+email);
                    Intent intent = new Intent(getActivity(),PostActivity.class);
                    startActivityForResult(intent,66);
                }else{
                    Toast.makeText(getContext(),"请您先登录",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),UserLoginActivity.class);
                    startActivityForResult(intent,77);
                }

            }
        });
        return view;
    }
    @Override
    protected void initData() {
        super.initData();

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecruitPresenter = new RecruitPresenterImpl(getContext(),this,Constants.FOUND_ARTICLE);
        mRecruitPresenter.loadArticles();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void postArticle(View view)
    {
        Intent intent = new Intent(getActivity(),PostActivity.class);
        getActivity().startActivityForResult(intent,getActivity().RESULT_FIRST_USER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: -- refresh ....");
        onRefresh();
    }

    @Override
    public void onRefresh() {
        if(mData!=null)
        {
            mData.clear();
        }
        if(mData!=null)
        {
            Log.i(TAG, "onRefresh: -"+mData.size());
        }

        mRecruitPresenter.loadArticles();
//        mAdapter = new RecruitAdapter(getContext(),mData);
//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addRecruits(List<ArticleBean> beans) {
        Log.i(TAG, "addRecruits: enter");
//        mRecruitPresenter.loadArticles();
//        mRecruitPresenter.loadArticles();
        Log.i(TAG, "addRecruits: beans size "+beans.toString());
        if(mData==null)
        {
            mData = new ArrayList<ArticleBean>();
        }
        mData.addAll(beans);
        mAdapter = new ArtcileAdapter(getContext(),mData);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecruitAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(getActivity(), DetailActivity.class);
//                intent.putExtra("content",mData.get(position).getContent());
//                intent.putExtra("title",mData.get(position).getTitle());
//                startActivity(intent);
                String imageUrl = Constants.HOST+mData.get(position).getImageUrl();
                Log.i(TAG, "onItemClick: imgaeUrl "+imageUrl);
                new PhotoPagerConfig.Builder(getActivity())
                        .addSingleBigImageUrl(imageUrl) //图片url,可以是sd卡res，asset，网络图片.
                        .setSavaImage(true)                         //开启保存图片，默认false
                        .setPosition(1).build();                             //默认展示第2张图片
                                                //这里是你想保存大图片到手机的地址，不传会有默认地址

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

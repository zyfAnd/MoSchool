package citi.com.moschool.main.presenter;

import android.util.Log;
import android.view.View;

import citi.com.moschool.R;

/**
 * Created by zhang on 2017/3/9.
 */

public class MainPresenterImpl implements MainPresenter {
    private MainView mMainView;
    private String TAG = "MainPresenterImpl";

    public MainPresenterImpl(MainView mMainView) {
        this.mMainView = mMainView;
    }

    @Override
    public void switchNavigation(int id) {
        switch (id) {
            case R.id.id_home_rbtn:
                Log.i(TAG, "switchNavigation: id_home_rbtn");
                mMainView.switchHome();
                break;
            case R.id.id_schedule_rbtn:
                Log.i(TAG, "switchNavigation: id_schedule_rbtn");
                mMainView.switchSchedule();
                break;
            case R.id.id_found_rbtn:
                Log.i(TAG, "switchNavigation: id_found_rbtn");
                mMainView.switchFound();
                break;
            case R.id.id_mine_rbtn:
                Log.i(TAG, "switchNavigation: id_mine_rbtn");
                mMainView.switchMine();
                break;
        }
    }

}

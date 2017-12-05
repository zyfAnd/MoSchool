package citi.com.moschool.mine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import citi.com.moschool.R;
import citi.com.moschool.base.BaseFragment;
import citi.com.moschool.mine.view.UserLoginActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends BaseFragment implements View.OnClickListener{
    private final String TAG = "MineFragment";
    private View view;
    private CircleImageView mImageView;
    public static final int REQ_CODE = 0x11;
    private RelativeLayout mRelativeLayout;
    private LinearLayout mLinearLayout;
    private RelativeLayout mRelativeLayoutLink;
    private TextView tvUser;
    SharedPreferences pref;
    String nickName;

    @Override
    protected View initView() {
        view = View.inflate(mContext,R.layout.fragment_mine, null);
        mImageView = (CircleImageView) view.findViewById(R.id.id_mine_imageview);
        tvUser = (TextView) view.findViewById(R.id.login_user);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.id_clearCache);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.id_mine_login);
        mRelativeLayoutLink = (RelativeLayout) view.findViewById(R.id.id_link);
        pref = getActivity().getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        nickName = pref.getString( "nickName","");
        if(nickName!=null&&!nickName.equals(""))
        {
            tvUser.setText(nickName);
        }

        return view;
    }
    @Override
    protected void initData() {
        super.initData();
        mImageView.setOnClickListener(this);
        mRelativeLayout.setOnClickListener(this);
        mLinearLayout.setOnClickListener(this);
        mRelativeLayoutLink.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.id_mine_imageview:
                if(nickName==null||nickName.equals(""))
                {
                    Intent intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivityForResult(intent,REQ_CODE);
                }else{

                }
                break;
            case R.id.id_clearCache:
                SharedPreferences data = getActivity().getSharedPreferences("data",Context.MODE_MULTI_PROCESS);
                data.edit().clear().commit();
                Toast.makeText(getContext(),"清除缓存成功",Toast.LENGTH_SHORT).show();
                String email = data.getString("email", "");
                Log.i(TAG, "onClick: "+email);
                break;
            case R.id.id_link:
                startActivity(new Intent(getActivity(),AboutActivity.class));
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "mineFragment: "+resultCode);
        if(resultCode==UserLoginActivity.LOGIN_RESULT_CODE)
        {
            if (data!=null)
            {
                String nickName = data.getStringExtra("nickName");
                tvUser.setText(nickName);
            }
        }
    }
}

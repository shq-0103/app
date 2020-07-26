package com.shq.movies.ui.fragment;

import android.widget.ImageView;

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.SettingBar;
import com.shq.movies.R;
import com.shq.movies.common.MyFragment;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.UserApi;
import com.shq.movies.http.response.UserInfoBean;
import com.shq.movies.ui.activity.CopyActivity;

import org.greenrobot.eventbus.EventBus;

public final class UserInfoFragment extends MyFragment<CopyActivity> {

    private SettingBar sb_username;
    private SettingBar sb_nickname;
    private ImageView mAvatarView;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initView() {
        sb_nickname=findViewById(R.id.sb_nickname);
        sb_username=findViewById(R.id.sb_username);

        mAvatarView=findViewById(R.id.iv_person_data_avatar);
    }

    @Override
    protected void initData() {
        GlideApp.with(getActivity())
                .load(R.drawable.avatar_placeholder_ic)
                .placeholder(R.drawable.avatar_placeholder_ic)
                .error(R.drawable.avatar_placeholder_ic)
                .circleCrop()
                .into(mAvatarView);
        EasyHttp.get(this).api(new UserApi()).request(new HttpCallback<HttpData<UserInfoBean>>(this){
            @Override
            public void onSucceed(HttpData<UserInfoBean> result) {
                super.onSucceed(result);
                sb_nickname.setRightText(result.getData().getNickname());
                sb_username.setRightText(result.getData().getUsername());

            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
                // 跳转到登录页
                EventBus.getDefault().post(getString(R.string.event_login_fail));
            }
        });
    }
}
package com.shq.movies.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.SettingBar;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.UpdateImageApi;
import com.shq.movies.http.request.UserApi;
import com.shq.movies.http.response.UserInfoBean;
import com.shq.movies.ui.dialog.InputDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

public final class EditActivity extends MyActivity {

    private ViewGroup mAvatarLayout;
    private ImageView mAvatarView;
    private String mAvatarUrl;
    private SettingBar mNameView;
    private SettingBar sb_setting_email;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_userinfo;
    }

    @Override
    protected void initView() {
        mAvatarLayout = findViewById(R.id.fl_person_data_avatar);
        mAvatarView = findViewById(R.id.iv_person_data_avatar);
        mNameView = findViewById(R.id.sb_nickname);
        sb_setting_email = findViewById(R.id.sb_setting_email);
        setOnClickListener(mAvatarLayout, mAvatarView,mNameView,sb_setting_email);
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
                mNameView.setText(result.getData().getNickname());
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
                // 跳转到登录页
                EventBus.getDefault().post(getString(R.string.event_login_fail));
            }
        });

    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sb_setting_email:
                startActivity(PasswordResetActivity.class);
                break;
        }
        if (v == mAvatarLayout) {
            ImageSelectActivity.start(this, data -> {

                if (true) {
                    mAvatarUrl = data.get(0);
                    GlideApp.with(getActivity())
                            .load(mAvatarUrl)
                            .into(mAvatarView);
                    return;
                }
                // 上传头像
                EasyHttp.post(this)
                        .api(new UpdateImageApi()
                                .setImage(new File(data.get(0))))
                        .request(new HttpCallback<HttpData<String>>(EditActivity.this) {

                            @Override
                            public void onSucceed(HttpData<String> data) {
                                mAvatarUrl = data.getData();
                                GlideApp.with(getActivity())
                                        .load(mAvatarUrl)
                                        .into(mAvatarView);
                            }
                        });
            });
        } else if (v == mAvatarView) {
            if (!TextUtils.isEmpty(mAvatarUrl)) {
                // 查看头像
                ImagePreviewActivity.start(getActivity(), mAvatarUrl);
            } else {
                // 选择头像
                onClick(mAvatarLayout);
            }
        } else if (v == mNameView) {
            new InputDialog.Builder(this)
                    // 标题可以不用填写
                    .setTitle(getString(R.string.personal_data_name_hint))
                    .setContent(mNameView.getRightText())
                    //.setHint(getString(R.string.personal_data_name_hint))
                    //.setConfirm("确定")
                    // 设置 null 表示不显示取消按钮
                    //.setCancel("取消")
                    // 设置点击按钮后不关闭对话框
                    //.setAutoDismiss(false)
                    .setListener((dialog, content) -> {
                        if (!mNameView.getRightText().equals(content)) {
                            mNameView.setRightText(content);
                        }
                    })
                    .show();
        }

    }
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}
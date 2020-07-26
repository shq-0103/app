package com.shq.movies.ui.fragment;

import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.SettingBar;
import com.shq.movies.R;
import com.shq.movies.common.MyFragment;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.UserApi;
import com.shq.movies.http.response.UserInfoBean;
import com.shq.movies.ui.activity.EditActivity;
import com.shq.movies.ui.activity.HomeActivity;
import com.shq.movies.ui.activity.PasswordResetActivity;
import com.shq.movies.ui.dialog.MessageDialog;

import org.greenrobot.eventbus.EventBus;

public final class MineFragment extends MyFragment<HomeActivity> implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView tv_nickname;

    private SettingBar sb_sign_out;
    private SettingBar sb_modify_userinfo;

    private ImageView iv_avatar;
    private BottomNavigationView bv_user_info;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        tv_nickname = findViewById(R.id.tv_nickname);
        sb_sign_out = findViewById(R.id.sb_setting_exit);
        sb_modify_userinfo = findViewById(R.id.sb_modify);
        iv_avatar = findViewById(R.id.iv_avatar);
        bv_user_info = findViewById(R.id.bv_user_info_navigation);
        // 不使用图标默认变色
        bv_user_info.setItemIconTintList(null);
        bv_user_info.setOnNavigationItemSelectedListener(this);

        setOnClickListener(sb_sign_out, sb_modify_userinfo);
    }

    @Override
    protected void initData() {
        GlideApp.with(getActivity())
                .load(R.drawable.avatar_placeholder_ic)
                .placeholder(R.drawable.avatar_placeholder_ic)
                .error(R.drawable.avatar_placeholder_ic)
                .circleCrop()
                .into(iv_avatar);
        EasyHttp.get(this).api(new UserApi()).request(new HttpCallback<HttpData<UserInfoBean>>(this) {
            @Override
            public void onSucceed(HttpData<UserInfoBean> result) {
                super.onSucceed(result);
                tv_nickname.setText(result.getData().getNickname());
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
                // 跳转到登录页
                EventBus.getDefault().post(getString(R.string.event_login_fail));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        toast(item.getTitle());
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sb_setting_exit:
                // 消息对话框
                new MessageDialog.Builder(getContext())
                        // 内容必须要填写
                        .setMessage("Confirm logout?")
                        // 确定按钮文本
                        .setConfirm("Confirm")
                        // 设置 null 表示不显示取消按钮
                        .setCancel("Cancel")
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setListener(new MessageDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                toast("Confirm");
                                //步骤1：创建一个SharedPreferences对象
                                SharedPreferences sharedPreferences = getAttachActivity().getSharedPreferences("data", getContext().MODE_PRIVATE);
                                //步骤2： 实例化SharedPreferences.Editor对象
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove(getString(R.string.user_token));
                                editor.commit();
                                // 跳转到登录页
                                EventBus.getDefault().post(getString(R.string.event_login_fail));
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("Cancel");
                            }
                        })
                        .show();

                break;
            case R.id.sb_modify:
                startActivity(EditActivity.class);
                break;

            default:
                break;
        }
    }

    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}
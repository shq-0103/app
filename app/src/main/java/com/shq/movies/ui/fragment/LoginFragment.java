package com.shq.movies.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.shq.movies.R;
import com.shq.movies.aop.SingleClick;
import com.shq.movies.common.MyFragment;
import com.shq.movies.event.StringEvent;
import com.shq.movies.helper.InputTextHelper;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.LoginApi;
import com.shq.movies.http.response.LoginBean;
import com.shq.movies.other.IntentKey;
import com.shq.movies.ui.activity.HomeActivity;
import com.shq.movies.ui.activity.RegisterActivity;

import org.greenrobot.eventbus.EventBus;


public class LoginFragment extends MyFragment<HomeActivity> {


    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button mCommitView;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
        mUsernameView = findViewById(R.id.et_login_username);
        mPasswordView = findViewById(R.id.et_login_password);
        mCommitView = findViewById(R.id.btn_login_commit);
        setOnClickListener(R.id.btn_login_commit);

        InputTextHelper.with(this.getActivity())
                .addView(mUsernameView)
                .addView(mPasswordView)
                .setMain(mCommitView)
                .build();
    }

    @Override
    protected void initData() {

    }

    @SingleClick
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login_commit) {

            EasyHttp.post(this)
                    .api(new LoginApi()
                            .setUsername(mUsernameView.getText().toString())
                            .setPassword(mPasswordView.getText().toString()))
                    .request(new HttpCallback<HttpData<LoginBean>>(this) {

                        @Override
                        public void onSucceed(HttpData<LoginBean> data) {
                            ToastUtils.show("Login success");
                            //步骤1：创建一个SharedPreferences对象
                            SharedPreferences sharedPreferences =getAttachActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                            //步骤2： 实例化SharedPreferences.Editor对象
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //步骤3：将获取过来的值放入文件
                            editor.putString(getString(R.string.user_token), data.getData().getToken());
                            //步骤4：提交
                            editor.commit();
                            mUsernameView.setText(null);
                            mPasswordView.setText(null);


                            // 更新 Token
                            EasyConfig.getInstance()
                                    .addHeader("Authorization", "Bearer "+data.getData().getToken());
                            // 跳转到主页
                            EventBus.getDefault().post(getString(R.string.event_login_success));
                        }
                    });
        }
    }

    @Override
    public void onRightClick(View v) {
        // 跳转到注册界面
        startActivity(RegisterActivity.class);
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}
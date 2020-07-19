package com.shq.movies.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.gyf.immersionbar.ImmersionBar;
import com.shq.movies.R;
import com.shq.movies.aop.SingleClick;
import com.shq.movies.common.MyActivity;
import com.shq.movies.helper.InputTextHelper;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.GetCodeApi;
import com.shq.movies.http.request.RegisterApi;
import com.shq.movies.http.response.RegisterBean;
import com.shq.movies.other.IntentKey;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CountdownView;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 注册界面
 */
public final class RegisterActivity extends MyActivity {

    private EditText mUsernameView;

    private EditText mPasswordView1;
    private EditText mPasswordView2;

    private Button mCommitView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {

        mUsernameView = findViewById(R.id.et_username);
        mPasswordView1 = findViewById(R.id.et_register_password1);
        mPasswordView2 = findViewById(R.id.et_register_password2);
        mCommitView = findViewById(R.id.btn_register_commit);
        setOnClickListener(mCommitView);

        // 给这个 View 设置沉浸式，避免状态栏遮挡
        ImmersionBar.setTitleBar(this, findViewById(R.id.tv_register_title));

        InputTextHelper.with(this)
                .addView(mUsernameView)
                .addView(mPasswordView1)
                .addView(mPasswordView2)
                .setMain(mCommitView)
                .build();
    }

    @Override
    protected void initData() {

    }

    @SingleClick
    @Override
    public void onClick(View v) {


        if (v == mCommitView) {


            if (!mPasswordView1.getText().toString().equals(mPasswordView2.getText().toString())) {
                toast(R.string.common_password_input_unlike);
                return;
            }


            // 提交注册
            EasyHttp.post(this)
                    .api(new RegisterApi()
                            .setPhone(mUsernameView.getText().toString())
                            .setPassword(mPasswordView1.getText().toString()))
                    .request(new HttpCallback<HttpData<Boolean>>(this) {

                        @Override
                        public void onSucceed(HttpData<Boolean> data) {
                            toast(R.string.register_succeed);
//                            setResult(RESULT_OK, new Intent()
//                                    .putExtra(IntentKey.PHONE, mUsernameView.getText().toString())
//                                    .putExtra(IntentKey.PASSWORD, mPasswordView1.getText().toString()));
                            finish();
                        }
                    });
        }
    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 不要把整个布局顶上去
                .keyboardEnable(true);
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }
}
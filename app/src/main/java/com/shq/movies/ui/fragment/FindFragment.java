package com.shq.movies.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shq.movies.R;
import com.shq.movies.aop.SingleClick;
import com.shq.movies.common.MyFragment;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.ui.activity.HomeActivity;
import com.hjq.widget.view.CountdownView;
import com.hjq.widget.view.SwitchButton;
import com.shq.movies.ui.activity.MovieListActivity;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 项目自定义控件展示
 */
public final class FindFragment extends MyFragment<HomeActivity>
        implements SwitchButton.OnCheckedChangeListener {

    private ImageView mCircleView;
    private SwitchButton mSwitchButton;
    private CountdownView mCountdownView;
    private TextView find_new_text;

    public static FindFragment newInstance() {
        return new FindFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.find_fragment;
    }

    @Override
    protected void initView() {
        mCircleView = findViewById(R.id.iv_find_circle);
        mSwitchButton = findViewById(R.id.sb_find_switch);
        mCountdownView = findViewById(R.id.cv_find_countdown);
        find_new_text = findViewById(R.id.find_new_text);
        setOnClickListener(mCountdownView,find_new_text);

        mSwitchButton.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {
        GlideApp.with(this)
                .load(R.drawable.example_bg)
                .circleCrop()
                .into(mCircleView);
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        if (v == mCountdownView) {
            toast(R.string.common_code_send_hint);
            mCountdownView.start();
        }
        switch (v.getId()) {
            case R.id.find_new_text:
                startActivity(MovieListActivity.class);
                break;
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    /**
     * {@link SwitchButton.OnCheckedChangeListener}
     */

    @Override
    public void onCheckedChanged(SwitchButton button, boolean isChecked) {
        toast(isChecked);
    }
}
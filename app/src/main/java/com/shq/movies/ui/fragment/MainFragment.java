package com.shq.movies.ui.fragment;

import com.shq.movies.R;
import com.shq.movies.common.MyFragment;
import com.shq.movies.ui.activity.CopyActivity;
import com.shq.movies.ui.activity.HomeActivity;

public final class MainFragment extends MyFragment<HomeActivity> {

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}
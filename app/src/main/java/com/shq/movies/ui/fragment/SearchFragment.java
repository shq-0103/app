package com.shq.movies.ui.fragment;


import com.shq.movies.R;
import com.shq.movies.common.MyFragment;

import com.shq.movies.ui.activity.HomeActivity;


public final class SearchFragment extends MyFragment<HomeActivity> {



    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}
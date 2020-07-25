package com.shq.movies.ui.fragment;

import com.shq.movies.R;
import com.shq.movies.common.MyFragment;
import com.shq.movies.ui.activity.CopyActivity;
import com.shq.movies.ui.activity.HomeActivity;


public final class CommunityFragment extends MyFragment<HomeActivity> {

    public static CopyFragment newInstance() {
        return new CopyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.copy_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
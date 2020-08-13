package com.shq.movies.ui.fragment;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hjq.base.BaseFragmentAdapter;
import com.shq.movies.R;
import com.shq.movies.common.MyFragment;
import com.shq.movies.ui.activity.CopyActivity;
import com.shq.movies.ui.activity.HomeActivity;
import com.shq.movies.ui.adapter.MovieAdapter;
import com.shq.movies.ui.adapter.MovieReviewAdapter;

public final class MovieReviewFragment extends MyFragment<HomeActivity>  {

    private TabLayout tb_review;
    private ViewPager vg_movielist;
    private BaseFragmentAdapter<MyFragment> mPagerAdapter;

    public static MovieReviewFragment newInstance() {
        return new MovieReviewFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_moviereview;}

    @Override
    protected void initView() {

        tb_review = findViewById(R.id.tb_review);
        vg_movielist = findViewById(R.id.vg_movielist);
        mPagerAdapter = new BaseFragmentAdapter<>(this);
        mPagerAdapter.addFragment(PopularReviewFragment.newInstance(), "Popular");
        mPagerAdapter.addFragment(ReviewListFragment.newInstance(), "New");
        vg_movielist.setAdapter(mPagerAdapter);
        tb_review.setupWithViewPager(vg_movielist);
    }

    @Override
    protected void initData() {

    }
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}
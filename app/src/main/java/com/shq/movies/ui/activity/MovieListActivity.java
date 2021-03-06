package com.shq.movies.ui.activity;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hjq.base.BaseFragmentAdapter;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.common.MyFragment;
import com.shq.movies.ui.fragment.HighScoreFragment;
import com.shq.movies.ui.fragment.MovieListFragment;


public final class MovieListActivity extends MyActivity {

    private TabLayout tb_movielist;
    private ViewPager pg_movielist;
    private BaseFragmentAdapter<MyFragment> mPagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_movielist;
    }

    @Override
    protected void initView() {
        tb_movielist = findViewById(R.id.tb_movielist);
        pg_movielist = findViewById(R.id.pg_movielist);

        mPagerAdapter = new BaseFragmentAdapter<>(this);

        mPagerAdapter.addFragment(MovieListFragment.newInstance(), "Most popular");
        mPagerAdapter.addFragment(HighScoreFragment.newInstance(), "Top rated");
        mPagerAdapter.addFragment(MovieListFragment.newInstance(), "Week Popularity");
        pg_movielist.setAdapter(mPagerAdapter);
        tb_movielist.setupWithViewPager(pg_movielist);
    }

    @Override
    protected void initData() {

    }
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}
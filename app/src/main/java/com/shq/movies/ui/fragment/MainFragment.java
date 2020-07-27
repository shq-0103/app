package com.shq.movies.ui.fragment;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.rd.PageIndicatorView;
import com.shq.movies.R;
import com.shq.movies.common.MyFragment;
import com.shq.movies.ui.activity.HomeActivity;
import com.shq.movies.ui.adapter.MainTopImgAdapter;



public final class MainFragment extends MyFragment<HomeActivity>
        implements ViewPager.OnPageChangeListener {
    private ViewPager vp_main_pager;
    private PageIndicatorView pv_main_indicator;
    private MainTopImgAdapter mPagerAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        vp_main_pager= findViewById(R.id.vp_main_pager);
        pv_main_indicator = findViewById(R.id.pv_main_indicator);
        setOnClickListener(pv_main_indicator);

        pv_main_indicator.setViewPager(vp_main_pager);

    }

    @Override
    protected void initData() {
        mPagerAdapter = new MainTopImgAdapter();
        vp_main_pager.setAdapter(mPagerAdapter);
        vp_main_pager.addOnPageChangeListener(this);
    }
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public boolean isSwipeEnable() {
        return false;
    }
}
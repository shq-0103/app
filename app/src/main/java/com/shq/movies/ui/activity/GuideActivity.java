package com.shq.movies.ui.activity;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.shq.movies.R;
import com.shq.movies.aop.SingleClick;
import com.shq.movies.common.MyActivity;
import com.shq.movies.ui.pager.GuidePagerAdapter;
import com.rd.PageIndicatorView;


public final class GuideActivity extends MyActivity
        implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private PageIndicatorView mIndicatorView;
    private View mCompleteView;

    private GuidePagerAdapter mPagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.guide_activity;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.vp_guide_pager);
        mIndicatorView = findViewById(R.id.pv_guide_indicator);
        mCompleteView = findViewById(R.id.btn_guide_complete);
        setOnClickListener(mCompleteView);

        mIndicatorView.setViewPager(mViewPager);
    }

    @Override
    protected void initData() {
        mPagerAdapter = new GuidePagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        if (v == mCompleteView) {
            startActivity(HomeActivity.class);
            finish();
        }
    }

    /**
     * {@link ViewPager.OnPageChangeListener}
     */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mViewPager.getCurrentItem() == mPagerAdapter.getCount() - 1 && positionOffsetPixels > 0) {
            mCompleteView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            mCompleteView.setVisibility(mViewPager.getCurrentItem() == mPagerAdapter.getCount() - 1 ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }
}
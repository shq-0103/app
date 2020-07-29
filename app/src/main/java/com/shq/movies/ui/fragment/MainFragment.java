package com.shq.movies.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.rd.PageIndicatorView;
import com.shq.movies.R;
import com.shq.movies.common.MyFragment;
import com.shq.movies.ui.activity.HomeActivity;
import com.shq.movies.ui.activity.QueryMovieActivity;
import com.shq.movies.ui.adapter.MainTopImgAdapter;



public final class MainFragment extends MyFragment<HomeActivity>
        implements ViewPager.OnPageChangeListener {
    private ViewPager vp_main_pager;
    private PageIndicatorView pv_main_indicator;
    private MainTopImgAdapter mPagerAdapter;
 private EditText et_search;
 private ImageView iv_search;

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
        et_search = findViewById(R.id.et_search);
        iv_search=findViewById(R.id.iv_home_search);
        setOnClickListener(pv_main_indicator,iv_search);
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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_home_search){
            if(!et_search.getText().toString().trim().isEmpty()){
                // 跳转到搜索 Activity 并传参
                Intent intent =new Intent( getAttachActivity().getContext(), QueryMovieActivity.class);
                intent.putExtra("keyword", et_search.getText().toString().trim());
                startActivity(intent);
            }
        }
    }

    public boolean isSwipeEnable() {
        return false;
    }
}
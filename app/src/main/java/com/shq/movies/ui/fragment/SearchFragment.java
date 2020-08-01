package com.shq.movies.ui.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.shq.movies.R;
import com.shq.movies.common.MyFragment;

import com.shq.movies.ui.activity.HomeActivity;
import com.shq.movies.ui.activity.QueryMovieActivity;


public final class SearchFragment extends MyFragment<HomeActivity> implements ViewPager.OnPageChangeListener{

    private EditText et_search;
    private ImageView iv_search;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initView() {
        et_search = findViewById(R.id.et_search);
        iv_search=findViewById(R.id.iv_home_search);
        setOnClickListener(iv_search);

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
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
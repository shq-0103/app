package com.shq.movies.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.rd.PageIndicatorView;
import com.shq.movies.R;
import com.shq.movies.common.MyFragment;
import com.shq.movies.ui.activity.HomeActivity;
import com.shq.movies.ui.activity.QueryMovieActivity;
import com.shq.movies.ui.adapter.MainTopImgAdapter;

import java.util.Timer;
import java.util.TimerTask;


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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //获得当前的位置
                int currentItem = vp_main_pager.getCurrentItem();

                //跳转到指定位置
                if(currentItem+1==vp_main_pager.getAdapter().getCount()){
                    vp_main_pager.setCurrentItem(0);
                }else {
                    vp_main_pager.setCurrentItem(currentItem + 1);

                }
            }
        }
    };

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
        //定时器 每两秒执行一次
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 0, 2000);
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
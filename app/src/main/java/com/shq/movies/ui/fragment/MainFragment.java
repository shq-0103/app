package com.shq.movies.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;
import com.rd.PageIndicatorView;
import com.shq.movies.R;
import com.shq.movies.common.MyFragment;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.QueryMovieApi;
import com.shq.movies.http.request.ReviewApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.http.response.ReviewBean;
import com.shq.movies.ui.activity.HomeActivity;
import com.shq.movies.ui.activity.MovieDetailActivity;
import com.shq.movies.ui.activity.QueryMovieActivity;
import com.shq.movies.ui.adapter.ListAdapter;
import com.shq.movies.ui.adapter.MainReviewAdapter;
import com.shq.movies.ui.adapter.MainTopImgAdapter;
import com.shq.movies.ui.adapter.MovieAdapter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public final class MainFragment extends MyFragment<HomeActivity>
        implements ViewPager.OnPageChangeListener, BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {
    private ViewPager vp_main_pager;
    private PageIndicatorView pv_main_indicator;
    private MainTopImgAdapter mPagerAdapter;
    private EditText et_search;
    private ImageView iv_search;

    private WrapRecyclerView rv_recom_movie_list;
    private WrapRecyclerView rv_recom_review_list;

    private ListAdapter listAdapter;
    private MainReviewAdapter reviewAdapter;

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
                if (currentItem + 1 == vp_main_pager.getAdapter().getCount()) {
                    vp_main_pager.setCurrentItem(0);
                } else {
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
        vp_main_pager = findViewById(R.id.vp_main_pager);
        pv_main_indicator = findViewById(R.id.pv_main_indicator);
        et_search = findViewById(R.id.et_search);
        iv_search = findViewById(R.id.iv_home_search);
        setOnClickListener(pv_main_indicator, iv_search);
        pv_main_indicator.setViewPager(vp_main_pager);
        //定时器 每两秒执行一次
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 0, 2500);

        rv_recom_movie_list = findViewById(R.id.rv_recom_movie_list);
        listAdapter = new ListAdapter(getAttachActivity());
        rv_recom_movie_list.setLayoutManager(new LinearLayoutManager(getAttachActivity(), WrapRecyclerView.HORIZONTAL, false));
        listAdapter.setOnItemClickListener(this);
        listAdapter.setOnChildClickListener(R.id.iv_cover,this);
        rv_recom_movie_list.setAdapter(listAdapter);

        rv_recom_review_list = findViewById(R.id.rv_recom_review_list);
        reviewAdapter = new MainReviewAdapter(getAttachActivity());
        reviewAdapter.setOnItemClickListener(this);
        rv_recom_review_list.setAdapter(reviewAdapter);
    }

    @Override
    protected void initData() {
        mPagerAdapter = new MainTopImgAdapter();
        vp_main_pager.setAdapter(mPagerAdapter);
        vp_main_pager.addOnPageChangeListener(this);

        this.getData();

    }

    private void getData() {

        EasyHttp.get(this).api((IRequestApi) new QueryMovieApi().setName(null).setPage(listAdapter.getPageNumber()).setPageSize(10)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                listAdapter.setData(result.getData());
            }
        });

        EasyHttp.get(this).api((IRequestApi) new ReviewApi().setPage(reviewAdapter.getPageNumber()).setPageSize(10)).request(new HttpCallback<HttpData<List<ReviewBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<ReviewBean>> result) {
                super.onSucceed(result);
                reviewAdapter.setData(result.getData());
            }
        });
    }

    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
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

    @Override
    public void onResume() {
        super.onResume();
        this.getData();
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {

        this.routerToDetail(String.valueOf(listAdapter.getItem(position).getId()));
    }

    public void  routerToDetail(String movieId){
        Intent intent = new Intent(getAttachActivity().getContext(), MovieDetailActivity.class);
        intent.putExtra("movieId", movieId);
        startActivity(intent);
    }

    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {
        toast("child");
        if(childView.getId()==R.id.iv_cover){
            this.routerToDetail(String.valueOf(listAdapter.getItem(position).getId()));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_home_search) {
            if (!et_search.getText().toString().trim().isEmpty()) {
                // 跳转到搜索 Activity 并传参
                Intent intent = new Intent(getAttachActivity().getContext(), QueryMovieActivity.class);
                intent.putExtra("keyword", et_search.getText().toString().trim());
                startActivity(intent);
            }
        }
    }

    public boolean isSwipeEnable() {
        return false;
    }
}
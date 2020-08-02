package com.shq.movies.ui.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.SettingBar;
import com.hjq.widget.layout.WrapRecyclerView;
import com.shq.movies.R;
import com.shq.movies.common.MyFragment;

import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.QueryMovieApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.ui.activity.HomeActivity;
import com.shq.movies.ui.activity.MovieListActivity;
import com.shq.movies.ui.activity.QueryMovieActivity;
import com.shq.movies.ui.adapter.FindAdapter;
import com.shq.movies.ui.adapter.MovieAdapter;

import java.util.List;


public final class SearchFragment extends MyFragment<HomeActivity> implements
        ViewPager.OnPageChangeListener, BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {

    private EditText et_search;
    private ImageView iv_search;
    private WrapRecyclerView rv_score;
    private WrapRecyclerView rv_popular;
    private WrapRecyclerView rv_week;
    private FindAdapter SfindAdapter;
    private FindAdapter PfindAdapter;
    private FindAdapter WfindAdapter;
    private LinearLayout upcoming;
    private LinearLayout month;
    private SettingBar sb_popular;

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
        iv_search = findViewById(R.id.iv_home_search);
        upcoming = findViewById(R.id.upcoming);
        month = findViewById(R.id.month);
        sb_popular = findViewById(R.id.sb_popular);
        setOnClickListener(iv_search, upcoming);


        rv_score = findViewById(R.id.rv_score);
        SfindAdapter = new FindAdapter(getAttachActivity());
        SfindAdapter.setOnItemClickListener(this);
        rv_score.setAdapter(SfindAdapter);


        rv_popular = findViewById(R.id.rv_popular);
        PfindAdapter = new FindAdapter(getAttachActivity());
        PfindAdapter.setOnItemClickListener(this);
        rv_popular.setAdapter(PfindAdapter);

        rv_week = findViewById(R.id.rv_week);
        WfindAdapter = new FindAdapter(getAttachActivity());
        WfindAdapter.setOnItemClickListener(this);
        rv_week.setAdapter(WfindAdapter);
    }

    @Override
    protected void initData() {
        this.getData();
    }

    private void getData() {
        EasyHttp.get(this).api((IRequestApi) new QueryMovieApi().setName(null).setPage(SfindAdapter.getPageNumber()).setPageSize(3)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                SfindAdapter.setData(result.getData());
            }
        });
        EasyHttp.get(this).api((IRequestApi) new QueryMovieApi().setName(null).setPage(PfindAdapter.getPageNumber()).setPageSize(3)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                PfindAdapter.setData(result.getData());
            }
        });

        EasyHttp.get(this).api((IRequestApi) new QueryMovieApi().setName(null).setPage(WfindAdapter.getPageNumber()).setPageSize(3)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                WfindAdapter.setData(result.getData());
            }
        });

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
        if (v.getId() == R.id.iv_home_search) {
            if (!et_search.getText().toString().trim().isEmpty()) {
                // 跳转到搜索 Activity 并传参
                Intent intent = new Intent(getAttachActivity().getContext(), QueryMovieActivity.class);
                intent.putExtra("keyword", et_search.getText().toString().trim());
                startActivity(intent);
            }
        } else if (v.getId() == R.id.upcoming) {
            startActivity(MovieListActivity.class);
        }else if(v.getId() == R.id.month){
            startActivity(MovieListActivity.class);
        }else if(v.getId() ==R.id.sb_popular){
            startActivity(MovieListActivity.class);
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

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {

    }

    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {

    }
}
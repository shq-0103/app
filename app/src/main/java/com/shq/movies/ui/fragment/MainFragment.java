package com.shq.movies.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;


public final class MainFragment extends MyFragment<HomeActivity>
        implements ViewPager.OnPageChangeListener, BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {
    private BGABanner banner_content;
    private EditText et_search;
    private ImageView iv_search;

    private WrapRecyclerView rv_recom_movie_list;
    private WrapRecyclerView rv_recom_review_list;

    private ListAdapter listAdapter;
    private MainReviewAdapter reviewAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {

        et_search = findViewById(R.id.et_search);
        iv_search = findViewById(R.id.iv_home_search);
        setOnClickListener(iv_search);
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

        // 设置轮播
        banner_content=findViewById(R.id.banner_main);
    }

    @Override
    protected void initData() {
        this.getData();
       // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 200, 320, 200);
       // 设置数据源
        banner_content.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.drawable.top1,
                R.drawable.top2,
                R.drawable.top3);


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
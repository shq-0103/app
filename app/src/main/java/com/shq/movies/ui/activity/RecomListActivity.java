package com.shq.movies.ui.activity;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.QueryMovieApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.ui.adapter.MovieListAdapter;

import java.util.List;

public final class RecomListActivity extends MyActivity
        implements ViewPager.OnPageChangeListener, BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {

    private WrapRecyclerView rv_recom;
    private MovieListAdapter movieListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recom_movielist;
    }

    @Override
    protected void initView() {
        rv_recom = findViewById(R.id.rv_recom);
        movieListAdapter = new MovieListAdapter(getActivity());
        movieListAdapter.setOnItemClickListener(this);
        rv_recom.setAdapter(movieListAdapter);
    }

    @Override
    protected void initData() {
        this.getData();
    }

    private void getData() {

        EasyHttp.get(this).api((IRequestApi) new QueryMovieApi().setName(null).setPage(movieListAdapter.getPageNumber()).setPageSize(8)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                movieListAdapter.setData(result.getData());
            }
        });
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
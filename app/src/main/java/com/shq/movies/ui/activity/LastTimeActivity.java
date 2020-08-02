package com.shq.movies.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.CollectMovieApi;
import com.shq.movies.http.request.OldMovieApi;
import com.shq.movies.http.request.QueryMovieApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.ui.adapter.MovieAdapter;
import com.shq.movies.ui.adapter.MovieListAdapter;

import java.util.ArrayList;
import java.util.List;

public final class LastTimeActivity extends MyActivity implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {

    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRecyclerView;
    private MovieListAdapter movieListAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_lastlist;
    }

    @Override
    protected void initView() {
        mRefreshLayout = findViewById(R.id.rl_favorite_movie_refresh);
        mRecyclerView = findViewById(R.id.rv_favorite_movie_list);

        movieListAdapter = new MovieListAdapter(getActivity());
        movieListAdapter.setOnItemClickListener(this);
        movieListAdapter.setOnChildClickListener(R.id.tv_movie_name,this);
        movieListAdapter.setOnChildClickListener(R.id.tv_movie_date,this);
        movieListAdapter.setOnChildClickListener(R.id.bt_favorite,this);



        mRecyclerView.setAdapter(movieListAdapter);

        TextView headerView = mRecyclerView.addHeaderView(R.layout.picker_item);
        headerView.setText("");
        headerView.setOnClickListener(v -> toast(""));

        TextView footerView = mRecyclerView.addFooterView(R.layout.picker_item);
        footerView.setText("");
        footerView.setOnClickListener(v -> toast(""));

        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        this.getData(false);
    }

    private void getData(boolean isLoadMore){

        EasyHttp.get(this).api((IRequestApi) new OldMovieApi().setPage(movieListAdapter.getPageNumber()).setPageSize(10)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                if(isLoadMore){
                    movieListAdapter.addData(result.getData());
                    mRefreshLayout.finishLoadMore();
                }else {
                    movieListAdapter.setData(result.getData());
                    mRefreshLayout.finishRefresh();
                }
                movieListAdapter.setPageNumber(movieListAdapter.getPageNumber()+1);
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
                if(isLoadMore){
                    mRefreshLayout.finishLoadMore();
                }else {
                    mRefreshLayout.finishRefresh();
                }
            }
        });
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        toast("onClickItem"+position);
    }

    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {
        switch (childView.getId()){
            case R.id.bt_favorite:
                toast("点击了喜欢"+movieListAdapter.getItem(position).getName());
                break;
            default:
                toast(((TextView)childView).getText() );
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        movieListAdapter.clearData();
        movieListAdapter.setPageNumber(1);
        this.getData(false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        this.getData(true);
    }
}
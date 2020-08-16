package com.shq.movies.ui.activity;

import android.content.Intent;
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
import com.shq.movies.http.request.OldMovieApi;
import com.shq.movies.http.request.RatingApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.http.response.RateBean;
import com.shq.movies.ui.adapter.RatingAdapter;

import java.util.List;

public final class RatingActivity extends MyActivity implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {

    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRecyclerView;
    private RatingAdapter ratingAapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userinfo_rating;
    }

    @Override
    protected void initView() {
        mRefreshLayout = findViewById(R.id.rl_rating_refresh);
        mRecyclerView = findViewById(R.id.rv_rating_list);

        ratingAapter = new RatingAdapter(getActivity());
        ratingAapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(ratingAapter);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        this.getData(false);
    }
    private void getData(boolean isLoadMore){

        EasyHttp.get(this).api((IRequestApi) new RatingApi().setPage(ratingAapter.getPageNumber()).setPageSize(10)).request(new HttpCallback<HttpData<List<RateBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<RateBean>> result) {
                super.onSucceed(result);
                if(isLoadMore){
                    ratingAapter.addData(result.getData());
                    mRefreshLayout.finishLoadMore();
                }else {
                    ratingAapter.setData(result.getData());
                    mRefreshLayout.finishRefresh();
                }
                ratingAapter.setPageNumber(ratingAapter.getPageNumber()+1);
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
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra("movieId",String.valueOf( ratingAapter.getItem(position).getMovieId()));
        startActivity(intent);
    }

    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}
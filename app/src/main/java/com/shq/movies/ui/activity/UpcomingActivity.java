package com.shq.movies.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.shq.movies.http.request.AddCollectApi;
import com.shq.movies.http.request.CollectMovieApi;
import com.shq.movies.http.request.DeleteCollectApi;
import com.shq.movies.http.request.OldMovieApi;
import com.shq.movies.http.request.QueryMovieApi;
import com.shq.movies.http.request.monthOrUncomeMovieApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.ui.adapter.MovieAdapter;
import com.shq.movies.ui.adapter.MovieListAdapter;
import com.shq.movies.ui.adapter.UpcomingAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class UpcomingActivity extends MyActivity implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {

    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView rv_upcoming;
    private UpcomingAdapter upcomingAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_upcomingmovies;
    }

    @Override
    protected void initView() {
        mRefreshLayout = findViewById(R.id.rl_favorite_movie_refresh);
        rv_upcoming = findViewById(R.id.rv_upcoming);

        upcomingAdapter = new UpcomingAdapter(getActivity());
        upcomingAdapter.setOnItemClickListener(this);
        upcomingAdapter.setOnChildClickListener(R.id.tv_movie_name,this);
        upcomingAdapter.setOnChildClickListener(R.id.tv_movie_date,this);
        upcomingAdapter.setOnChildClickListener(R.id.bt_favorite,this);



        rv_upcoming.setAdapter(upcomingAdapter);

        TextView headerView = rv_upcoming.addHeaderView(R.layout.picker_item);
        headerView.setText("");
        headerView.setOnClickListener(v -> toast(""));

        TextView footerView = rv_upcoming.addFooterView(R.layout.picker_item);
        footerView.setText("");
        footerView.setOnClickListener(v -> toast(""));

        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        this.getData(false);
    }

    private void getData(boolean isLoadMore){

        EasyHttp.get(this).api((IRequestApi) new monthOrUncomeMovieApi().setPage(upcomingAdapter.getPageNumber()).setOrder("upcoming").setPageSize(10)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                if(isLoadMore){
                    upcomingAdapter.addData(result.getData());
                    mRefreshLayout.finishLoadMore();
                }else {
                    upcomingAdapter.setData(result.getData());
                    mRefreshLayout.finishRefresh();
                }
                upcomingAdapter.setPageNumber(upcomingAdapter.getPageNumber()+1);
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
        if (recyclerView.getId() == R.id.rv_upcoming) {
            this.routerToDetail(String.valueOf(upcomingAdapter.getItem(position).getId()));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {
        switch (childView.getId()){
            case R.id.bt_favorite:
                onClickFavorite((ImageButton) childView, position);
                break;
            case R.id.rv_upcoming:
                this.routerToDetail(String.valueOf(upcomingAdapter.getItem(position).getId()));
                break;
            default:
                toast(((TextView)childView).getText() );
                break;
        }
    }
    public void routerToDetail (String movieId){
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra("movieId", movieId);
        startActivity(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onClickFavorite(ImageButton bt_favor, int position) {
        // 判断保存的 id 是否存在
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        String favorId = sharedPreferences.getString(getString(R.string.favorite_movie_id), null);
        boolean hasFavor = false;
        if (favorId != null && !favorId.isEmpty()) {
            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));
            hasFavor = ids.contains(String.valueOf(upcomingAdapter.getItem(position).getId()));
        }

        // 判断是否已登录
        String token = sharedPreferences.getString(getString(R.string.user_token), null);

        if (token == null || token.isEmpty()) {
            toast("please login");
            return;
        }

        if (!hasFavor) {
            EasyHttp.post(this)
                    .api(new AddCollectApi().setFavoriteId(upcomingAdapter.getItem(position).getId()).setType(1))
                    .request(new HttpCallback<HttpData<Boolean>>(this) {

                        @Override
                        public void onSucceed(HttpData<Boolean> data) {
                            bt_favor.setImageResource(R.drawable.ic_collect_2);
                            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));

                            ids.add(String.valueOf(upcomingAdapter.getItem(position).getId()));
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.favorite_movie_id), ids.stream().map(i -> i.toString()).collect(Collectors.joining("|")));
                            editor.commit();
                        }
                    });
        } else {
            EasyHttp.post(this)
                    .api(new DeleteCollectApi().setId(upcomingAdapter.getItem(position).getId()).setType(1))
                    .request(new HttpCallback<HttpData<Boolean>>(this) {

                        @Override
                        public void onSucceed(HttpData<Boolean> data) {
                            bt_favor.setImageResource(R.drawable.ic_collect_1);
                            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));

                            ids.remove(String.valueOf(upcomingAdapter.getItem(position).getId()));
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.favorite_movie_id), ids.stream().map(i -> i.toString()).collect(Collectors.joining("|")));
                            editor.commit();

                        }
                    });
        }
    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        upcomingAdapter.clearData();
        upcomingAdapter.setPageNumber(1);
        this.getData(false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        this.getData(true);
    }
}
package com.shq.movies.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.SettingBar;
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
import com.shq.movies.http.request.QueryMovieApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.ui.adapter.MovieAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QueryMovieActivity extends MyActivity implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {

    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRecyclerView;
    private MovieAdapter movieAdapter;
    private SettingBar sb_serach_keyword;


    private String keyword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_query_movie;
    }

    @Override
    protected void initView() {

        // 获取参数
        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");

        mRefreshLayout = findViewById(R.id.rl_favorite_movie_refresh);
        mRecyclerView = findViewById(R.id.rv_favorite_movie_list);
        sb_serach_keyword = findViewById(R.id.sb_search_keyword);

        sb_serach_keyword.setRightText(keyword);

        movieAdapter = new MovieAdapter(getActivity());
        movieAdapter.setOnItemClickListener(this);
        movieAdapter.setOnChildClickListener(R.id.tv_movie_name, this);
        movieAdapter.setOnChildClickListener(R.id.tv_movie_date, this);
        movieAdapter.setOnChildClickListener(R.id.bt_favorite, this);


        mRecyclerView.setAdapter(movieAdapter);

        TextView headerView = mRecyclerView.addHeaderView(R.layout.picker_item);
        headerView.setText("This is top");
        headerView.setOnClickListener(v -> toast("点击了头部"));

        TextView footerView = mRecyclerView.addFooterView(R.layout.picker_item);
        footerView.setText("This is bottom");
        footerView.setOnClickListener(v -> toast("点击了尾部"));

        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        this.getData(false);
    }

    private void getData(boolean isLoadMore) {

        EasyHttp.get(this).api((IRequestApi) new QueryMovieApi().setName(keyword).setPage(movieAdapter.getPageNumber()).setPageSize(10)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                if (isLoadMore) {
                    movieAdapter.addData(result.getData());
                    mRefreshLayout.finishLoadMore();
                } else {
                    movieAdapter.setData(result.getData());
                    mRefreshLayout.finishRefresh();
                }
                movieAdapter.setPageNumber(movieAdapter.getPageNumber() + 1);
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
                if (isLoadMore) {
                    mRefreshLayout.finishLoadMore();
                } else {
                    mRefreshLayout.finishRefresh();
                }
            }
        });
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        toast("onClickItem" + position);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {
        switch (childView.getId()) {
            case R.id.bt_favorite:
                onClickFavorite((ImageButton) childView, position);
                break;
            default:
                toast(((TextView) childView).getText());
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onClickFavorite(ImageButton bt_favor, int position) {
        // 判断保存的 id 是否存在
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        String favorId = sharedPreferences.getString(getString(R.string.favorite_movie_id), null);
        boolean hasFavor = false;
        if (favorId != null && !favorId.isEmpty()) {
            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));
            hasFavor = ids.contains(String.valueOf(movieAdapter.getItem(position).getId()));
        }

        // 判断是否已登录
        String token = sharedPreferences.getString(getString(R.string.user_token), null);

        if (token == null || token.isEmpty()) {
            toast("please login");
            return;
        }

        if (!hasFavor) {
            EasyHttp.post(this)
                    .api(new AddCollectApi().setFavoriteId(movieAdapter.getItem(position).getId()).setType(1))
                    .request(new HttpCallback<HttpData<Boolean>>(this) {

                        @Override
                        public void onSucceed(HttpData<Boolean> data) {
                            bt_favor.setImageResource(R.drawable.ic_collect_2);
                            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));

                            ids.add(String.valueOf(movieAdapter.getItem(position).getId()));
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.favorite_movie_id), ids.stream().map(i -> i.toString()).collect(Collectors.joining("|")));
                            editor.commit();
                        }
                    });
        } else {
            EasyHttp.post(this)
                    .api(new DeleteCollectApi().setId(movieAdapter.getItem(position).getId()).setType(1))
                    .request(new HttpCallback<HttpData<Boolean>>(this) {

                        @Override
                        public void onSucceed(HttpData<Boolean> data) {
                            bt_favor.setImageResource(R.drawable.ic_collect_1);
                            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));

                            ids.remove(String.valueOf(movieAdapter.getItem(position).getId()));
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.favorite_movie_id), ids.stream().map(i -> i.toString()).collect(Collectors.joining("|")));
                            editor.commit();

                        }
                    });
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        movieAdapter.clearData();
        movieAdapter.setPageNumber(1);
        this.getData(false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        this.getData(true);
    }
}
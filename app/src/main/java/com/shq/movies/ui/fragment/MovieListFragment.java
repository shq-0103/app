package com.shq.movies.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.shq.movies.common.MyFragment;
import com.shq.movies.event.QueryEvent;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.AddCollectApi;
import com.shq.movies.http.request.CollectMovieApi;
import com.shq.movies.http.request.DeleteCollectApi;
import com.shq.movies.http.request.QueryMovieApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.helper.OnClickHelper;
import com.shq.movies.ui.activity.MovieDetailActivity;
import com.shq.movies.ui.activity.MovieListActivity;
import com.shq.movies.ui.activity.MyMovieListActivity;
import com.shq.movies.ui.adapter.MovieAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class MovieListFragment extends MyFragment<MovieListActivity> implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView rv_favorite_movie_list;
    private MovieAdapter movieAdapter;

    @Subscribe

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favorite;
    }

    @Override
    protected void initView() {
        mRefreshLayout = findViewById(R.id.rl_favorite_movie_refresh);
        rv_favorite_movie_list = findViewById(R.id.rv_favorite_movie_list);

        movieAdapter = new MovieAdapter(getActivity());
        movieAdapter.setOnItemClickListener(this);
        movieAdapter.setOnChildClickListener(R.id.bt_favorite, this);


        rv_favorite_movie_list.setAdapter(movieAdapter);

        mRefreshLayout.setOnRefreshLoadMoreListener(this);


    }

    @Override
    protected void initData() {
        this.getData(false);
    }

    private void getData(boolean isLoadMore) {
        EasyHttp.get(this).api((IRequestApi) new QueryMovieApi().setName(null).setPage(movieAdapter.getPageNumber()).setOrder("viewCount").setPageSize(10)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                movieAdapter.setData(result.getData());
            }
        });
    }


    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        if (recyclerView.getId() == R.id.rv_favorite_movie_list) {
            this.routerToDetail(String.valueOf(movieAdapter.getItem(position).getId()));
        }
    }

    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {
        switch (childView.getId()) {
            case R.id.bt_favorite:
//                toast("点击了喜欢" + movieAdapter.getItem(position).getName());
                OnClickHelper.onClickFavorite((ImageButton) childView,movieAdapter.getItem(position).getId(),getAttachActivity());
                onRefresh(mRefreshLayout);
                break;
            default:
                break;
        }
    }
    public void routerToDetail(String movieId) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra("movieId", movieId);
        startActivity(intent);
    }


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

                        @RequiresApi(api = Build.VERSION_CODES.N)
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
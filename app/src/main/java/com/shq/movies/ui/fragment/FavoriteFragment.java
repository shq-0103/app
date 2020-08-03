package com.shq.movies.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shq.movies.R;
import com.shq.movies.common.MyFragment;
import com.shq.movies.event.QueryEvent;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.CollectMovieApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.helper.OnClickHelper;
import com.shq.movies.ui.activity.MyMovieListActivity;
import com.shq.movies.ui.adapter.MovieAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public final class FavoriteFragment extends MyFragment<MyMovieListActivity> implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRecyclerView;
    private MovieAdapter movieAdapter;
    private CollectMovieApi collectMovieApi;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void changeQuery(QueryEvent queryEvent) {
        if(queryEvent.key.equals("queryChange")){
            collectMovieApi.setDecade(queryEvent.decade).setGenres(queryEvent.genres).setOrder(queryEvent.order);
            onRefresh(mRefreshLayout);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favorite;
    }

    @Override
    protected void initView() {
        mRefreshLayout = findViewById(R.id.rl_favorite_movie_refresh);
        mRecyclerView = findViewById(R.id.rv_favorite_movie_list);

        movieAdapter = new MovieAdapter(getActivity());
        movieAdapter.setOnItemClickListener(this);
        movieAdapter.setOnChildClickListener(R.id.bt_favorite, this);


        mRecyclerView.setAdapter(movieAdapter);

        collectMovieApi = new CollectMovieApi().setPage(movieAdapter.getPageNumber()).setPageSize(10);

        mRefreshLayout.setOnRefreshLoadMoreListener(this);


    }

    @Override
    protected void initData() {
        this.getData(false);
    }

    private void getData(boolean isLoadMore) {

        EasyHttp.get(this).api(collectMovieApi.setPage(movieAdapter.getPageNumber()).setPageSize(10)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                if (result.getData() == null || result.getData().isEmpty()) {
                    toast(R.string.hint_no_more_data);
                    movieAdapter.setPageNumber(movieAdapter.getPageNumber() - 1);
                    movieAdapter.setLastPage(true);
                    mRefreshLayout.setNoMoreData(true);
                    return;
                }
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
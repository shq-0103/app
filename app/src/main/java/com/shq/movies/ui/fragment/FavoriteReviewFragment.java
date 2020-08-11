package com.shq.movies.ui.fragment;

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
import com.shq.movies.common.MyFragment;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.CollectMovieApi;
import com.shq.movies.http.request.MyReviewApi;
import com.shq.movies.http.request.ReviewApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.http.response.ReviewBean;
import com.shq.movies.ui.activity.MyMovieListActivity;
import com.shq.movies.ui.adapter.FavoriteReviewAdapter;
import com.shq.movies.ui.adapter.MovieAdapter;
import com.shq.movies.ui.adapter.MovieReviewAdapter;
import com.shq.movies.ui.adapter.StatusAdapter;

import java.util.List;

public final class FavoriteReviewFragment extends MyFragment<MyActivity> implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {

    public static FavoriteReviewFragment newInstance() {
        return new FavoriteReviewFragment();
    }

    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRecyclerView;
    private FavoriteReviewAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favorite;
    }

    @Override
    protected void initView() {
        mRefreshLayout = findViewById(R.id.rl_favorite_movie_refresh);
        mRecyclerView = findViewById(R.id.rv_favorite_movie_list);


        mAdapter = new FavoriteReviewAdapter(getAttachActivity());
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        this.getData(false);
    }

    private void getData(boolean isLoadMore){

        EasyHttp.get(this).api((IRequestApi) new MyReviewApi().setPage(mAdapter.getPageNumber()).setPageSize(10)).request(new HttpCallback<HttpData<List<ReviewBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<ReviewBean>> result) {
                super.onSucceed(result);
                if(isLoadMore){
                    mAdapter.addData(result.getData());
                    mRefreshLayout.finishLoadMore();
                }else {
                    mAdapter.setData(result.getData());
                    mRefreshLayout.finishRefresh();
                }
                mAdapter.setPageNumber(mAdapter.getPageNumber()+1);
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
//    public void onChildClick(RecyclerView recyclerView, View childView, int position) {
//        switch (childView.getId()){
//            case R.id.bt_favorite:
//                toast("点击了喜欢"+reviewAdapter.getItem(position).getName());
//                break;
//            default:
//                toast(((TextView)childView).getText() );
//                break;
//        }
//    }


    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mAdapter.clearData();
        mAdapter.setPageNumber(1);
        this.getData(false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        this.getData(true);
    }

    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {

    }
}
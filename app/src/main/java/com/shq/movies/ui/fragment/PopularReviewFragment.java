package com.shq.movies.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.shq.movies.helper.OnClickHelper;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.CollectMovieApi;
import com.shq.movies.http.request.OnLikeApi;
import com.shq.movies.http.request.ReviewApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.http.response.ReviewBean;
import com.shq.movies.ui.activity.MyMovieListActivity;
import com.shq.movies.ui.activity.PasswordForgetActivity;
import com.shq.movies.ui.activity.ReviewDetailActivity;
import com.shq.movies.ui.activity.WriteReviewActivity;
import com.shq.movies.ui.adapter.MovieAdapter;
import com.shq.movies.ui.adapter.MovieReviewAdapter;

import java.util.List;

public final class PopularReviewFragment extends MyFragment<MyActivity> implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {

    public static PopularReviewFragment newInstance() {
        return new PopularReviewFragment();
    }


    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRecyclerView;
    private MovieReviewAdapter reviewAdapter;
    private FloatingActionButton fab;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reviewlist;
    }

    @Override
    protected void initView() {
        mRefreshLayout = findViewById(R.id.rl_favorite_movie_refresh);
        mRecyclerView = findViewById(R.id.rv_favorite_movie_list);
        fab = findViewById(R.id.fab);
        setOnClickListener(fab);

        reviewAdapter = new MovieReviewAdapter(getActivity());
        reviewAdapter.setOnItemClickListener(this);
        reviewAdapter.setOnChildClickListener(R.id.iv_good,this);
        mRecyclerView.setAdapter(reviewAdapter);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        this.getData(false);
    }

    private void getData(boolean isLoadMore) {

        EasyHttp.get(this).api((IRequestApi) new ReviewApi().setPage(reviewAdapter.getPageNumber()).setOrder("popular").setPageSize(10)).request(new HttpCallback<HttpData<List<ReviewBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<ReviewBean>> result) {
                super.onSucceed(result);
                if (isLoadMore) {
                    reviewAdapter.addData(result.getData());
                    mRefreshLayout.finishLoadMore();
                } else {
                    reviewAdapter.setData(result.getData());
                    mRefreshLayout.finishRefresh();
                }
                reviewAdapter.setPageNumber(reviewAdapter.getPageNumber() + 1);
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

    public void onClick(View v) {
        if (v == fab) {
            startActivity(WriteReviewActivity.class);
        }
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        Intent intent = new Intent(getContext(), ReviewDetailActivity.class);
        intent.putExtra("reviewId", reviewAdapter.getItem(position).getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh(mRefreshLayout);
    }

    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        reviewAdapter.clearData();
        reviewAdapter.setPageNumber(1);
        this.getData(false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        this.getData(true);
    }

    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {
        if(childView.getId()==R.id.iv_good){
            EasyHttp.post(this)
                    .api(new OnLikeApi().setToId(reviewAdapter.getItem(position).getId()).setType(1))
                    .request(new HttpCallback<HttpData<Boolean>>(this) {

                        @Override
                        public void onSucceed(HttpData<Boolean> data) {
                            if(data.getData()){
                                OnClickHelper.delOrAdd(reviewAdapter.getItem(position).getId(),R.string.like_review_id,false,getContext());
                                ((ImageView)childView).setImageDrawable(getDrawable( R.drawable.ic_good_on));
                                ((TextView)recyclerView.findViewHolderForAdapterPosition(position)
                                        .itemView
                                        .findViewById(R.id.tv_good))
                                        .setText(String.valueOf(reviewAdapter.getItem(position).getLikeNum()+1));
                                reviewAdapter.getItem(position).setLikeNum(reviewAdapter.getItem(position).getLikeNum()+1);
                            }else {
                                OnClickHelper.delOrAdd(reviewAdapter.getItem(position).getId(),R.string.like_review_id,true,getContext());
                                ((ImageView)childView).setImageDrawable(getDrawable( R.drawable.ic_good));
                                ((TextView)recyclerView.findViewHolderForAdapterPosition(position)
                                        .itemView
                                        .findViewById(R.id.tv_good))
                                        .setText(String.valueOf(reviewAdapter.getItem(position).getLikeNum()-1));
                                reviewAdapter.getItem(position).setLikeNum(reviewAdapter.getItem(position).getLikeNum()-1);
                            }
                        }
                    });
        }
    }
}
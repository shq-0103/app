package com.shq.movies.ui.fragment;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.base.BaseAdapter;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.common.MyFragment;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.DeleteReviewApi;
import com.shq.movies.http.request.MyReviewApi;
import com.shq.movies.http.response.ReviewBean;
import com.shq.movies.ui.activity.ReviewDetailActivity;
import com.shq.movies.ui.adapter.FavoriteReviewAdapter;
import com.shq.movies.ui.dialog.MessageDialog;

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
        mAdapter.setOnChildClickListener(R.id.iv_delete, this);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        this.getData(false);
    }

    private void getData(boolean isLoadMore) {

        EasyHttp.get(this).api((IRequestApi) new MyReviewApi().setPage(mAdapter.getPageNumber()).setPageSize(10)).request(new HttpCallback<HttpData<List<ReviewBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<ReviewBean>> result) {
                super.onSucceed(result);
                if (isLoadMore) {
                    mAdapter.addData(result.getData());
                    mRefreshLayout.finishLoadMore();
                } else {
                    mAdapter.setData(result.getData());
                    mRefreshLayout.finishRefresh();
                }
                mAdapter.setPageNumber(mAdapter.getPageNumber() + 1);
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
        Intent intent = new Intent(getContext(), ReviewDetailActivity.class);
        intent.putExtra("reviewId", mAdapter.getItem(position).getId());
        startActivity(intent);
    }


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
        if (childView.getId() == R.id.iv_delete) {
            new MessageDialog.Builder(getContext())
                    .setMessage("Delete or not?")
                    .setConfirm(getString(R.string.common_confirm))
                    .setCancel(getString(R.string.common_cancel))
                    .setListener(new MessageDialog.OnListener() {

                        @Override
                        public void onConfirm(BaseDialog dialog) {
                            EasyHttp.get(getActivity())
                                    .api(new DeleteReviewApi().setId(mAdapter.getItem(position).getId()))
                                    .request(new HttpCallback<HttpData<Boolean>>((OnHttpListener) getActivity()) {
                                        @Override
                                        public void onSucceed(HttpData<Boolean> data) {
                                            onRefresh(mRefreshLayout);
                                        }
                                    });
                        }
                    })
                    .show();
        }
    }
}
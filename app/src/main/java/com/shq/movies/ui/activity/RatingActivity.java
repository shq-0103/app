package com.shq.movies.ui.activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.base.BaseAdapter;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.ui.adapter.RatingAdapter;

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

        TextView headerView = mRecyclerView.addHeaderView(R.layout.picker_item);
        headerView.setText("我是头部");
        headerView.setOnClickListener(v -> toast("点击了头部"));

        TextView footerView = mRecyclerView.addFooterView(R.layout.picker_item);
        footerView.setText("我是尾部");
        footerView.setOnClickListener(v -> toast("点击了尾部"));

        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {

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
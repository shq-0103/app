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
import com.shq.movies.ui.adapter.MessageAdapter;


public final class MessageActivity extends MyActivity implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {

    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRecyclerView;
    private MessageAdapter messageAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userinfo_message;
    }

    @Override
    protected void initView() {
        mRefreshLayout = findViewById(R.id.rl_notice_refresh);
        mRecyclerView = findViewById(R.id.rv_notice_list);

        messageAdapter = new MessageAdapter(getActivity());
        messageAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(messageAdapter);

//        TextView headerView = mRecyclerView.addHeaderView(R.layout.picker_item);
//        headerView.setText("");
//        headerView.setOnClickListener(v -> toast(""));
//
//        TextView footerView = mRecyclerView.addFooterView(R.layout.picker_item);
//        footerView.setText("");
//        footerView.setOnClickListener(v -> toast(""));

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
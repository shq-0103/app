package com.shq.movies.ui.activity;

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
import com.shq.movies.http.request.MessageApi;
import com.shq.movies.http.request.MovieRateApi;
import com.shq.movies.http.response.MessageBean;
import com.shq.movies.http.response.RateBean;
import com.shq.movies.ui.adapter.MessageAdapter;

import java.util.List;


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
        this.getData(false);
    }
    private void getData(boolean isLoadMore){

        EasyHttp.get(this).api( new MessageApi().setPage(messageAdapter.getPageNumber()).setPageSize(10)).request(new HttpCallback<HttpData<List<MessageBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MessageBean>> result) {
                super.onSucceed(result);
                if(isLoadMore){
                    messageAdapter.addData(result.getData());
                    mRefreshLayout.finishLoadMore();
                }else {
                    messageAdapter.setData(result.getData());
                    mRefreshLayout.finishRefresh();
                }
                messageAdapter.setPageNumber(messageAdapter.getPageNumber()+1);
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
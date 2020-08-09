package com.shq.movies.ui.activity;

import android.view.View;

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
import com.shq.movies.http.request.MovieRateApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.http.response.RateBean;
import com.shq.movies.ui.adapter.CommentAdapter;

import java.util.List;

public class CommentListActivity extends MyActivity implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {
    private SmartRefreshLayout rl_commentlist;
    private WrapRecyclerView rv_commentlist;
    private CommentAdapter commentAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_commentlist;
    }

    @Override
    protected void initView() {
        rl_commentlist = findViewById(R.id.rl_commentlist);
        rv_commentlist = findViewById(R.id.rv_commentlist);

        commentAdapter = new CommentAdapter(getActivity());
        commentAdapter.setOnItemClickListener(this);
        rv_commentlist.setAdapter(commentAdapter);

        rl_commentlist.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        this.getData(false);
    }
    private void getData(boolean isLoadMore){

        EasyHttp.get(this).api((IRequestApi) new MovieRateApi().setPage(commentAdapter.getPageNumber()).setPageSize(10)).request(new HttpCallback<HttpData<List<RateBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<RateBean>> result) {
                super.onSucceed(result);
                if(isLoadMore){
                    commentAdapter.addData(result.getData());
                    rl_commentlist.finishLoadMore();
                }else {
                    commentAdapter.setData(result.getData());
                    rl_commentlist.finishRefresh();
                }
                commentAdapter.setPageNumber(commentAdapter.getPageNumber()+1);
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
                if(isLoadMore){
                    rl_commentlist.finishLoadMore();
                }else {
                    rl_commentlist.finishRefresh();
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

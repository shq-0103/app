package com.shq.movies.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.hjq.base.BaseAdapter;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.ui.adapter.ListAdapter;
import com.shq.movies.ui.adapter.MainReviewAdapter;
import com.shq.movies.ui.adapter.ReviewCommentAdapter;


public final class ReviewDetailActivity extends MyActivity
        implements ViewPager.OnPageChangeListener, BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener, OnRefreshLoadMoreListener {
    private ImageView iv_userimg;
    private ImageView iv_authorimg;
    private TextView tv_author;
    private TextView tv_date;
    private TextView tv_usertitle;
    private TextView tv_usercontent;
    private TextView tv_totalcomment;
    private EditText et_input;

    private SmartRefreshLayout mRefreshLayout;
    private ReviewCommentAdapter reviewCommentAdapter;
    private WrapRecyclerView rv_comment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_review_detail;
    }

    @Override
    protected void initView() {
        iv_authorimg = findViewById(R.id.iv_authorimg);
        iv_userimg = findViewById(R.id.iv_userimg);
        tv_author = findViewById(R.id.tv_author);
        tv_date = findViewById(R.id.tv_date);
        tv_usertitle = findViewById(R.id.tv_usertitle);
        tv_usercontent = findViewById(R.id.tv_usercontent);
        tv_totalcomment = findViewById(R.id.tv_totalcomment);
        et_input = findViewById(R.id.et_input);

        rv_comment = findViewById(R.id.rv_comment);
        mRefreshLayout = findViewById(R.id.rl_status_refresh);
        reviewCommentAdapter = new ReviewCommentAdapter(getActivity());
        reviewCommentAdapter.setOnItemClickListener(this);
        rv_comment.setAdapter(reviewCommentAdapter);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {

    }

    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
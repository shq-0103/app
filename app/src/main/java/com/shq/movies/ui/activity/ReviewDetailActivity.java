package com.shq.movies.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.CollectMovieApi;
import com.shq.movies.http.request.ReviewDetailApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.http.response.ReviewBean;
import com.shq.movies.ui.adapter.ListAdapter;
import com.shq.movies.ui.adapter.MainReviewAdapter;
import com.shq.movies.ui.adapter.ReviewCommentAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


public final class ReviewDetailActivity extends MyActivity
        implements ViewPager.OnPageChangeListener, BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener, OnRefreshLoadMoreListener {
    private ImageView iv_userimg;
    private ImageView iv_authorimg;
    private ImageView iv_review_img;

    private TextView tv_author;
    private TextView tv_date;
    private TextView tv_usertitle;
    private TextView tv_usercontent;
    private TextView tv_totalcomment;
    private EditText et_input;

    private SmartRefreshLayout mRefreshLayout;
    private ReviewCommentAdapter reviewCommentAdapter;
    private WrapRecyclerView rv_comment;

    private long reviewId;
    private ReviewBean reviewBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_review_detail;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        reviewId = intent.getLongExtra("reviewId", 0);
        iv_authorimg = findViewById(R.id.iv_authorimg);
        iv_userimg = findViewById(R.id.iv_userimg);
        iv_review_img = findViewById(R.id.iv_review_img);
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
        EasyHttp.get(this).api(new ReviewDetailApi().setReviewId(reviewId)).request(new HttpCallback<HttpData<ReviewBean>>(this) {
            @Override
            public void onSucceed(HttpData<ReviewBean> result) {
                super.onSucceed(result);
                reviewBean = result.getData();

                tv_author.setText(reviewBean.getNickname());

                // Date转String
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                tv_date.setText(sdf.format(new Date(reviewBean.getDate() * 1000)));
                GlideApp.with(getContext())
                        .load(EasyConfig.getInstance().getServer().getHost() + reviewBean.getAvatar())
                        .error(R.drawable.avatar_placeholder_ic)
                        .circleCrop()
                        .into(iv_authorimg);
                if (TextUtils.isEmpty(reviewBean.getImages())) {
                    iv_review_img.setVisibility(View.GONE);
                } else {
                    GlideApp.with(getContext())
                            .load(EasyConfig.getInstance().getServer().getHost() + reviewBean.getImages())
                            .error(R.drawable.ic_movie_placeholder)
                            .into(iv_review_img);
                }
                tv_usercontent.setText(reviewBean.getContents());
                tv_usertitle.setText(reviewBean.getTitle());
            }
        });

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
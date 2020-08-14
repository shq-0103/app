package com.shq.movies.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.gyf.immersionbar.ImmersionBar;
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
import com.shq.movies.helper.InputTextHelper;
import com.shq.movies.helper.OnClickHelper;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.AddCommentApi;
import com.shq.movies.http.request.CollectMovieApi;
import com.shq.movies.http.request.OnLikeApi;
import com.shq.movies.http.request.ReviewCommentApi;
import com.shq.movies.http.request.ReviewDetailApi;
import com.shq.movies.http.request.ReviewImageApi;
import com.shq.movies.http.response.CommentBean;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.http.response.ReviewBean;
import com.shq.movies.ui.adapter.ListAdapter;
import com.shq.movies.ui.adapter.MainReviewAdapter;
import com.shq.movies.ui.adapter.ReviewCommentAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private EditText et_comment;

    private SmartRefreshLayout mRefreshLayout;
    private ReviewCommentAdapter reviewCommentAdapter;
    private WrapRecyclerView rv_comment;

    private long reviewId;
    private ReviewBean reviewBean;
    private Button bt_submit;

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
        et_comment = findViewById(R.id.et_comment);
        bt_submit=findViewById(R.id.bt_submit);

        rv_comment = findViewById(R.id.rv_comment);
        mRefreshLayout = findViewById(R.id.rl_status_refresh);
        reviewCommentAdapter = new ReviewCommentAdapter(getActivity());
        reviewCommentAdapter.setOnItemClickListener(this);
        reviewCommentAdapter.setOnChildClickListener(R.id.iv_good,this);
        rv_comment.setAdapter(reviewCommentAdapter);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        InputTextHelper.with(this.getActivity())
                .addView(et_comment)
                .setMain(bt_submit)
                .build();
        setOnClickListener(bt_submit);

    }

    @Override
    protected void initData() {
        SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        String token=sharedPreferences.getString(getString(R.string.user_token),null);

        GlideUrl glideUrl = new GlideUrl(EasyConfig.getInstance().getServer().getHost() + "avatar", new Headers() {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                //不一定都要添加，具体看原站的请求信息
                header.put("Authorization", "Bearer "+token);
                return header;
            }
        });
        GlideApp.with(getContext())
                .load(glideUrl)
                .error(R.drawable.avatar_placeholder_ic)
                .circleCrop()
                .into(iv_userimg);
        EasyHttp.get(this).api(new ReviewDetailApi().setReviewId(reviewId)).request(new HttpCallback<HttpData<ReviewBean>>(this) {
            @Override
            public void onSucceed(HttpData<ReviewBean> result) {
                super.onSucceed(result);
                reviewBean = result.getData();

                tv_author.setText(reviewBean.getNickname());
                tv_totalcomment.setText("Comment ("+reviewBean.getCommentNum()+") ");
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

        this.getReviewComment(false);
    }

    private void getReviewComment(boolean isLoadMore){
        EasyHttp.get(this).api(new ReviewCommentApi().setId(reviewId).setPage(reviewCommentAdapter.getPageNumber()).setPageSize(10))
                .request(new HttpCallback<HttpData<List<CommentBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<CommentBean>> result) {
                        super.onSucceed(result);
                        if (result.getData() == null || result.getData().isEmpty()) {
                            toast(R.string.hint_no_more_data);
                            reviewCommentAdapter.setPageNumber(reviewCommentAdapter.getPageNumber() - 1);
                            reviewCommentAdapter.setLastPage(true);
                            mRefreshLayout.setNoMoreData(true);
                            return;
                        }
                        if(isLoadMore){
                            reviewCommentAdapter.addData(result.getData());
                            mRefreshLayout.finishLoadMore();
                        }else {
                            reviewCommentAdapter.setData(result.getData());
                            mRefreshLayout.finishRefresh();
                        }
                        reviewCommentAdapter.setPageNumber(reviewCommentAdapter.getPageNumber()+1);
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
    public void onClick(View v) {
        if(v.getId()==R.id.bt_submit){
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
            // 判断是否已登录
            String token = sharedPreferences.getString(getString(R.string.user_token), null);

            if (token == null || token.isEmpty()) {
                toast("please login");
                return;
            }
            EasyHttp.post(this)
                    .api(new AddCommentApi().setContents(et_comment.getText().toString())
                    .setType(1).setToId(reviewId))
                    .request(new HttpCallback<HttpData<String>>(this) {

                        @Override
                        public void onSucceed(HttpData<String> data) {
                            reviewBean.setCommentNum(reviewBean.getCommentNum()+1);
                            tv_totalcomment.setText("Comment ("+reviewBean.getCommentNum()+") ");
                            et_comment.clearFocus();
                            et_comment.setText(null);
                            onRefresh(mRefreshLayout);
                        }
                    });
        }
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
        if(childView.getId()==R.id.iv_good){
            EasyHttp.post(this)
                    .api(new OnLikeApi().setToId(reviewCommentAdapter.getItem(position).getId()).setType(2))
                    .request(new HttpCallback<HttpData<Boolean>>(this) {

                        @Override
                        public void onSucceed(HttpData<Boolean> data) {
                            if(data.getData()){
                                OnClickHelper.delOrAdd(reviewCommentAdapter.getItem(position).getId(),R.string.like_comment_id,false,getContext());
                                ((ImageView)childView).setImageDrawable(getDrawable( R.drawable.ic_good_on));
                            }else {
                                OnClickHelper.delOrAdd(reviewCommentAdapter.getItem(position).getId(),R.string.like_comment_id,true,getContext());
                                ((ImageView)childView).setImageDrawable(getDrawable( R.drawable.ic_good));
                            }
                        }
                    });
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        reviewCommentAdapter.clearData();
        reviewCommentAdapter.setPageNumber(1);
        this.getReviewComment(false);
    }
    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // Don't put the entire layout on top
                .keyboardEnable(true);
    }
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        this.getReviewComment(true);
    }
}
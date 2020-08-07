package com.shq.movies.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.hjq.bar.TitleBar;
import com.hjq.base.BaseAdapter;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.helper.OnClickHelper;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.AddCollectApi;
import com.shq.movies.http.request.CommentApi;
import com.shq.movies.http.request.DeleteCollectApi;
import com.shq.movies.http.request.MovieDetailApi;
import com.shq.movies.http.request.QueryMovieApi;
import com.shq.movies.http.request.ReviewApi;
import com.shq.movies.http.request.UserApi;
import com.shq.movies.http.response.CommentBean;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.http.response.ReviewBean;
import com.shq.movies.http.response.UserInfoBean;
import com.shq.movies.ui.adapter.CommentAdapter;
import com.shq.movies.ui.adapter.MainReviewAdapter;
import com.shq.movies.ui.dialog.MessageDialog;
import com.shq.movies.widget.XCollapsingToolbarLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class MovieDetailActivity extends MyActivity
        implements ViewPager.OnPageChangeListener, BaseAdapter.OnItemClickListener,
        BaseAdapter.OnChildClickListener, XCollapsingToolbarLayout.OnScrimsListener {
    private ImageView iv_movie_cover;
    private TextView tv_movie_title;
    private TextView tv_movie_type;
    private TextView tv_movie_time;
    private Button bt_favorite;
    private TextView tv_score;
    private TextView tv_derector;
    private TextView tv_actor;
    private TextView tv_intro;
    private Long movieId;
    private TabLayout tabLayout;
    private TabItem tab1;
    private TabItem tab2;
    private TabItem tab3;
    private int tabIndex = 0;//tablayout所处的下标
    private boolean scrollviewFlag = false;//标记是否是scrollview在滑动
    private WrapRecyclerView rv_comment;
    private CommentAdapter commentAdapter;
    private Button bt_seen;

    private XCollapsingToolbarLayout mCollapsingToolbarLayout;
    private TitleBar tb_moviedetail;
    private MovieBean movieDetail = new MovieBean();
    private LinearLayout ll_fav_container;

    @Override
    protected int getLayoutId() {
        return R.layout.acticity_moviedetail;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        movieId = Long.parseLong(intent.getStringExtra("movieId"));

        iv_movie_cover = findViewById(R.id.iv_movie_cover);
        tv_movie_title = findViewById(R.id.tv_movie_title);
        tv_movie_type = findViewById(R.id.tv_movie_type);
        tv_movie_time = findViewById(R.id.tv_movie_time);
        bt_favorite = findViewById(R.id.bt_favorite);
        tv_score = findViewById(R.id.tv_score);
        tv_derector = findViewById(R.id.tv_derector);
        tv_actor = findViewById(R.id.tv_actor);
        tv_intro = findViewById(R.id.tv_intro);
        tabLayout = findViewById(R.id.tabLayout);
        tab1 = findViewById(R.id.tab1);
        tab2 = findViewById(R.id.tab2);
        tab3 = findViewById(R.id.tab3);
        bt_seen = findViewById(R.id.bt_seen);
        setOnClickListener(bt_favorite, bt_seen);

        rv_comment = findViewById(R.id.rv_comment);
        commentAdapter = new CommentAdapter(getActivity());
        commentAdapter.setOnItemClickListener(this);
        rv_comment.setAdapter(commentAdapter);


        tb_moviedetail = findViewById(R.id.tb_moviedetail);
        mCollapsingToolbarLayout = findViewById(R.id.ctl_home_bar);
        mCollapsingToolbarLayout.setOnScrimsListener(this);
        ll_fav_container=findViewById(R.id.ll_fav_container);
    }

    @Override
    protected void initData() {
        EasyHttp.get(this).api(new MovieDetailApi().setId(movieId)).request(new HttpCallback<HttpData<MovieBean>>(this) {
            @Override
            public void onSucceed(HttpData<MovieBean> result) {
                movieDetail = result.getData();
                super.onSucceed(result);
                tv_movie_title.setText(result.getData().getName());
                tv_movie_type.setText(result.getData().getGenres());
                tv_movie_time.setText(result.getData().getReleaseDate());
                tv_score.setText(String.valueOf(result.getData().getScore()));
                tv_derector.setText(result.getData().getDirector());
                tv_actor.setText(result.getData().getActor());
                tv_intro.setText(result.getData().getIntro());
                if(OnClickHelper.hasFavorite(movieId,(AppCompatActivity) getActivity())){
                    bt_favorite.setBackgroundColor(getResources().getColor(R.color.colorSelect) );
                }
                if(OnClickHelper.hasRate(movieId,(AppCompatActivity) getActivity())){
                    bt_seen.setBackgroundColor(getResources().getColor(R.color.colorSelect) );
                }
                GlideApp.with(getActivity())
                        .load(result.getData().getCover())
                        .placeholder(R.drawable.ic_movie_placeholder)
                        .error(R.drawable.ic_movie_placeholder)
                        .into(iv_movie_cover);
            }
        });
        this.getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void getData() {

        EasyHttp.get(this).api((IRequestApi) new CommentApi().setPage(commentAdapter.getPageNumber()).setPageSize(5)).request(new HttpCallback<HttpData<List<CommentBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<CommentBean>> result) {
                super.onSucceed(result);
                commentAdapter.setData(result.getData());
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_seen:
                if(!OnClickHelper.hasRate(movieId,(AppCompatActivity)getActivity())){
                    Intent intent = new Intent(getContext(), RateActivity.class);
                    intent.putExtra("movieId", movieId);
                    startActivity(intent);
                }

                break;
            case R.id.bt_favorite:
                OnClickHelper.onClickFavorite(bt_favorite,movieId,(AppCompatActivity)getActivity());
                break;
            default:
                break;
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

    }


    /**
     * CollapsingToolbarLayout 渐变回调
     * <p>
     * {@link XCollapsingToolbarLayout.OnScrimsListener}
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void onScrimsStateChange(XCollapsingToolbarLayout layout, boolean shown) {
        System.out.println(shown);
        if (shown) {
            tb_moviedetail.setTitle(movieDetail.getName());
            tb_moviedetail.setBackground(getDrawable(R.drawable.images));
            ll_fav_container.setVisibility(View.INVISIBLE);
            getStatusBarConfig().statusBarDarkFont(true).init();
        } else {
            tb_moviedetail.setTitle(null);
            ll_fav_container.setVisibility(View.VISIBLE);
            tb_moviedetail.setBackground(null);
            getStatusBarConfig().statusBarDarkFont(false).init();
        }
    }

}
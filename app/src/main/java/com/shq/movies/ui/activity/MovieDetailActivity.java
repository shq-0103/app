package com.shq.movies.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.hjq.bar.TitleBar;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.SettingBar;
import com.hjq.widget.layout.WrapRecyclerView;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.helper.OnClickHelper;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.MovieLikeThisApi;
import com.shq.movies.http.request.MovieRateApi;
import com.shq.movies.http.request.MovieDetailApi;
import com.shq.movies.http.request.OnLikeApi;
import com.shq.movies.http.request.SametypeApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.http.response.RateBean;
import com.shq.movies.ui.adapter.CommentAdapter;
import com.shq.movies.ui.adapter.SametypeAdapter;
import com.shq.movies.widget.XCollapsingToolbarLayout;

import java.util.List;

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
    private TextView sb_number;
    private SettingBar sb_comment;
    private SettingBar sb_movie;

    private TabLayout tabLayout;

    private TextView tv_movie;

    private int tabIndex = 0;//tablayout所处的下标
    private boolean scrollviewFlag = false;//标记是否是scrollview在滑动
    private NestedScrollView scrollView;

    private WrapRecyclerView rv_comment;
    private WrapRecyclerView rv_sametype;
    private CommentAdapter commentAdapter;
    private SametypeAdapter sametypeAdapter;
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
        tv_movie = findViewById(R.id.tv_movie);
        scrollView=findViewById(R.id.scrollView);
        bt_seen = findViewById(R.id.bt_seen);
        sb_number = findViewById(R.id.sb_number);
        sb_comment = findViewById(R.id.sb_comment);
        sb_movie=findViewById(R.id.sb_movie);
        setOnClickListener(bt_favorite, bt_seen,sb_comment);

        rv_comment = findViewById(R.id.rv_comment);
        commentAdapter = new CommentAdapter(getActivity());
        commentAdapter.setOnItemClickListener(this);
        commentAdapter.setOnChildClickListener(R.id.iv_good,this);
        rv_comment.setAdapter(commentAdapter);

        rv_sametype = findViewById(R.id.rv_sametype);
        rv_sametype.setLayoutManager(new LinearLayoutManager(getContext(), WrapRecyclerView.HORIZONTAL, false));
        sametypeAdapter = new SametypeAdapter(getActivity());
        sametypeAdapter.setOnItemClickListener(this);
        rv_sametype.setAdapter(sametypeAdapter);

        tb_moviedetail = findViewById(R.id.tb_moviedetail);
        mCollapsingToolbarLayout = findViewById(R.id.ctl_home_bar);
        mCollapsingToolbarLayout.setOnScrimsListener(this);
        ll_fav_container=findViewById(R.id.ll_fav_container);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (!scrollviewFlag) {
                    switch (tab.getPosition()) {
                        case 0://scrollview移动到tv1的顶部坐标处
                            scrollView.scrollTo(0, tv_movie.getTop());

                            break;
                        case 1://scrollview移动到tv2的顶部坐标处
                            scrollView.scrollTo(0, sb_comment.getTop());

                            break;
                        case 2://scrollview移动到tv3的顶部坐标处
                            scrollView.scrollTo(0, sb_movie.getTop());

                            break;
                    }
                }
                tab.select();
                //用户点击tablayout时，标记不是scrollview主动滑动
                scrollviewFlag = false;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrollviewFlag = true;
                tabIndex = tabLayout.getSelectedTabPosition();
                if (scrollY < sb_comment.getTop()) {
                    if (tabIndex != 0) {//增加判断，如果滑动的区域是tableIndex=0对应的区域，则不改变tablayout的状态
                        tabLayout.selectTab(tabLayout.getTabAt(0));
                    }
                } else if (scrollY >= sb_comment.getTop() && scrollY < sb_movie.getTop()) {
                    if (tabIndex != 1) {
                        tabLayout.selectTab(tabLayout.getTabAt(1));
                    }
                } else if (scrollY >= sb_movie.getTop()) {
                    if (tabIndex != 2) {
                        tabLayout.selectTab(tabLayout.getTabAt(2));
                    }
                }
                scrollviewFlag = false;
            }
        });
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
                sb_number.setText("Total of "+String.valueOf(result.getData().getViewCount())+" people rated");
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

        EasyHttp.get(this).api((IRequestApi) new MovieRateApi().setMovieId(movieId).setPage(commentAdapter.getPageNumber()).setPageSize(3)).request(new HttpCallback<HttpData<List<RateBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<RateBean>> result) {
                super.onSucceed(result);
                commentAdapter.setData(result.getData());
            }
        });
        EasyHttp.get(this).api((IRequestApi) new MovieLikeThisApi().setMovieId(movieId).setNum(6)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                sametypeAdapter.setData(result.getData());
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_seen:
                if(!OnClickHelper.hasRate(movieId,(AppCompatActivity)getActivity())){
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                    // 判断是否已登录
                    String token = sharedPreferences.getString(getString(R.string.user_token), null);

                    if (token == null || token.isEmpty()) {
                        toast("please login");
                        return;
                    }

                    Intent intent = new Intent(getContext(), RateActivity.class);
                    intent.putExtra("movieId", movieId);
                    startActivity(intent);
                }

                break;
            case R.id.bt_favorite:
                OnClickHelper.onClickFavorite(bt_favorite,movieId,(AppCompatActivity)getActivity());
                break;
            case R.id.sb_comment:
                Intent intent = new Intent(getContext(), CommentListActivity.class);
                intent.putExtra("movieId", movieId);
                startActivity(intent);
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
       if(childView.getId()==R.id.iv_good){
           EasyHttp.post(this)
                   .api(new OnLikeApi().setToId(commentAdapter.getItem(position).getId()).setType(3))
                   .request(new HttpCallback<HttpData<Boolean>>(this) {

                       @Override
                       public void onSucceed(HttpData<Boolean> data) {
                           if(data.getData()){
                               OnClickHelper.delOrAdd(commentAdapter.getItem(position).getId(),R.string.like_rate_id,false,getContext());
                               ((ImageView)childView).setImageDrawable(getDrawable( R.drawable.ic_good_on));
                           }else {
                               OnClickHelper.delOrAdd(commentAdapter.getItem(position).getId(),R.string.like_rate_id,true,getContext());
                               ((ImageView)childView).setImageDrawable(getDrawable( R.drawable.ic_good));
                           }
                       }
                   });
       }
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
package com.shq.movies.ui.fragment;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.hjq.base.BaseAdapter;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.SettingBar;
import com.hjq.widget.layout.WrapRecyclerView;
import com.shq.movies.R;
import com.shq.movies.common.MyFragment;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.OldMovieApi;
import com.shq.movies.http.request.QueryMovieApi;
import com.shq.movies.http.request.RandomMovieApi;
import com.shq.movies.http.request.RecommendByIdApi;
import com.shq.movies.http.request.RecommendUserMovieApi;
import com.shq.movies.http.request.ReviewApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.http.response.RecommendMovieBean;
import com.shq.movies.http.response.ReviewBean;
import com.shq.movies.ui.activity.HomeActivity;
import com.shq.movies.ui.activity.LastTimeActivity;
import com.shq.movies.ui.activity.MovieDetailActivity;
import com.shq.movies.ui.activity.QueryMovieActivity;
import com.shq.movies.ui.activity.RecomListActivity;
import com.shq.movies.ui.activity.ReviewDetailActivity;
import com.shq.movies.ui.adapter.ListAdapter;
import com.shq.movies.ui.adapter.MainReviewAdapter;
import com.shq.movies.ui.adapter.MainTopImgAdapter;
import com.shq.movies.ui.dialog.MenuDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;


public final class MainFragment extends MyFragment<HomeActivity>
        implements ViewPager.OnPageChangeListener, BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {
    private BGABanner banner_content;
    private EditText et_search;
    private ImageView iv_search;
    private SettingBar sb_recom;
    private SettingBar sb_last;

    private WrapRecyclerView rv_recom_movie_list;
    private WrapRecyclerView rv_recom_review_list;
    private WrapRecyclerView rv_lasttime;

    private ListAdapter listAdapter;
    private MainReviewAdapter reviewAdapter;
    private ListAdapter lastAdapter;

    private LinearLayout ll_btn_explore;
    private Button btn_explore;

    private long recMovieId=0;


    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {

        et_search = findViewById(R.id.et_search);
        iv_search = findViewById(R.id.iv_home_search);
        sb_recom = findViewById(R.id.sb_recom);
        sb_last = findViewById(R.id.sb_last);
        ll_btn_explore = findViewById(R.id.ll_btn_explore);
        btn_explore = findViewById(R.id.btn_explore);

        setOnClickListener(iv_search, sb_recom, sb_last, btn_explore);
        rv_recom_movie_list = findViewById(R.id.rv_recom_movie_list);
        listAdapter = new ListAdapter(getAttachActivity());
        rv_recom_movie_list.setLayoutManager(new LinearLayoutManager(getAttachActivity(), WrapRecyclerView.HORIZONTAL, false));
        listAdapter.setOnItemClickListener(this);
        listAdapter.setOnChildClickListener(R.id.iv_cover, this);
        rv_recom_movie_list.setAdapter(listAdapter);

        rv_recom_review_list = findViewById(R.id.rv_recom_review_list);
        reviewAdapter = new MainReviewAdapter(getAttachActivity());
        reviewAdapter.setOnItemClickListener(this);
        rv_recom_review_list.setAdapter(reviewAdapter);

        rv_lasttime = findViewById(R.id.rv_lasttime);
        lastAdapter = new ListAdapter(getAttachActivity());
        rv_lasttime.setLayoutManager(new LinearLayoutManager(getAttachActivity(), WrapRecyclerView.HORIZONTAL, false));
        lastAdapter.setOnItemClickListener(this);
        lastAdapter.setOnChildClickListener(R.id.iv_cover, this);
        rv_lasttime.setAdapter(lastAdapter);


        // 设置轮播
        banner_content = findViewById(R.id.banner_main);
    }

    @Override
    protected void initData() {
        this.getData();
        // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 200, 320, 200);
        // 设置数据源
        banner_content.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.drawable.top1,
                R.drawable.top2,
                R.drawable.top3);


    }

    private void getData() {

        EasyHttp.get(this).api(new RecommendUserMovieApi()).request(new HttpCallback<HttpData<RecommendMovieBean>>(this) {
            @Override
            public void onSucceed(HttpData<RecommendMovieBean> result) {
                super.onSucceed(result);

                if (result.getData().getList() != null && (!result.getData().getList().isEmpty())) {
                    listAdapter.setData(result.getData().getList());
                    recMovieId = result.getData().getMovieId();
                    ll_btn_explore.setVisibility(View.GONE);
                }
                toast(result.getData().getMovieId());
            }
        });

        EasyHttp.get(this).api((IRequestApi) new ReviewApi().setPage(reviewAdapter.getPageNumber()).setPageSize(10)).request(new HttpCallback<HttpData<List<ReviewBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<ReviewBean>> result) {
                super.onSucceed(result);
                reviewAdapter.setData(result.getData());
            }
        });

        EasyHttp.get(this).api((IRequestApi) new OldMovieApi().setPage(lastAdapter.getPageNumber()).setPageSize(10)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                lastAdapter.setData(result.getData());
            }
        });
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
    public void onResume() {
        super.onResume();
        this.getData();
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        if (recyclerView.getId() == R.id.rv_recom_movie_list) {
            this.routerToDetail(String.valueOf(listAdapter.getItem(position).getId()));
        } else if (recyclerView.getId() == R.id.rv_lasttime) {
            this.routerToDetail(String.valueOf(lastAdapter.getItem(position).getId()));
        } else if (recyclerView.getId() == R.id.rv_recom_review_list) {
            Intent intent = new Intent(getContext(), ReviewDetailActivity.class);
            intent.putExtra("reviewId", reviewAdapter.getItem(position).getId());
            startActivity(intent);
        }

    }

    public void routerToDetail(String movieId) {
        Intent intent = new Intent(getAttachActivity().getContext(), MovieDetailActivity.class);
        intent.putExtra("movieId", movieId);
        startActivity(intent);
    }

    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {
        if (recyclerView.getId() == R.id.rv_recom_movie_list) {
            this.routerToDetail(String.valueOf(listAdapter.getItem(position).getId()));
        } else if (recyclerView.getId() == R.id.rv_lasttime) {
            this.routerToDetail(String.valueOf(lastAdapter.getItem(position).getId()));
        }
    }

    public void recMovie() {
        EasyHttp.get(this).api(new RecommendByIdApi().setMovieId(recMovieId).setNum(5)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                listAdapter.setData(result.getData());
                ll_btn_explore.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_home_search) {
            if (!et_search.getText().toString().trim().isEmpty()) {
                // 跳转到搜索 Activity 并传参
                Intent intent = new Intent(getAttachActivity().getContext(), QueryMovieActivity.class);
                intent.putExtra("keyword", et_search.getText().toString().trim());
                startActivity(intent);
            }
        } else if (v.getId() == R.id.sb_recom) {
            if(recMovieId==0){
                toast("No data");
                return;
            }
            Intent intent = new Intent(getAttachActivity().getContext(), RecomListActivity.class);
            intent.putExtra("recMovieId", recMovieId);
            startActivity(intent);
        } else if (v.getId() == R.id.sb_last) {
            startActivity(LastTimeActivity.class);
        } else if (v.getId() == R.id.btn_explore) {
            EasyHttp.get(this).api(new RandomMovieApi().setNum(9)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
                @Override
                public void onSucceed(HttpData<List<MovieBean>> result) {
                    super.onSucceed(result);
                    // 居中选择框
                    new MenuDialog.Builder(getContext())
                            .setGravity(Gravity.CENTER)
                            // 设置 null 表示不显示取消按钮
                            //.setCancel(null)
                            // 设置点击按钮后不关闭对话框
                            //.setAutoDismiss(false)
                            .setList(result.getData())
                            .setTitle("Pick a movie you like")
                            .setListener(new MenuDialog.OnListener<MovieBean>() {

                                @Override
                                public void onSelected(BaseDialog dialog, int position, MovieBean movieBean) {
                                    recMovieId = movieBean.getId();
                                    recMovie();
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {

                                }
                            })
                            .show();
                }
            });


        }
    }

    public boolean isSwipeEnable() {
        return false;
    }
}
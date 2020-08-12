package com.shq.movies.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.AddCollectApi;
import com.shq.movies.http.request.DeleteCollectApi;
import com.shq.movies.http.request.QueryMovieApi;
import com.shq.movies.http.request.RecommendByIdApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.ui.adapter.MovieListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class RecomListActivity extends MyActivity
        implements ViewPager.OnPageChangeListener, BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {

    private WrapRecyclerView rv_recom;
    private MovieListAdapter movieListAdapter;
    private long movieId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recom_movielist;
    }

    @Override
    protected void initView() {
        Intent intent=getIntent();
        movieId=intent.getLongExtra("recMovieId",0);
        rv_recom = findViewById(R.id.rv_recom);

        movieListAdapter = new MovieListAdapter(getActivity());
        movieListAdapter.setOnItemClickListener(this);
        movieListAdapter.setOnChildClickListener(R.id.bt_favorite, this);
        rv_recom.setAdapter(movieListAdapter);
    }

    @Override
    protected void initData() {
        this.getData();
    }

    private void getData() {

        EasyHttp.get(this).api(new RecommendByIdApi().setMovieId(movieId).setNum(10)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                movieListAdapter.setData(result.getData());
            }
        });
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        if (recyclerView.getId() == R.id.rv_recom) {
            this.routerToDetail(String.valueOf(movieListAdapter.getItem(position).getId()));
        }
    }

        public void routerToDetail (String movieId){
            Intent intent = new Intent(getContext(), MovieDetailActivity.class);
            intent.putExtra("movieId", movieId);
            startActivity(intent);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onChildClick (RecyclerView recyclerView, View childView,int position){
            switch (childView.getId()) {
                case R.id.bt_favorite:
                    onClickFavorite((ImageButton) childView, position);
                    break;
                case R.id.rv_recom:
                    this.routerToDetail(String.valueOf(movieListAdapter.getItem(position).getId()));
                    break;
                default:
                    toast(((TextView) childView).getText());
                    break;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private void onClickFavorite (ImageButton bt_favor,int position){
            // 判断保存的 id 是否存在
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
            String favorId = sharedPreferences.getString(getString(R.string.favorite_movie_id), null);
            boolean hasFavor = false;
            if (favorId != null && !favorId.isEmpty()) {
                List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));
                hasFavor = ids.contains(String.valueOf(movieListAdapter.getItem(position).getId()));
            }

            // 判断是否已登录
            String token = sharedPreferences.getString(getString(R.string.user_token), null);

            if (token == null || token.isEmpty()) {
                toast("please login");
                return;
            }

            if (!hasFavor) {
                EasyHttp.post(this)
                        .api(new AddCollectApi().setFavoriteId(movieListAdapter.getItem(position).getId()).setType(1))
                        .request(new HttpCallback<HttpData<Boolean>>(this) {

                            @Override
                            public void onSucceed(HttpData<Boolean> data) {
                                bt_favor.setImageResource(R.drawable.ic_collect_2);
                                List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));

                                ids.add(String.valueOf(movieListAdapter.getItem(position).getId()));
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(getString(R.string.favorite_movie_id), ids.stream().map(i -> i.toString()).collect(Collectors.joining("|")));
                                editor.commit();
                            }
                        });
            } else {
                EasyHttp.post(this)
                        .api(new DeleteCollectApi().setId(movieListAdapter.getItem(position).getId()).setType(1))
                        .request(new HttpCallback<HttpData<Boolean>>(this) {

                            @Override
                            public void onSucceed(HttpData<Boolean> data) {
                                bt_favor.setImageResource(R.drawable.ic_collect_1);
                                List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));

                                ids.remove(String.valueOf(movieListAdapter.getItem(position).getId()));
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(getString(R.string.favorite_movie_id), ids.stream().map(i -> i.toString()).collect(Collectors.joining("|")));
                                editor.commit();

                            }
                        });
            }
        }

        @Override
        public void onPageScrolled ( int position, float positionOffset, int positionOffsetPixels){

        }

        @Override
        public void onPageSelected ( int position){

        }

        @Override
        public void onPageScrollStateChanged ( int state){

        }


    }
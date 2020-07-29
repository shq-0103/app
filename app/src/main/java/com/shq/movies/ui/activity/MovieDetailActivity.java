package com.shq.movies.ui.activity;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.MovieDetailApi;
import com.shq.movies.http.request.UserApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.http.response.UserInfoBean;

import org.greenrobot.eventbus.EventBus;

public final class MovieDetailActivity extends MyActivity {
    private ImageView iv_movie_cover;
    private TextView tv_movie_title;
    private TextView tv_movie_type;
    private TextView tv_movie_time;
    private Button bt_favorite;
    private TextView tv_score;

    @Override
    protected int getLayoutId() {
        return R.layout.acticity_moviedetail;
    }

    @Override
    protected void initView() {
        iv_movie_cover =findViewById(R.id.iv_movie_cover);
        tv_movie_title = findViewById(R.id.tv_movie_title);
        tv_movie_type = findViewById(R.id.tv_movie_type);
        tv_movie_time = findViewById(R.id.tv_movie_time);
        bt_favorite = findViewById(R.id.bt_favorite);
        tv_score = findViewById(R.id.tv_score);
        setOnClickListener(bt_favorite);
    }

    @Override
    protected void initData() {
        EasyHttp.get(this).api(new MovieDetailApi().setId(1)).request(new HttpCallback<HttpData<MovieBean>>(this) {
            @Override
            public void onSucceed(HttpData<MovieBean> result) {

                super.onSucceed(result);
                tv_movie_title.setText(result.getData().getName());
                tv_movie_type.setText(result.getData().getGenres());
                tv_movie_time.setText(result.getData().getReleaseDate());
                tv_score.setText(result.getData().getScore());
                GlideApp.with(getActivity())
                        .load(result.getData().getCover())
                        .placeholder(R.drawable.ic_movie_placeholder)
                        .error(R.drawable.ic_movie_placeholder)
                        .into(iv_movie_cover);
            }
        });
    }
}
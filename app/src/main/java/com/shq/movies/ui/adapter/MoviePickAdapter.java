package com.shq.movies.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shq.movies.R;
import com.shq.movies.common.MyAdapter;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.response.MovieBean;

import java.util.Arrays;
import java.util.List;

public final class MoviePickAdapter extends MyAdapter<MovieBean> {

    public MoviePickAdapter(Context context) {
        super(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends MyAdapter.ViewHolder {

        private ImageView iv_movie;
        private TextView tv_movie_name;
        private TextView tv_movie_type;
        private TextView tv_movie_score;

        private ViewHolder() {
            super(R.layout.item_movie_pick);
            iv_movie=(ImageView)findViewById(R.id.iv_movie_cover);
            tv_movie_name = (TextView)findViewById(R.id.tv_movie_name);
            tv_movie_type = (TextView)findViewById(R.id.tv_movie_type);
            tv_movie_score =(TextView)findViewById(R.id.tv_movie_score);
        }

        @Override
        public void onBindView(int position) {
            MovieBean movieBean= getItem(position);
            GlideApp.with(getContext())
                    .load(getItem(position).getCover())
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.ic_movie_placeholder)
                    .into(iv_movie);
            tv_movie_name.setText(getItem(position).getName());
            tv_movie_type.setText(getItem(position).getGenres());
            tv_movie_score.setText(String.valueOf(getItem(position).getScore()));
        }
    }
}
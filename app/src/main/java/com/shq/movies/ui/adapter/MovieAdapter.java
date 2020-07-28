package com.shq.movies.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shq.movies.R;
import com.shq.movies.common.MyAdapter;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.response.MovieBean;

public final class MovieAdapter extends MyAdapter<MovieBean> {

    public MovieAdapter(Context context) {
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
        private TextView tv_movie_date;
        private TextView tv_movie_type;

        private ViewHolder() {
            super(R.layout.item_movie);
            iv_movie=(ImageView)findViewById(R.id.iv_movie_cover);
            tv_movie_name = (TextView)findViewById(R.id.tv_movie_name);
            tv_movie_date = (TextView)findViewById(R.id.tv_movie_date);
            tv_movie_type = (TextView)findViewById(R.id.tv_movie_type);
        }

        @Override
        public void onBindView(int position) {
            int c=getItemCount();
            MovieBean movieBean= getItem(position);
            if(c<=0){
                return;
            }
            GlideApp.with(getContext())
                    .load(getItem(position).getCover())
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.ic_movie_placeholder)
                    .into(iv_movie);
            tv_movie_name.setText(getItem(position).getName());
            tv_movie_date.setText(getItem(position).getReleaseDate());
            tv_movie_type.setText(getItem(position).getGenres());
        }
    }
}
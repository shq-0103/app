package com.shq.movies.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shq.movies.R;
import com.shq.movies.common.MyAdapter;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.response.MovieBean;


public final class FindAdapter extends MyAdapter<MovieBean> {

    public FindAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends MyAdapter.ViewHolder {
        private TextView tv_postion;
        private ImageView iv_movie_cover;
        private TextView tv_movie_name;
        private TextView tv_vision;

        private ViewHolder() {
            super(R.layout.item_find);
            tv_postion =(TextView)findViewById(R.id.tv_postion);
            iv_movie_cover = (ImageView)findViewById(R.id.iv_movie_cover);
            tv_movie_name = (TextView)findViewById(R.id.tv_movie_name);
            tv_vision = (TextView)findViewById(R.id.tv_vision);
        }

        @Override
        public void onBindView(int position) {
            GlideApp.with(getContext())
                    .load(getItem(position).getCover())
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.ic_movie_placeholder)
                    .into(iv_movie_cover);

            tv_vision.setText(String.valueOf(getItem(position).getScore()));
            tv_postion.setText(String.valueOf(position+1));
            tv_movie_name.setText(getItem(position).getName());
        }
    }
}
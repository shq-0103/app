package com.shq.movies.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shq.movies.R;
import com.shq.movies.common.MyAdapter;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.http.response.ReviewBean;

public final class ListAdapter extends MyAdapter<MovieBean> {

    public ListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends MyAdapter.ViewHolder {

        private ImageView iv_cover;
        private ImageButton ib_collect;
        private TextView tv_title;
        private TextView tv_rate;
        private ImageView iv_good;
        private TextView tv_detail;

        private ViewHolder() {
            super(R.layout.item_list);
            iv_cover = (ImageView)findViewById(R.id.iv_cover);
            ib_collect =(ImageButton) findViewById(R.id.ib_collect);
            tv_title =(TextView)findViewById(R.id.tv_title);
            tv_rate = (TextView)findViewById(R.id.tv_rate);
            iv_good = (ImageView)findViewById(R.id.iv_good);
            tv_detail = (TextView)findViewById(R.id.tv_detail);
        }

        @Override
        public void onBindView(int position) {

            GlideApp.with(getContext())
                    .load(getItem(position).getCover())
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.ic_movie_placeholder)
                    .into(iv_cover);
            tv_title.setText(getItem(position).getName());
        }
    }
}
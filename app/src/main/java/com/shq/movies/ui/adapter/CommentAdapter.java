package com.shq.movies.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shq.movies.R;
import com.shq.movies.common.MyAdapter;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.response.CommentBean;
import com.shq.movies.http.response.RateBean;

public final class CommentAdapter extends MyAdapter<RateBean> {

    public CommentAdapter(Context context) {
        super(context);
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends MyAdapter.ViewHolder {
        private ImageView iv_cover;
        private TextView tv_author;
        private TextView tv_date;
        private TextView tv_rate;
        private TextView tv_detail;


        private ViewHolder() {
            super(R.layout.item_comment);
            iv_cover = (ImageView)findViewById(R.id.iv_cover);
            tv_author = (TextView)findViewById(R.id.tv_author);
            tv_date = (TextView)findViewById(R.id.tv_date);
            tv_rate = (TextView)findViewById(R.id.tv_rate);
            tv_detail = (TextView)findViewById(R.id.tv_detail);
        }

        @Override
        public void onBindView(int position) {
            tv_author.setText(getItem(position).getNickname());
            tv_rate.setText(String.valueOf(getItem(position).getRate()));
            tv_detail.setText(getItem(position).getContents());
            GlideApp.with(getContext())
                    .load(getItem(position).getAvatar())
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.ic_movie_placeholder)
                    .into(iv_cover);
        }
    }
}
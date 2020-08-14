package com.shq.movies.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shq.movies.R;
import com.shq.movies.common.MyAdapter;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.response.RateBean;

import java.text.DecimalFormat;

public final class RatingAdapter extends MyAdapter<RateBean> {

    public RatingAdapter(Context context) {
        super(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends MyAdapter.ViewHolder {
        private ImageView iv_cover;
        private TextView tv_name;
        private TextView tv_author;
        private TextView tv_rate;
        private TextView tv_all_rate;
        private TextView tv_userRate;

        private ViewHolder() {
            super(R.layout.item_rating);
            iv_cover = (ImageView)findViewById(R.id.iv_cover);
            tv_name = (TextView)findViewById(R.id.tv_name);
            tv_author = (TextView)findViewById(R.id.tv_author);
            tv_rate =(TextView)findViewById(R.id.tv_rate);
            tv_all_rate =(TextView)findViewById(R.id.tv_all_rate);
            tv_userRate =(TextView)findViewById(R.id.tv_userRate);
        }

        @Override
        public void onBindView(int position) {
            GlideApp.with(getContext())
                    .load(getItem(position).getCover())
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.ic_movie_placeholder)
                    .into(iv_cover);
            DecimalFormat decimalFormat=new DecimalFormat(".00");
            tv_rate.setText(decimalFormat.format(getItem(position).getScore()));
            tv_name.setText(getItem(position).getName());
            tv_userRate.setText(String.valueOf(getItem(position).getRate()));
            tv_all_rate.setText(String.valueOf(getItem(position).getViewCount())+"people");
        }
    }
}
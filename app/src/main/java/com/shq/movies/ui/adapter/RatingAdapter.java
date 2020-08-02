package com.shq.movies.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shq.movies.R;
import com.shq.movies.common.MyAdapter;
import com.shq.movies.http.response.RateBean;

public final class RatingAdapter extends MyAdapter<RateBean> {

    public RatingAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemCount() {
        return 10;
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

        }
    }
}
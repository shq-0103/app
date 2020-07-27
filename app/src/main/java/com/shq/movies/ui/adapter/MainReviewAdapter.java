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
import com.shq.movies.http.response.ReviewBean;

public final class MainReviewAdapter extends MyAdapter<ReviewBean> {

    public MainReviewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends MyAdapter.ViewHolder {
        private  TextView tv_title;
        private ImageView iv_review;
        private TextView tv_author;
        private  TextView tv_comment;
        private  TextView tv_date;

        private ViewHolder() {
            super(R.layout.item_reviewslist);
            tv_title =(TextView) findViewById(R.id.tv_title);
            iv_review = (ImageView) findViewById(R.id.iv_review);
            tv_author = (TextView) findViewById(R.id.tv_author);
            tv_comment = (TextView) findViewById(R.id.tv_comment);
            tv_date = (TextView) findViewById(R.id.tv_date);
        }

        @Override
        public void onBindView(int position) {
            int c=getItemCount();
            ReviewBean reviewBean= getItem(position);
            if(c<=0){
                return;
            }
            tv_title.setText(getItem(position).getTitle());
        }
        }
    }

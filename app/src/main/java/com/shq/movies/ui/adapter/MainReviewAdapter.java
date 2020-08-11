package com.shq.movies.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hjq.http.EasyConfig;
import com.shq.movies.R;
import com.shq.movies.common.MyAdapter;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.http.response.ReviewBean;

import java.text.SimpleDateFormat;
import java.util.Date;

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
            int c = getItemCount();
            ReviewBean reviewBean = getItem(position);
            if (c <= 0) {
                return;
            }
            tv_title.setText(getItem(position).getTitle());
            GlideApp.with(getContext())
                    .load(EasyConfig.getInstance().getServer().getHost() + reviewBean.getAvatar())
                    .error(R.drawable.avatar_placeholder_ic)
                    .circleCrop()
                    .into(iv_review);


            if(TextUtils.isEmpty(reviewBean.getImages())){
                iv_review.setVisibility(View.GONE);
            }else {
                GlideApp.with(getContext())
                        .load(EasyConfig.getInstance().getServer().getHost() + reviewBean.getImages())
                        .error(R.drawable.ic_movie_placeholder)
                        .into(iv_review);
            }
            // Date转String
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tv_date.setText(sdf.format(new Date(reviewBean.getDate() * 1000)));
            tv_author.setText(reviewBean.getNickname());
            tv_comment.setText(String.valueOf(reviewBean.getCommentNum()));
        }
        }
    }

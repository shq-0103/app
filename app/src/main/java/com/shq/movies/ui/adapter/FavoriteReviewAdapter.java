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
import com.shq.movies.http.response.ReviewBean;

import java.text.SimpleDateFormat;
import java.util.Date;


public final class FavoriteReviewAdapter extends MyAdapter<ReviewBean> {

    public FavoriteReviewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends MyAdapter.ViewHolder {

        private ImageView iv_cover;
        private TextView tv_title;
        private TextView tv_author;
        private TextView tv_date;

        private ViewHolder() {
            super(R.layout.item_favoritereview);
            iv_cover = (ImageView) findViewById(R.id.iv_cover);
            tv_title = (TextView) findViewById(R.id.tv_title);
            tv_author = (TextView)findViewById(R.id.tv_author);
            tv_date = (TextView)findViewById(R.id.tv_date);
        }

        @Override
        public void onBindView(int position) {
            int c=getItemCount();
            ReviewBean reviewBean= getItem(position);
            if(c<=0){
                return;
            }
            GlideApp.with(getContext())
                    .load(EasyConfig.getInstance().getServer().getHost() + reviewBean.getAvatar())
                    .error(R.drawable.avatar_placeholder_ic)
                    .circleCrop()
                    .into(iv_cover);


            if(TextUtils.isEmpty(reviewBean.getImages())){
                iv_cover.setVisibility(View.GONE);
            }else {
                GlideApp.with(getContext())
                        .load(EasyConfig.getInstance().getServer().getHost() + reviewBean.getImages())
                        .error(R.drawable.ic_movie_placeholder)
                        .into(iv_cover);
            }
            tv_title.setText(getItem(position).getTitle());
            tv_author.setText(getItem(position).getNickname());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tv_date.setText(sdf.format(new Date(reviewBean.getDate() * 1000)));
        }
    }
}
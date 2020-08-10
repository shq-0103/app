package com.shq.movies.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hjq.http.EasyConfig;
import com.shq.movies.R;
import com.shq.movies.common.MyAdapter;
import com.shq.movies.helper.OnClickHelper;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.response.CommentBean;
import com.shq.movies.http.response.RateBean;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        private ImageView iv_good;

        private TextView tv_author;
        private TextView tv_date;
        private TextView tv_rate;
        private TextView tv_detail;


        private ViewHolder() {
            super(R.layout.item_comment);
            iv_good= (ImageView)findViewById(R.id.iv_good);
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
            if(OnClickHelper.hasLike(getItem(position).getId(),(AppCompatActivity) getContext(),3)){
                iv_good.setImageDrawable(getDrawable( R.drawable.ic_good_on));
            }else {
                iv_good.setImageDrawable(getDrawable( R.drawable.ic_good));
            }
            GlideApp.with(getContext())
                    .load(EasyConfig.getInstance().getServer().getHost() +getItem(position).getAvatar())
                    .placeholder(R.drawable.avatar_placeholder_ic)
                    .error(R.drawable.avatar_placeholder_ic)
                    .circleCrop()
                    .into(iv_cover);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            tv_date.setText(sdf.format(new Date(getItem(position).getTime() * 1000)));
        }
    }
}
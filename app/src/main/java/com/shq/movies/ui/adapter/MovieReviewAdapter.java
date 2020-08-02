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
import com.shq.movies.http.response.ReviewBean;

public final class MovieReviewAdapter extends MyAdapter<ReviewBean> {

    public MovieReviewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends MyAdapter.ViewHolder {
        private ImageView iv_userimg;
        private TextView tv_author;
        private TextView tv_date;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_good;
        private TextView tv_comment;


        private ViewHolder() {
            super(R.layout.item_review);
            iv_userimg= (ImageView)findViewById(R.id.iv_userimg);
            tv_author = (TextView)findViewById(R.id.tv_author);
            tv_date = (TextView)findViewById(R.id.tv_date);
            tv_title = (TextView)findViewById(R.id.tv_title);
            tv_content = (TextView)findViewById(R.id.tv_content);
            tv_good = (TextView)findViewById(R.id.tv_good);
            tv_comment = (TextView)findViewById(R.id.tv_comment);

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
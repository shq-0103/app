package com.shq.movies.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shq.movies.R;
import com.shq.movies.common.MyAdapter;


public final class ReviewCommentAdapter extends MyAdapter<String> {

    public ReviewCommentAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends MyAdapter.ViewHolder {
        private ImageView ig_commentimg;
        private TextView tv_comment_author;
        private TextView tv_comment_date;
        private ImageView comment_good;
        private TextView tv_comment_content;

        private ViewHolder() {
            super(R.layout.item_reviewcomment);
            ig_commentimg = (ImageView)findViewById(R.id.ig_commentimg);
            tv_comment_author = (TextView)findViewById(R.id.tv_comment_author);
            tv_comment_date = (TextView)findViewById(R.id.tv_comment_date);
            comment_good = (ImageView)findViewById(R.id.comment_good);
            tv_comment_content = (TextView)findViewById(R.id.tv_comment_content);
        }

        @Override
        public void onBindView(int position) {

        }
    }
}
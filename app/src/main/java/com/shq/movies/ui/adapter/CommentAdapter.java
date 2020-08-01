package com.shq.movies.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shq.movies.R;
import com.shq.movies.common.MyAdapter;
import com.shq.movies.http.response.CommentBean;

public final class CommentAdapter extends MyAdapter<CommentBean> {

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


        private ViewHolder() {
            super(R.layout.item_comment);
            iv_cover = (ImageView)findViewById(R.id.iv_cover);
            tv_author = (TextView)findViewById(R.id.tv_author);
            tv_date = (TextView)findViewById(R.id.tv_date);
        }

        @Override
        public void onBindView(int position) {

        }
    }
}
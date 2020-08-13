package com.shq.movies.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shq.movies.R;
import com.shq.movies.common.MyAdapter;
import com.shq.movies.http.response.MessageBean;

import java.text.SimpleDateFormat;
import java.util.Date;


public final class MessageAdapter extends MyAdapter<MessageBean> {

    public MessageAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends MyAdapter.ViewHolder {

        private TextView tv_title;
        private TextView tv_date;
        private TextView tv_content;

        private ViewHolder() {
            super(R.layout.item_message);
            tv_title = (TextView)findViewById(R.id.tv_title);
            tv_date = (TextView)findViewById(R.id.tv_date);
            tv_content = (TextView)findViewById(R.id.tv_content);
        }

        @Override
        public void onBindView(int position) {
            tv_content.setText(getItem(position).getContent());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            tv_date.setText(sdf.format(new Date(getItem(position).getTime() * 1000)));
        }
    }
}
package com.shq.movies.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hjq.http.EasyConfig;
import com.shq.movies.R;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.common.MyAdapter;
import com.shq.movies.http.response.MovieBean;

public final class SametypeAdapter extends MyAdapter<MovieBean> {

    public SametypeAdapter(Context context) {
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


            private ViewHolder() {
                super(R.layout.item_list);
                iv_cover = (ImageView) findViewById(R.id.iv_cover);
                tv_title = (TextView) findViewById(R.id.tv_title);
            }

            @Override
            public void onBindView(int position) {
                tv_title.setText(getItem(position).getName());
                GlideApp.with(getContext())
                        .load(EasyConfig.getInstance().getServer().getHost() + getItem(position).getCover())
                        .placeholder(R.drawable.avatar_placeholder_ic)
                        .error(R.drawable.avatar_placeholder_ic)
                        .circleCrop()
                        .into(iv_cover);

            }
        }
    }


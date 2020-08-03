package com.shq.movies.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.shq.movies.R;
import com.shq.movies.common.MyAdapter;

public final class SelectListAdapter extends MyAdapter<String> {

    public SelectListAdapter(Context context) {
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

        private ViewHolder() {
            super(R.layout.item_selectlist);
        }

        @Override
        public void onBindView(int position) {

        }
    }
}
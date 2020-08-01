package com.shq.movies.ui.activity;

import androidx.core.content.ContextCompat;

import com.shq.movies.R;
import com.shq.movies.action.StatusAction;
import com.shq.movies.common.MyActivity;
import com.shq.movies.ui.dialog.MenuDialog;
import com.shq.movies.widget.HintLayout;
public final class FavoriteReviewActivity extends MyActivity
        implements StatusAction {

    private HintLayout mHintLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userinfo_reviews;
    }

    @Override
    protected void initView() {
        mHintLayout = findViewById(R.id.hl_status_hint);
    }

    @Override
    protected void initData() {
    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }
}
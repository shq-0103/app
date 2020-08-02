package com.shq.movies.ui.activity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.shq.movies.R;
import com.shq.movies.common.MyActivity;

public final class RateActivity extends MyActivity {
    private RatingBar rb_rating;
    private EditText et_input;
    private Button bt_submit;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_rating;
    }

    @Override
    protected void initView() {
        rb_rating = findViewById(R.id.rb_rating);
        et_input = findViewById(R.id.et_input);
        bt_submit = findViewById(R.id.bt_submit);
    }

    @Override
    protected void initData() {

    }
}
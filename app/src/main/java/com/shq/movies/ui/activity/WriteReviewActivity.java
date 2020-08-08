package com.shq.movies.ui.activity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.shq.movies.R;
import com.shq.movies.common.MyActivity;


public final class WriteReviewActivity extends MyActivity {
    private EditText et_title;
    private EditText et_content;
    private ImageView iv_img;
    private Button bt_submit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_writereview;
    }

    @Override
    protected void initView() {
        et_title= findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        iv_img = findViewById(R.id.iv_img);
        bt_submit = findViewById(R.id.bt_submit);
    }

    @Override
    protected void initData() {

    }
}
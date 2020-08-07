package com.shq.movies.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.hjq.base.BaseDialog;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.ui.dialog.MessageDialog;

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
        setOnClickListener(bt_submit);
    }

    @Override
    protected void initData() {

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                // 消息对话框
                new MessageDialog.Builder(this)
                        // 内容必须要填写
                        .setMessage("Confirm submission?")
                        // 确定按钮文本
                        .setConfirm(getString(R.string.common_confirm))
                        // 设置 null 表示不显示取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setListener(new MessageDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                toast("Corfirm");
                                onBackPressed();
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("Cancel");
                            }
                        })
                        .show();
                break;
        }
    }

    private void rate(){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
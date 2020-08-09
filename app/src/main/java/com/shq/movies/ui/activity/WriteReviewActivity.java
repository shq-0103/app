package com.shq.movies.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.SettingBar;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.helper.InputTextHelper;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.CollectMovieApi;
import com.shq.movies.http.request.MovieReviewApi;
import com.shq.movies.http.request.ReviewApi;
import com.shq.movies.http.request.ReviewImageApi;
import com.shq.movies.http.request.UpdateImageApi;
import com.shq.movies.http.response.MovieBean;

import java.io.File;
import java.util.List;


public final class WriteReviewActivity extends MyActivity {
    private EditText et_title;
    private EditText et_content;
    private ImageView iv_img;
    private Button bt_submit;
    private SettingBar sb_img;
    private String imageStr;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_writereview;
    }

    @Override
    protected void initView() {
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        iv_img = findViewById(R.id.iv_img);
        bt_submit = findViewById(R.id.bt_submit);
        sb_img = findViewById(R.id.sb_review_img);

        setOnClickListener(R.id.iv_img,R.id.bt_submit,R.id.sb_review_img);

        InputTextHelper.with(this.getActivity())
                .addView(et_title)
                .addView(et_content)
                .setMain(bt_submit)
                .build();

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sb_review_img:
                ImageSelectActivity.start(this, data -> {
                    EasyHttp.post(this)
                            .api(new ReviewImageApi()
                                    .setImage(new File(data.get(0))))
                            .request(new HttpCallback<HttpData<String>>(WriteReviewActivity.this) {

                                @Override
                                public void onSucceed(HttpData<String> data) {
                                    imageStr =data.getData();
                                    GlideApp.with(getActivity())
                                            .load(EasyConfig.getInstance().getServer().getHost() + data.getData())
                                            .into(iv_img);
                                }
                            });
                });
                break;
            case R.id.iv_img:
                if (!TextUtils.isEmpty(imageStr)) {
                    // 查看头像
                    ImagePreviewActivity.start(getActivity(), EasyConfig.getInstance().getServer().getHost() +imageStr);
                } else {
                    // 选择头像
                    onClick(sb_img);
                }
                break;
            case R.id.bt_submit:
                submitReview();
                finish();
                break;
            default:
                break;
        }
    }

    private void submitReview(){
        EasyHttp.post(this).api( new MovieReviewApi()
                .setTitle(et_title.getText().toString())
                .setContents(et_content.getText().toString())
                .setImages(imageStr)
        ).request(new HttpCallback<HttpData<String>>(this) {
            @Override
            public void onSucceed(HttpData<String> result) {
                super.onSucceed(result);
            }
        });
    }
}
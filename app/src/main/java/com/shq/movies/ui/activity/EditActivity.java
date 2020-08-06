package com.shq.movies.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hjq.base.BaseDialog;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.SettingBar;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.RegisterApi;
import com.shq.movies.http.request.UpdateImageApi;
import com.shq.movies.http.request.UpdateUserApi;
import com.shq.movies.http.request.UserApi;
import com.shq.movies.http.request.UserInfoApi;
import com.shq.movies.http.response.UserInfoBean;
import com.shq.movies.ui.dialog.DateDialog;
import com.shq.movies.ui.dialog.InputDialog;
import com.shq.movies.ui.dialog.ResetPasswordDialog;
import com.shq.movies.ui.dialog.SelectDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

public final class EditActivity extends MyActivity {

    private ViewGroup mAvatarLayout;
    private ImageView mAvatarView;
    private String mAvatarUrl;
    private SettingBar sb_username;
    private SettingBar sb_nickname;
    private SettingBar sb_email;
    private SettingBar sb_tags;
    private SettingBar sb_birthday;
    private SettingBar sb_password;
    private SettingBar sb_intro;
    private SettingBar sb_gender;

    private UserInfoBean userInfo;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_userinfo;
    }

    @Override
    protected void initView() {
        mAvatarLayout = findViewById(R.id.fl_person_data_avatar);
        mAvatarView = findViewById(R.id.iv_person_data_avatar);
        sb_nickname = findViewById(R.id.sb_nickname);
        sb_email = findViewById(R.id.sb_email);
        sb_birthday = findViewById(R.id.sb_birthday);
        sb_password = findViewById(R.id.sb_password);
        sb_intro = findViewById(R.id.sb_intro);
        sb_gender = findViewById(R.id.sb_sex);
        sb_username = findViewById(R.id.sb_username);
        sb_tags = findViewById(R.id.sb_tags);
        userInfo=new UserInfoBean();

        setOnClickListener(mAvatarLayout, mAvatarView, sb_nickname, sb_email, sb_birthday, sb_intro, sb_password, sb_gender);
    }

    @Override
    protected void initData() {

        EasyHttp.get(this).api(new UserApi()).request(new HttpCallback<HttpData<UserInfoBean>>(this) {
            @Override
            public void onSucceed(HttpData<UserInfoBean> result) {
                super.onSucceed(result);
                userInfo = result.getData();
                sb_nickname.setRightText(result.getData().getNickname());
                sb_username.setRightText(userInfo.getUsername());
                sb_email.setRightText(userInfo.getEmail());
                sb_intro.setRightText(userInfo.getIntroduction());
                GlideApp.with(getActivity())
                        .load(EasyConfig.getInstance().getServer().getHost()+userInfo.getAvatar())
                        .placeholder(R.drawable.avatar_placeholder_ic)
                        .error(R.drawable.avatar_placeholder_ic)
                        .circleCrop()
                        .into(mAvatarView);
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
                // 跳转到登录页
                EventBus.getDefault().post(getString(R.string.event_login_fail));
            }
        });

    }

    private void updateUser(BaseDialog dialog,String content,SettingBar sb){

        UpdateUserApi updateUserApi = new UpdateUserApi()
                .setNickname(userInfo.getNickname())
                .setGender(userInfo.getGender())
                .setBirthday(userInfo.getBirthday())
                .setEmail(userInfo.getEmail())
                .setIntroduction(userInfo.getIntroduction())
                .setAvatar(userInfo.getAvatar());



        switch (sb.getId()){
            case R.id.sb_nickname:
                updateUserApi.setNickname(content);
                break;
            case R.id.sb_sex:
                updateUserApi.setGender(content);
                break;
            case R.id.sb_email:
                updateUserApi.setEmail(content);
                break;
            case R.id.sb_intro:
                updateUserApi.setEmail(content);
                break;
            default:
                break;
        }

        EasyHttp.post(this)
                .api(updateUserApi)
                .request(new HttpCallback<HttpData<Boolean>>(this) {

                    @Override
                    public void onSucceed(HttpData<Boolean> data) {
                        sb.setRightText(content);
                        dialog.dismiss();
                    }
                });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sb_password:
                new ResetPasswordDialog.Builder(getContext()).show();
                break;
            case R.id.fl_person_data_avatar:
                ImageSelectActivity.start(this, data -> {
//                    if (true) {
//                        mAvatarUrl = data.get(0);
//                        GlideApp.with(getActivity())
//                                .load(mAvatarUrl)
//                                .into(mAvatarView);
//                        return;
//                    }
                    // 上传头像
                    EasyHttp.post(this)
                            .api(new UpdateImageApi()
                                    .setImage(new File(data.get(0))))
                            .request(new HttpCallback<HttpData<String>>(EditActivity.this) {

                                @Override
                                public void onSucceed(HttpData<String> data) {
                                    mAvatarUrl = EasyConfig.getInstance().getServer().getHost()+data.getData();
                                    GlideApp.with(getActivity())
                                            .load(mAvatarUrl)
                                            .into(mAvatarView);
                                }
                            });
                });
                break;
            case R.id.iv_person_data_avatar:
                if (!TextUtils.isEmpty(mAvatarUrl)) {
                    // 查看头像
                    ImagePreviewActivity.start(getActivity(), mAvatarUrl);
                } else {
                    // 选择头像
                    onClick(mAvatarLayout);
                }
                break;
            case R.id.sb_nickname:
                new InputDialog.Builder(this)
                        // 标题可以不用填写
                        .setTitle(getString(R.string.personal_data_name_hint))
                        .setContent(sb_nickname.getRightText())
                        //.setHint(getString(R.string.personal_data_name_hint))
                        //.setConfirm("确定")
                        // 设置 null 表示不显示取消按钮
                        //.setCancel("取消")
                        // 设置点击按钮后不关闭对话框
                        .setAutoDismiss(false)
                        .setListener(new InputDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                    if (!sb_nickname.getRightText().equals(content)) {
                                        updateUser(dialog,content,sb_nickname);
                                    }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.sb_sex:
                // 单选对话框
                new SelectDialog.Builder(this)
                        .setTitle("Choose your gender")
                        .setList("female", "male", "secret")
                        // 设置单选模式
                        .setSingleSelect()
                        // 设置默认选中
                        .setSelect(0)
                        .setListener(new SelectDialog.OnListener<String>() {
                            @Override
                            public void onSelected(BaseDialog dialog, HashMap<Integer, String> data) {
                                toast("确定了：" + data.toString());
                            }
                        })
                        .show();
                break;
            case R.id.sb_birthday:
                // 日期选择对话框
                new DateDialog.Builder(this)
                        .setTitle(getString(R.string.date_title))
                        // 确定按钮文本
                        .setConfirm(getString(R.string.common_confirm))
                        // 设置 null 表示不显示取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        // 设置日期
                        //.setDate("2018-12-31")
                        //.setDate("20181231")
                        //.setDate(1546263036137)
                        // 设置年份
                        //.setYear(2018)
                        // 设置月份
                        //.setMonth(2)
                        // 设置天数
                        //.setDay(20)
                        // 不选择天数
                        //.setIgnoreDay()
                        .setListener(new DateDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, int year, int month, int day) {
                                toast(year + getString(R.string.common_year) + month + getString(R.string.common_month) + day + getString(R.string.common_day));

                                // 如果不指定时分秒则默认为现在的时间
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                // 月份从零开始，所以需要减 1
                                calendar.set(Calendar.MONTH, month - 1);
                                calendar.set(Calendar.DAY_OF_MONTH, day);
                                toast("时间戳：" + calendar.getTimeInMillis());
                                //toast(new SimpleDateFormat("yyyy年MM月dd日 kk:mm:ss").format(calendar.getTime()));
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        })
                        .show();
                break;
            case R.id.sb_email:
                new InputDialog.Builder(this)
                        // 标题可以不用填写
                        .setTitle(getString(R.string.personal_data_email_hint))
                        .setContent(sb_email.getRightText())
                        //.setHint(getString(R.string.personal_data_name_hint))
                        //.setConfirm("确定")
                        // 设置 null 表示不显示取消按钮
                        //.setCancel("取消")
                        // 设置点击按钮后不关闭对话框
                        .setAutoDismiss(false)
                        .setListener(new InputDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                if (!sb_email.getRightText().equals(content)) {
                                    updateUser(dialog,content,sb_email);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.sb_intro:
                new InputDialog.Builder(this)
                        // 标题可以不用填写
                        .setTitle(getString(R.string.personal_data_intro_hint))
                        .setContent(sb_intro.getRightText())
                        //.setHint(getString(R.string.personal_data_name_hint))
                        //.setConfirm("确定")
                        // 设置 null 表示不显示取消按钮
                        //.setCancel("取消")
                        // 设置点击按钮后不关闭对话框
                        .setAutoDismiss(false)
                        .setListener(new InputDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                if (!sb_intro.getRightText().equals(content)) {
                                    updateUser(dialog,content,sb_intro);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.sb_tags:
                // 多选对话框
                new SelectDialog.Builder(this)
                        .setTitle("请选择工作日")
                        .setList("星期一", "星期二", "星期三", "星期四", "星期五")
                        // 设置最大选择数
                        .setMaxSelect(3)
                        // 设置默认选中
                        .setSelect(2, 3, 4)
                        .setListener(new SelectDialog.OnListener<String>() {

                            @Override
                            public void onSelected(BaseDialog dialog, HashMap<Integer, String> data) {
                                toast("确定了：" + data.toString());
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }
}
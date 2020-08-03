package com.shq.movies.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.base.BaseAdapter;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.SettingBar;
import com.hjq.widget.layout.WrapRecyclerView;
import com.shq.movies.R;
import com.shq.movies.common.MyFragment;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.CollectMovieApi;
import com.shq.movies.http.request.CollectMovieIdListApi;
import com.shq.movies.http.request.QueryMovieApi;
import com.shq.movies.http.request.ReviewApi;
import com.shq.movies.http.request.UserApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.http.response.ReviewBean;
import com.shq.movies.http.response.UserInfoBean;
import com.shq.movies.ui.activity.EditActivity;
import com.shq.movies.ui.activity.FavoriteActivity;
import com.shq.movies.ui.activity.FavoriteReviewActivity;
import com.shq.movies.ui.activity.HistoryActivity;
import com.shq.movies.ui.activity.HomeActivity;
import com.shq.movies.ui.activity.MessageActivity;
import com.shq.movies.ui.activity.MovieDetailActivity;
import com.shq.movies.ui.activity.MovieListActivity;
import com.shq.movies.ui.activity.MyMovieListActivity;
import com.shq.movies.ui.activity.RatingActivity;
import com.shq.movies.ui.adapter.ListAdapter;
import com.shq.movies.ui.dialog.MessageDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.stream.Collectors;

public final class MineFragment extends MyFragment<HomeActivity> implements BottomNavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener, BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener{

    private TextView tv_nickname;
    private TextView tv_intro;

    private SettingBar sb_sign_out;
    private SettingBar sb_modify_userinfo;
    private SettingBar sb_my_movielist;
    private SettingBar sb_recent_history;

    private SettingBar sb_about;

    private ImageView iv_avatar;
    private BottomNavigationView bv_user_info;
    private ImageButton bt_addmovie;
    private WrapRecyclerView rv_watch_list;
    private WrapRecyclerView rv_history_list;
    private ListAdapter watch_listAdapter;
    private ListAdapter history_listAdapter;


    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_intro = findViewById(R.id.tv_intro);
        sb_sign_out = findViewById(R.id.sb_setting_exit);
        sb_modify_userinfo = findViewById(R.id.sb_modify);
        sb_about = findViewById(R.id.sb_setting_about);
        iv_avatar = findViewById(R.id.iv_avatar);
        bv_user_info = findViewById(R.id.bv_user_info_navigation);
        sb_my_movielist = findViewById(R.id.sb_my_movielist);
        bt_addmovie = findViewById(R.id.bt_addmovie);
        sb_recent_history = findViewById(R.id.sb_recent_history);

        // 不使用图标默认变色
        bv_user_info.setItemIconTintList(null);
        bv_user_info.setOnNavigationItemSelectedListener(this);

        setOnClickListener(sb_sign_out, sb_modify_userinfo,sb_about,sb_my_movielist,bt_addmovie,sb_recent_history);

        rv_watch_list = findViewById(R.id.rv_watch_list);
        watch_listAdapter = new ListAdapter(getAttachActivity());
        rv_watch_list.setLayoutManager(new LinearLayoutManager(getAttachActivity(), WrapRecyclerView.HORIZONTAL, false));
        watch_listAdapter.setOnItemClickListener(this);
        rv_watch_list.setAdapter(watch_listAdapter);


        rv_history_list  = findViewById(R.id.rv_history_list );
        history_listAdapter = new ListAdapter(getAttachActivity());
        rv_history_list .setLayoutManager(new LinearLayoutManager(getAttachActivity(), WrapRecyclerView.HORIZONTAL, false));
        history_listAdapter.setOnItemClickListener(this);
        rv_history_list .setAdapter(history_listAdapter);
    }

    @Override
    protected void initData() {
        GlideApp.with(getActivity())
                .load(R.drawable.avatar_placeholder_ic)
                .placeholder(R.drawable.avatar_placeholder_ic)
                .error(R.drawable.avatar_placeholder_ic)
                .circleCrop()
                .into(iv_avatar);
        EasyHttp.get(this).api(new UserApi()).request(new HttpCallback<HttpData<UserInfoBean>>(this) {
            @Override
            public void onSucceed(HttpData<UserInfoBean> result) {
                super.onSucceed(result);
                tv_nickname.setText(result.getData().getNickname());
                tv_intro.setText(result.getData().getIntroduction());
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
                // 跳转到登录页
                EventBus.getDefault().post(getString(R.string.event_login_fail));
            }
        });

        EasyHttp.get(this).api(new CollectMovieIdListApi()).request(new HttpCallback<HttpData<List<Integer>>>(this) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSucceed(HttpData<List<Integer>> result) {
                super.onSucceed(result);

                //步骤1：创建一个SharedPreferences对象
                SharedPreferences sharedPreferences =getAttachActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                //步骤2： 实例化SharedPreferences.Editor对象
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String idS=result.getData().stream()
                        .map(i -> i.toString())
                        .collect(Collectors.joining("|"));
                //步骤3：将获取过来的值放入文件
                editor.putString(getString(R.string.favorite_movie_id), idS);
                //步骤4：提交
                editor.commit();
            }
        });
        this.getData();
    }
    private void getData() {

        EasyHttp.get(this).api((IRequestApi) new QueryMovieApi().setName(null).setPage(history_listAdapter.getPageNumber()).setPageSize(3)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                history_listAdapter.setData(result.getData());
            }
        });

        EasyHttp.get(this).api((IRequestApi) new CollectMovieApi().setPage(watch_listAdapter.getPageNumber()).setPageSize(2)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                watch_listAdapter.setData(result.getData());
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account_reviews:
                startActivity(FavoriteReviewActivity.class);
                return true;
            case R.id.account_rating:
                startActivity(RatingActivity.class);
                return true;
            case R.id.account_message:
                startActivity(MessageActivity.class);
                return true;
            case R.id.account_favorities:
                startActivity(MyMovieListActivity.class);
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sb_setting_exit:
                // 消息对话框
                new MessageDialog.Builder(getContext())
                        // 内容必须要填写
                        .setMessage("Confirm logout?")
                        // 确定按钮文本
                        .setConfirm("Confirm")
                        // 设置 null 表示不显示取消按钮
                        .setCancel("Cancel")
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setListener(new MessageDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                toast("Confirm");
                                //步骤1：创建一个SharedPreferences对象
                                SharedPreferences sharedPreferences = getAttachActivity().getSharedPreferences("data", getContext().MODE_PRIVATE);
                                //步骤2： 实例化SharedPreferences.Editor对象
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                // 跳转到登录页
                                EventBus.getDefault().post(getString(R.string.event_login_fail));
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("Cancel");
                            }
                        })
                        .show();

                break;
            case R.id.sb_modify:
                startActivity(EditActivity.class);
                break;
            case R.id.sb_my_movielist:
                startActivity(MyMovieListActivity.class);
                break;
            case R.id.sb_recent_history:
                startActivity(HistoryActivity.class);
                break;
            case R.id.bt_addmovie:
                startActivity(MovieListActivity.class);
                break;
            case R.id.sb_setting_about:
                startActivity(FavoriteActivity.class);
                break;

            default:
                break;
        }
    }

    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        if(recyclerView.getId()==R.id.rv_watch_list){
            this.routerToDetail(String.valueOf(watch_listAdapter.getItem(position).getId()));
        }else if(recyclerView.getId()==R.id.rv_history_list){
            this.routerToDetail(String.valueOf(history_listAdapter.getItem(position).getId()));
        }
    }

    public void  routerToDetail(String movieId){
        Intent intent = new Intent(getAttachActivity().getContext(), MovieDetailActivity.class);
        intent.putExtra("movieId", movieId);
        startActivity(intent);
    }

    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {
        if(recyclerView.getId()==R.id.rv_watch_list){
            this.routerToDetail(String.valueOf(watch_listAdapter.getItem(position).getId()));
        }else if(recyclerView.getId()==R.id.rv_history_list){
            this.routerToDetail(String.valueOf(history_listAdapter.getItem(position).getId()));
        }
    }
}
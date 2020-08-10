package com.shq.movies.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.base.BaseFragmentAdapter;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.common.MyFragment;
import com.shq.movies.helper.ActivityStackManager;
import com.shq.movies.helper.DoubleClickHelper;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.CollectMovieIdListApi;
import com.shq.movies.http.request.UserApi;
import com.shq.movies.http.response.IdListBean;
import com.shq.movies.http.response.UserInfoBean;
import com.shq.movies.other.KeyboardWatcher;
import com.shq.movies.ui.adapter.MainReviewAdapter;
import com.shq.movies.ui.fragment.FindFragment;
import com.shq.movies.ui.fragment.HomeFragment;
import com.shq.movies.ui.fragment.LoginFragment;
import com.shq.movies.ui.fragment.MainFragment;
import com.shq.movies.ui.fragment.MeFragment;
import com.shq.movies.ui.fragment.MessageFragment;
import com.shq.movies.ui.fragment.MineFragment;
import com.shq.movies.ui.fragment.MovieReviewFragment;
import com.shq.movies.ui.fragment.SearchFragment;
import com.shq.movies.ui.fragment.UserInfoFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.stream.Collectors;

public final class HomeActivity extends MyActivity
        implements KeyboardWatcher.SoftKeyboardStateListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;

    private BaseFragmentAdapter<MyFragment> mPagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.home_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }



    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.vp_home_pager);
        mBottomNavigationView = findViewById(R.id.bv_home_navigation);

        // 不使用图标默认变色
        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        KeyboardWatcher.with(this)
                .setListener(this);
    }

    @Override
    protected void initData() {

        SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        String token=sharedPreferences.getString(getString(R.string.user_token),null);
        EasyConfig.getInstance()
                .addHeader("Authorization", "Bearer "+token);

        mPagerAdapter = new BaseFragmentAdapter<>(this);
        mPagerAdapter.addFragment(MainFragment.newInstance());
        mPagerAdapter.addFragment(SearchFragment.newInstance());
        mPagerAdapter.addFragment(MovieReviewFragment.newInstance());
        mPagerAdapter.addFragment(MineFragment.newInstance());
        mPagerAdapter.addFragment(LoginFragment.newInstance());
        // 设置成懒加载模式
        mPagerAdapter.setLazyMode(true);
        mViewPager.setAdapter(mPagerAdapter);

        EasyHttp.get(this).api(new CollectMovieIdListApi()).request(new HttpCallback<HttpData<IdListBean>>(this) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSucceed(HttpData<IdListBean> result) {
                super.onSucceed(result);

                //步骤1：创建一个SharedPreferences对象
                SharedPreferences sharedPreferences =getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                //步骤2： 实例化SharedPreferences.Editor对象
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String idS=result.getData().getCollect().stream()
                        .map(i -> i.toString())
                        .collect(Collectors.joining("|"));
                String idSeen=result.getData().getSeen().stream()
                        .map(i -> i.toString())
                        .collect(Collectors.joining("|"));
                String idLikeReview=result.getData().getLikeReview().stream()
                        .map(i -> i.toString())
                        .collect(Collectors.joining("|"));
                String likeComment=result.getData().getLikeComment().stream()
                        .map(i -> i.toString())
                        .collect(Collectors.joining("|"));
                String likeRate=result.getData().getLikeRate().stream()
                        .map(i -> i.toString())
                        .collect(Collectors.joining("|"));
                //步骤3：将获取过来的值放入文件
                editor.putString(getString(R.string.seen_movie_id), idSeen);
                editor.putString(getString(R.string.favorite_movie_id), idS);
                editor.putString(getString(R.string.like_comment_id),likeComment);
                editor.putString(getString(R.string.like_review_id),idLikeReview);
                editor.putString(getString(R.string.like_rate_id),likeRate);


                //步骤4：提交
                editor.commit();
            }

            @Override
            public void onFail(Exception e) {
//                super.onFail(e);
            }
        });
    }

    @Subscribe
    public void onLoginSuccess(String eventName){

        if(eventName.equals(getString(R.string.event_login_success))){
            mViewPager.setCurrentItem(3);
        }else if(eventName.equals(getString(R.string.event_login_fail))){
            mViewPager.setCurrentItem(4);
        }
    }

    /**
     * {@link BottomNavigationView.OnNavigationItemSelectedListener}
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                mViewPager.setCurrentItem(0);
                return true;
            case R.id.home_found:
                mViewPager.setCurrentItem(1);
                return true;
            case R.id.home_message:
                mViewPager.setCurrentItem(2);
                return true;
            case R.id.home_me:
                SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
                String token=sharedPreferences.getString(getString(R.string.user_token),null);
                EasyConfig.getInstance()
                        .addHeader("Authorization", "Bearer "+token);
                if(token==null||token.isEmpty()){
                    mViewPager.setCurrentItem(4);
                }else {
                    mViewPager.setCurrentItem(3);
                }
                return true;
            default:
                break;
        }
        return false;
    }

    /**
     * {@link KeyboardWatcher.SoftKeyboardStateListener}
     */
    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        mBottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onSoftKeyboardClosed() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 回调当前 Fragment 的 onKeyDown 方法
        if (mPagerAdapter.getShowFragment().onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (DoubleClickHelper.isOnDoubleClick()) {
            // 移动到上一个任务栈，避免侧滑引起的不良反应
            moveTaskToBack(false);
            postDelayed(() -> {

                // 进行内存优化，销毁掉所有的界面
                ActivityStackManager.getInstance().finishAllActivities();
                // 销毁进程（注意：调用此 API 可能导致当前 Activity onDestroy 方法无法正常回调）
                // System.exit(0);

            }, 300);
        } else {
            toast(R.string.home_exit_hint);
        }
    }

    @Override
    protected void onDestroy() {
        mViewPager.setAdapter(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(null);
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }
}
package com.shq.movies.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.example.popupwindowlibrary.bean.FiltrateBean;
import com.example.popupwindowlibrary.view.ScreenPopWindow;
import com.google.android.material.tabs.TabLayout;
import com.hjq.base.BaseFragmentAdapter;
import com.shq.movies.R;
import com.shq.movies.common.MyActivity;
import com.shq.movies.common.MyFragment;
import com.shq.movies.event.QueryEvent;
import com.shq.movies.ui.fragment.FavoriteFragment;
import com.shq.movies.ui.fragment.StatusFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public final class MyMovieListActivity extends MyActivity {

    private TabLayout tb_movielist;
    private ViewPager pg_movielist;
    private BaseFragmentAdapter<MyFragment> mPagerAdapter;
    private ScreenPopWindow screenPopWindow;

    private List<FiltrateBean> dictList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_movielist;
    }

    @Override
    protected void initView() {
        tb_movielist = findViewById(R.id.tb_movielist);
        pg_movielist = findViewById(R.id.pg_movielist);

        mPagerAdapter = new BaseFragmentAdapter<>(this);
        mPagerAdapter.addFragment(FavoriteFragment.newInstance(), "Favorites");
        mPagerAdapter.addFragment(StatusFragment.newInstance(), "Seen");

        pg_movielist.setAdapter(mPagerAdapter);
        tb_movielist.setupWithViewPager(pg_movielist);
        screenPopWindow = new ScreenPopWindow(this, dictList);
        screenPopWindow.setChecked("#ffc824").setStrokeColor(R.color.colorSelect)
                .build();
    }

    @Override
    protected void initData() {

    }

    private void initParam() {
        String[] decade = {"older", "1990s", "2000s", "2010s"};
        String[] genres = {"Action", "Adventure", "Animation", "Children's", "Comedy"
                , "Crime", "Documentary", "Drama", "Fantasy"
                , "Film-Noir", "Horror", "Music", "Mystery"
                , "Romance", "Sci-Fi", "Thriller", "War", "Western"};

        String[] order = {"Recently favorited","Highest rated","Most popular"};

        FiltrateBean fb1 = new FiltrateBean();
        fb1.setTypeName("Decade");
        List<FiltrateBean.Children> childrenList = new ArrayList<>();
        for (String aBrand : decade) {
            FiltrateBean.Children cd = new FiltrateBean.Children();
            cd.setValue(aBrand);
            childrenList.add(cd);
        }
        fb1.setChildren(childrenList);

        FiltrateBean fb2 = new FiltrateBean();
        fb2.setTypeName("Genres");
        List<FiltrateBean.Children> childrenList2 = new ArrayList<>();
        for (String aType : genres) {
            FiltrateBean.Children cd = new FiltrateBean.Children();
            cd.setValue(aType);
            childrenList2.add(cd);
        }
        fb2.setChildren(childrenList2);

        FiltrateBean fb3 = new FiltrateBean();
        fb3.setTypeName("Order");
        List<FiltrateBean.Children> childrenList3 = new ArrayList<>();
        for (String aType : order) {
            FiltrateBean.Children cd = new FiltrateBean.Children();
            cd.setValue(aType);
            childrenList3.add(cd);
        }
        fb3.setChildren(childrenList3);

        dictList.add(fb3);
        dictList.add(fb1);
        dictList.add(fb2);

    }

    @Override
    public void onRightClick(View v) {
        screenPopWindow.showAsDropDown(v);
        screenPopWindow.setOnConfirmClickListener(new ScreenPopWindow.OnConfirmClickListener() {
            @Override
            public void onConfirmClick(List<String> list) {
                System.out.println(list);
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    str.append(list.get(i)).append(" ");
                }
                String order=null;
                if(list.get(0)!=null&&(!list.get(0).isEmpty())){
                    if(list.get(0).equals("Highest rated")){
                        order="score";
                    }
                    if(list.get(0).equals("Most popular")){
                        order="viewCount";
                    }
                }
                EventBus.getDefault().post(new QueryEvent("queryChange", order,list.get(1),list.get(2)));
            }
        });
    }



}
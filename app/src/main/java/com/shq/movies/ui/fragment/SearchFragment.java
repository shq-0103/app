package com.shq.movies.ui.fragment;


import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.SettingBar;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shq.movies.R;
import com.shq.movies.common.MyFragment;

import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.QueryMovieApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.ui.activity.HomeActivity;
import com.shq.movies.ui.activity.MovieListActivity;
import com.shq.movies.ui.activity.QueryMovieActivity;
import com.shq.movies.ui.adapter.ConstellationAdapter;
import com.shq.movies.ui.adapter.FindAdapter;
import com.shq.movies.ui.adapter.GirdDropDownAdapter;
import com.shq.movies.ui.adapter.ListDropDownAdapter;
import com.shq.movies.ui.adapter.MovieAdapter;
import com.shq.movies.ui.adapter.MovieListAdapter;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public final class SearchFragment extends MyFragment<HomeActivity> implements
        ViewPager.OnPageChangeListener, BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {

    private EditText et_search;
    private ImageView iv_search;

    private WrapRecyclerView rv_score;
    private WrapRecyclerView rv_popular;
    private WrapRecyclerView rv_week;

    private FindAdapter SfindAdapter;
    private FindAdapter PfindAdapter;
    private FindAdapter WfindAdapter;

    private LinearLayout upcoming;
    private LinearLayout month;
    private SettingBar sb_popular;
    private SettingBar sb_score;
    private SettingBar sb_week;

    private WrapRecyclerView rv_select_list;
    private MovieListAdapter movieListAdapter;
    private SmartRefreshLayout mRefreshLayout;

    private DropDownMenu mDropDownMenu;
    private String headers[] = {"Order", "Decade", "Genres"};
    private List<View> popupViews = new ArrayList<>();

    private GirdDropDownAdapter orderAdapter;
    private ListDropDownAdapter decadeAdapter;
    private ConstellationAdapter genresAdapter;

    private String order[] = {"Unlimited", "Recently favorited", "Highest rated", "Most popular"};
    private String decade[] = {"Unlimited", "older","1990s", "2000s", "2010s"};
    private String genres[] = {"Unlimited","Action", "Adventure", "Animation", "Children's", "Comedy"
            , "Crime", "Documentary", "Drama", "Fantasy"
            , "Film-Noir", "Horror", "Music", "Mystery"
            , "Romance", "Sci-Fi", "Thriller", "War", "Western"};

    private int constellationPosition = 0;
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initView() {
        et_search = findViewById(R.id.et_search);
        iv_search = findViewById(R.id.iv_home_search);
        upcoming = findViewById(R.id.upcoming);
        month = findViewById(R.id.month);
        sb_popular = findViewById(R.id.sb_popular);
        sb_score = findViewById(R.id.sb_score);
        sb_week =findViewById(R.id.sb_week);
        setOnClickListener(iv_search, upcoming,sb_popular,sb_score,sb_week);


        rv_score = findViewById(R.id.rv_score);
        SfindAdapter = new FindAdapter(getAttachActivity());
        SfindAdapter.setOnItemClickListener(this);
        rv_score.setAdapter(SfindAdapter);


        rv_popular = findViewById(R.id.rv_popular);
        PfindAdapter = new FindAdapter(getAttachActivity());
        PfindAdapter.setOnItemClickListener(this);
        rv_popular.setAdapter(PfindAdapter);

        rv_week = findViewById(R.id.rv_week);
        WfindAdapter = new FindAdapter(getAttachActivity());
        WfindAdapter.setOnItemClickListener(this);
        rv_week.setAdapter(WfindAdapter);

        rv_select_list = findViewById(R.id.rv_select_list);
        mRefreshLayout = findViewById(R.id.rl_favorite_movie_refresh);
        movieListAdapter = new  MovieListAdapter(getAttachActivity());
        movieListAdapter.setOnItemClickListener(this);
        rv_select_list.setAdapter(movieListAdapter);

        mDropDownMenu=findViewById(R.id.dropDownMenu);
        //init city menu
        final ListView orderView = new ListView(getAttachActivity());
        orderAdapter = new GirdDropDownAdapter(getAttachActivity(), Arrays.asList(order));
        orderView.setDividerHeight(0);
        orderView.setAdapter(orderAdapter);

        //init age menu
        final ListView decadeView = new ListView(getAttachActivity());
        decadeView.setDividerHeight(0);
        decadeAdapter = new ListDropDownAdapter(getAttachActivity(), Arrays.asList(decade));
        decadeView.setAdapter(decadeAdapter);

        //init constellation
        final View genresView = getLayoutInflater().inflate(R.layout.custom_layout, null);
        GridView constellation =genresView.findViewById(R.id.constellation);
        genresAdapter = new ConstellationAdapter(getAttachActivity(), Arrays.asList(genres));
        constellation.setAdapter(genresAdapter);
        TextView ok = genresView.findViewById(R.id.ok);
        setOnClickListener(ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropDownMenu.setTabText(constellationPosition == 0 ? headers[2] : genres[constellationPosition]);
                mDropDownMenu.closeMenu();
            }
        });

        //init popupViews
        popupViews.add(orderView);
        popupViews.add(decadeView);
        popupViews.add(genresView);

        //add item click event
        orderView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                orderAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : order[position]);
                mDropDownMenu.closeMenu();
            }
        });

        decadeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                decadeAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[1] : decade[position]);
                mDropDownMenu.closeMenu();
            }
        });
        

        constellation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                genresAdapter.setCheckItem(position);
                constellationPosition = position;
            }
        });

        //init context view
        TextView contentView = new TextView(getAttachActivity());
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 0);
        contentView.setVisibility(View.INVISIBLE);

        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }

    @Override
    protected void initData() {
        this.getData();
    }

    private void getData() {
        EasyHttp.get(this).api((IRequestApi) new QueryMovieApi().setName(null).setPage(SfindAdapter.getPageNumber()).setPageSize(3)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                SfindAdapter.setData(result.getData());
            }
        });
        EasyHttp.get(this).api((IRequestApi) new QueryMovieApi().setName(null).setPage(PfindAdapter.getPageNumber()).setPageSize(3)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                PfindAdapter.setData(result.getData());
            }
        });

        EasyHttp.get(this).api((IRequestApi) new QueryMovieApi().setName(null).setPage(WfindAdapter.getPageNumber()).setPageSize(3)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                WfindAdapter.setData(result.getData());
            }
        });


        EasyHttp.get(this).api((IRequestApi) new QueryMovieApi().setName(null).setPage(movieListAdapter.getPageNumber()).setPageSize(10)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                movieListAdapter.setData(result.getData());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.iv_home_search) {
            if (!et_search.getText().toString().trim().isEmpty()) {
                // 跳转到搜索 Activity 并传参
                Intent intent = new Intent(getAttachActivity().getContext(), QueryMovieActivity.class);
                intent.putExtra("keyword", et_search.getText().toString().trim());
                startActivity(intent);
            }
        } else if (v.getId() == R.id.upcoming) {
            startActivity(MovieListActivity.class);
        }else if(v.getId() == R.id.month){
            startActivity(MovieListActivity.class);
        }else if(v.getId() ==R.id.sb_popular){
            startActivity(MovieListActivity.class);
        }else if(v.getId() ==R.id.sb_score){
            startActivity(MovieListActivity.class);
        }else if(v.getId() ==R.id.sb_week){
            startActivity(MovieListActivity.class);
    }}

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

    }

    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {

    }
}
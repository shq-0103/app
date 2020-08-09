package com.shq.movies.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.SettingBar;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shq.movies.R;
import com.shq.movies.common.MyFragment;

import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.AddCollectApi;
import com.shq.movies.http.request.DeleteCollectApi;
import com.shq.movies.http.request.QueryMovieApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.ui.activity.HomeActivity;
import com.shq.movies.ui.activity.MonthActivity;
import com.shq.movies.ui.activity.MovieDetailActivity;
import com.shq.movies.ui.activity.MovieListActivity;
import com.shq.movies.ui.activity.QueryMovieActivity;
import com.shq.movies.ui.activity.UpcomingActivity;
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
import java.util.stream.Collectors;


public final class SearchFragment extends MyFragment<HomeActivity> implements OnRefreshLoadMoreListener,
        ViewPager.OnPageChangeListener, BaseAdapter.OnItemClickListener, BaseAdapter.OnChildClickListener {

    private SmartRefreshLayout smartRefreshLayout;

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

    private DropDownMenu mDropDownMenu;
    private String headers[] = {"Order", "Decade", "Genres"};
    private List<View> popupViews = new ArrayList<>();

    private GirdDropDownAdapter orderAdapter;
    private ListDropDownAdapter decadeAdapter;
    private ConstellationAdapter genresAdapter;

    private String order[] = {"Unlimited", "Highest rated", "Most popular"};
    private String decade[] = {"Unlimited", "older", "1990s", "2000s", "2010s"};
    private String genres[] = {"Unlimited", "Action", "Adventure", "Animation", "Children's", "Comedy"
            , "Crime", "Documentary", "Drama", "Fantasy"
            , "Film-Noir", "Horror", "Music", "Mystery"
            , "Romance", "Sci-Fi", "Thriller", "War", "Western"};

    private int constellationPosition = 0;

    private QueryMovieApi queryMovieApi=new QueryMovieApi();

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
        sb_week = findViewById(R.id.sb_week);

        setOnClickListener(iv_search, upcoming, sb_popular, sb_score, sb_week,month);


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

        movieListAdapter = new MovieListAdapter(getAttachActivity());
        movieListAdapter.setOnItemClickListener(this);
        movieListAdapter.setOnChildClickListener(R.id.bt_favorite,this);
        rv_select_list.setAdapter(movieListAdapter);

        mDropDownMenu = findViewById(R.id.dropDownMenu);
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
        GridView constellation = genresView.findViewById(R.id.constellation);
        genresAdapter = new ConstellationAdapter(getAttachActivity(), Arrays.asList(genres));
        constellation.setAdapter(genresAdapter);
        TextView ok = genresView.findViewById(R.id.ok);
        setOnClickListener(ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropDownMenu.setTabText(constellationPosition == 0 ? headers[2] : genres[constellationPosition]);
                if(constellationPosition==0){
                    queryMovieApi.setGenres(null);
                }else {
                    queryMovieApi.setGenres(genres[constellationPosition]);
                }
                onRefresh(smartRefreshLayout);
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
                if(position==0){
                    queryMovieApi.setOrder(null);
                }else {
                    if(order[position].equals("Highest rated")){
                        queryMovieApi.setOrder("score");
                    }
                    if(order[position].equals("Most popular")){
                        queryMovieApi.setOrder("viewCount");
                    }
                }
                onRefresh(smartRefreshLayout);
                mDropDownMenu.closeMenu();
            }
        });

        decadeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                decadeAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[1] : decade[position]);
                if(position==0){
                    queryMovieApi.setDecade(null);
                }else {
                    queryMovieApi.setDecade(decade[position]);
                }
                onRefresh(smartRefreshLayout);
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

        smartRefreshLayout=findViewById(R.id.rl_favorite_movie_refresh);
        smartRefreshLayout.setOnLoadMoreListener(this);
        LinearLayout ly_container=findViewById(R.id.ly_container);
        ly_container.removeView(smartRefreshLayout);

        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, smartRefreshLayout);
    }

    @Override
    protected void initData() {
        this.getData();
    }

    private void getData() {
        EasyHttp.get(this).api((IRequestApi) new QueryMovieApi().setName(null).setPage(SfindAdapter.getPageNumber()).setPageSize(3).setOrder("score")).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                SfindAdapter.setData(result.getData());
            }
        });
        EasyHttp.get(this).api((IRequestApi) new QueryMovieApi().setName(null).setPage(PfindAdapter.getPageNumber()).setPageSize(3).setOrder("viewCount")).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
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
        this.queryMovie(false);
    }

    private void queryMovie(boolean isLoadMore){
        EasyHttp.get(this).api(queryMovieApi.setPage(movieListAdapter.getPageNumber()).setPageSize(10)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                if(result.getData().isEmpty()){
                    toast(getString(R.string.hint_no_more_data));
                    smartRefreshLayout.setNoMoreData(true);
                    movieListAdapter.setLastPage(true);
                    return;
                }
                if(!isLoadMore){
                    movieListAdapter.setData(result.getData());
                    smartRefreshLayout.finishRefresh();
                }else {
                    movieListAdapter.addData(result.getData());
                    smartRefreshLayout.finishLoadMore();

                }
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
            startActivity(UpcomingActivity.class);
        } else if (v.getId() == R.id.month) {
            startActivity(MonthActivity.class);
        } else if (v.getId() == R.id.sb_popular) {
            startActivity(MovieListActivity.class);
        } else if (v.getId() == R.id.sb_score) {
            startActivity(MovieListActivity.class);
        } else if (v.getId() == R.id.sb_week) {
            startActivity(MovieListActivity.class);
        }
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
        if (recyclerView.getId() == R.id.rv_select_list) {
            this.routerToDetail(String.valueOf(movieListAdapter.getItem(position).getId()));
        }else if (recyclerView.getId() == R.id.rv_popular){
            this.routerToDetail(String.valueOf(PfindAdapter.getItem(position).getId()));
        }else if (recyclerView.getId()==R.id.rv_score){
            this.routerToDetail(String.valueOf(SfindAdapter.getItem(position).getId()));
        }else if (recyclerView.getId()==R.id.rv_week){
            this.routerToDetail(String.valueOf(WfindAdapter.getItem(position).getId()));
        }
    }

    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {
        switch (childView.getId()) {
            case R.id.bt_favorite:
                onClickFavorite((ImageButton) childView, position);
                break;
            case R.id.rv_select_list:
                this.routerToDetail(String.valueOf(movieListAdapter.getItem(position).getId()));
                break;
            case R.id.rv_popular:
                this.routerToDetail(String.valueOf(PfindAdapter.getItem(position).getId()));
                break;
            case R.id.rv_score:
                this.routerToDetail(String.valueOf(SfindAdapter.getItem(position).getId()));
                break;
            case R.id.rv_week:
                this.routerToDetail(String.valueOf(WfindAdapter.getItem(position).getId()));
                break;
            default:
                toast(((TextView) childView).getText());
                break;
        }
    }

    public void routerToDetail(String movieId) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra("movieId", movieId);
        startActivity(intent);
    }


    private void onClickFavorite(ImageButton bt_favor, int position) {
        // 判断保存的 id 是否存在
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        String favorId = sharedPreferences.getString(getString(R.string.favorite_movie_id), null);
        boolean hasFavor = false;
        if (favorId != null && !favorId.isEmpty()) {
            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));
            hasFavor = ids.contains(String.valueOf(movieListAdapter.getItem(position).getId()));
        }

        // 判断是否已登录
        String token = sharedPreferences.getString(getString(R.string.user_token), null);

        if (token == null || token.isEmpty()) {
            toast("please login");
            return;
        }

        if (!hasFavor) {
            EasyHttp.post(this)
                    .api(new AddCollectApi().setFavoriteId(movieListAdapter.getItem(position).getId()).setType(1))
                    .request(new HttpCallback<HttpData<Boolean>>(this) {

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onSucceed(HttpData<Boolean> data) {
                            bt_favor.setImageResource(R.drawable.ic_collect_2);
                            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));

                            ids.add(String.valueOf(movieListAdapter.getItem(position).getId()));
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.favorite_movie_id), ids.stream().map(i -> i.toString()).collect(Collectors.joining("|")));
                            editor.commit();
                        }
                    });
        } else {
            EasyHttp.post(this)
                    .api(new DeleteCollectApi().setId(movieListAdapter.getItem(position).getId()).setType(1))
                    .request(new HttpCallback<HttpData<Boolean>>(this) {

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onSucceed(HttpData<Boolean> data) {
                            bt_favor.setImageResource(R.drawable.ic_collect_1);
                            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));

                            ids.remove(String.valueOf(movieListAdapter.getItem(position).getId()));
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.favorite_movie_id), ids.stream().map(i -> i.toString()).collect(Collectors.joining("|")));
                            editor.commit();

                        }
                    });
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        movieListAdapter.setPageNumber(1);
        movieListAdapter.clearData();
        this.queryMovie(false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        movieListAdapter.setPageNumber(movieListAdapter.getPageNumber()+1);
        this.queryMovie(true);
    }
}
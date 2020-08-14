package com.shq.movies.ui.fragment;

import android.view.Gravity;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.base.BaseAdapter;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;
import com.shq.movies.R;
import com.shq.movies.common.MyFragment;
import com.shq.movies.event.PickMovieEvent;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.RandomMovieApi;
import com.shq.movies.http.response.MovieBean;
import com.shq.movies.ui.activity.CopyActivity;
import com.shq.movies.ui.adapter.MoviePickAdapter;
import com.shq.movies.ui.dialog.MenuDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public final class MoviePickFragment extends MyFragment<CopyActivity> implements BaseAdapter.OnItemClickListener {

    private WrapRecyclerView rv_pick_movie;
    private MoviePickAdapter moviePickAdapter;

    public static MoviePickFragment newInstance() {
        return new MoviePickFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pick_movie;
    }

    @Override
    protected void initView() {
        rv_pick_movie = findViewById(R.id.rv_pick_movie);
        moviePickAdapter = new MoviePickAdapter(getActivity());
//        rv_pick_movie.setLayoutManager(new GridLayoutManager(getContext(), 3));
        moviePickAdapter.setOnItemClickListener(this);

        rv_pick_movie.setAdapter(moviePickAdapter);
    }

    @Override
    protected void initData() {
        EasyHttp.get(this).api(new RandomMovieApi().setNum(9)).request(new HttpCallback<HttpData<List<MovieBean>>>(this) {
            @Override
            public void onSucceed(HttpData<List<MovieBean>> result) {
                super.onSucceed(result);
                moviePickAdapter.setData(result.getData());
            }
        });
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        EventBus.getDefault().post(new PickMovieEvent(moviePickAdapter.getItem(position).getId()));
        EventBus.getDefault().post("pick_success");
    }
}
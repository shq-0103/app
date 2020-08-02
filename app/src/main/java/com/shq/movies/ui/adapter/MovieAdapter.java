package com.shq.movies.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shq.movies.R;
import com.shq.movies.common.MyAdapter;
import com.shq.movies.http.glide.GlideApp;
import com.shq.movies.http.response.MovieBean;

import java.util.Arrays;
import java.util.List;

public final class MovieAdapter extends MyAdapter<MovieBean> {

    public MovieAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends MyAdapter.ViewHolder {

        private ImageView iv_movie;
        private TextView tv_movie_name;
        private TextView tv_movie_date;
        private TextView tv_movie_type;
        private TextView tv_postion;
        private ImageButton bt_favorite;

        private ViewHolder() {
            super(R.layout.item_movie);
            iv_movie=(ImageView)findViewById(R.id.iv_movie_cover);
            tv_movie_name = (TextView)findViewById(R.id.tv_movie_name);
            tv_movie_date = (TextView)findViewById(R.id.tv_movie_date);
            tv_movie_type = (TextView)findViewById(R.id.tv_movie_type);
            bt_favorite = (ImageButton)findViewById(R.id.bt_favorite);
            tv_postion=(TextView)findViewById(R.id.tv_postion);
        }

        @Override
        public void onBindView(int position) {
            SharedPreferences sharedPreferences=getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
            String token=sharedPreferences.getString(getString(R.string.favorite_movie_id),null);
            if(token!=null&&!token.isEmpty()){
                List<String> ids= Arrays.asList(token.split("\\|"));
                if(ids.contains(String.valueOf(getItem(position).getId()) )){
                    MovieBean c=getItem(position);
                    bt_favorite.setImageResource(R.drawable.ic_collect_2);
                }else {
                    bt_favorite.setImageResource(R.drawable.ic_collect_1);

                }
            }else {
                bt_favorite.setImageResource(R.drawable.ic_collect_1);

            }
            int c=getItemCount();
            MovieBean movieBean= getItem(position);
            if(c<=0){
                return;
            }
            GlideApp.with(getContext())
                    .load(getItem(position).getCover())
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.ic_movie_placeholder)
                    .into(iv_movie);
            tv_movie_name.setText(getItem(position).getName());
            tv_movie_date.setText(getItem(position).getReleaseDate());
            tv_movie_type.setText(getItem(position).getGenres());
            tv_postion.setText(String.valueOf(position+1));
        }
    }
}
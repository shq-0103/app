package com.shq.movies.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;
import com.shq.movies.R;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.AddCollectApi;
import com.shq.movies.http.request.DeleteCollectApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class OnClickHelper {
    public static void onClickFavorite(ImageButton bt_favor, Long movieId, AppCompatActivity context) {
        // 判断保存的 id 是否存在
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String favorId = sharedPreferences.getString(context.getString(R.string.favorite_movie_id), null);
        boolean hasFavor = false;
        if (favorId != null && !favorId.isEmpty()) {
            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));
            hasFavor = ids.contains(String.valueOf(movieId));
        }

        // 判断是否已登录
        String token = sharedPreferences.getString(context.getString(R.string.user_token), null);

        if (token == null || token.isEmpty()) {
            ToastUtils.show("please login");
            return;
        }

        if (!hasFavor) {
            EasyHttp.post(context)
                    .api(new AddCollectApi().setFavoriteId(movieId).setType(1))
                    .request(new HttpCallback<HttpData<Boolean>>((OnHttpListener) context) {

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onSucceed(HttpData<Boolean> data) {
                            bt_favor.setImageResource(R.drawable.ic_movie_placeholder);
                            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));

                            ids.add(String.valueOf(movieId));
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(context.getString(R.string.favorite_movie_id), ids.stream().map(i -> i.toString()).collect(Collectors.joining("|")));
                            editor.commit();
                        }
                    });
        } else {
            EasyHttp.post(context)
                    .api(new DeleteCollectApi().setId(movieId).setType(1))
                    .request(new HttpCallback<HttpData<Boolean>>((OnHttpListener) context) {

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onSucceed(HttpData<Boolean> data) {
                            bt_favor.setImageResource(R.drawable.ic_favorite);
                            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));

                            ids.remove(String.valueOf(movieId));
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(context.getString(R.string.favorite_movie_id), ids.stream().map(i -> i.toString()).collect(Collectors.joining("|")));
                            editor.commit();

                        }
                    });
        }
    }

    public static boolean hasFavorite(Long movieId, AppCompatActivity context) {
        // 判断保存的 id 是否存在
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String favorId = sharedPreferences.getString(context.getString(R.string.favorite_movie_id), null);
        boolean hasFavor = false;
        if (favorId != null && !favorId.isEmpty()) {
            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));
            hasFavor = ids.contains(String.valueOf(movieId));
        }
        return hasFavor;
    }

    public static void onRate(Long movieId, AppCompatActivity context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String seenId = sharedPreferences.getString(context.getString(R.string.seen_movie_id), null);
        seenId += "|" + String.valueOf(movieId);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.seen_movie_id), seenId);
        editor.commit();
    }

    public static boolean hasRate(Long movieId, AppCompatActivity context) {
        // 判断保存的 id 是否存在
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String seenId = sharedPreferences.getString(context.getString(R.string.seen_movie_id), null);
        boolean hasSeen = false;
        if (seenId != null && !seenId.isEmpty()) {
            List<String> ids = new ArrayList<>(Arrays.asList(seenId.split("\\|")));
            hasSeen = ids.contains(String.valueOf(movieId));
        }
        return hasSeen;
    }

    public static void onClickFavorite(Button bt_favor, Long movieId, AppCompatActivity context) {
        // 判断保存的 id 是否存在
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String favorId = sharedPreferences.getString(context.getString(R.string.favorite_movie_id), null);
        boolean hasFavor = false;
        if (favorId != null && !favorId.isEmpty()) {
            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));
            hasFavor = ids.contains(String.valueOf(movieId));
        }

        // 判断是否已登录
        String token = sharedPreferences.getString(context.getString(R.string.user_token), null);

        if (token == null || token.isEmpty()) {
            ToastUtils.show("please login");
            return;
        }

        if (!hasFavor) {
            EasyHttp.post(context)
                    .api(new AddCollectApi().setFavoriteId(movieId).setType(1))
                    .request(new HttpCallback<HttpData<Boolean>>((OnHttpListener) context) {

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onSucceed(HttpData<Boolean> data) {
                            bt_favor.setBackgroundColor(context.getColor(R.color.colorSelect));
                            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));

                            ids.add(String.valueOf(movieId));
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(context.getString(R.string.favorite_movie_id), ids.stream().map(i -> i.toString()).collect(Collectors.joining("|")));
                            editor.commit();
                        }
                    });
        } else {
            EasyHttp.post(context)
                    .api(new DeleteCollectApi().setId(movieId).setType(1))
                    .request(new HttpCallback<HttpData<Boolean>>((OnHttpListener) context) {

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onSucceed(HttpData<Boolean> data) {
                            bt_favor.setBackground(context.getDrawable(R.drawable.moviedetail_button_color));
                            List<String> ids = new ArrayList<>(Arrays.asList(favorId.split("\\|")));

                            ids.remove(String.valueOf(movieId));
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(context.getString(R.string.favorite_movie_id), ids.stream().map(i -> i.toString()).collect(Collectors.joining("|")));
                            editor.commit();

                        }
                    });
        }
    }

    public static boolean hasLike(Long likeId, AppCompatActivity context, int type) {
        String key = null;
        switch (type) {
            case 1:
                key = context.getString(R.string.like_review_id);
                break;
            case 2:
                key = context.getString(R.string.like_comment_id);
                break;
            case 3:
                key = context.getString(R.string.like_rate_id);
                break;
        }
        // 判断保存的 id 是否存在
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(key, null);
        boolean hasSeen = false;
        if (id != null && !id.isEmpty()) {
            List<String> ids = new ArrayList<>(Arrays.asList(id.split("\\|")));
            hasSeen = ids.contains(String.valueOf(likeId));
        }
        return hasSeen;
    }

    public static void delOrAdd(Long id, int key, boolean isDel, Context context) {
        // 判断保存的 id 是否存在
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String idStr = sharedPreferences.getString(context.getString(key), null);


        if (!isDel) {
            List<String> ids = new ArrayList<>(Arrays.asList(idStr.split("\\|")));
            ids.add(String.valueOf(id));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(context.getString(key), ids.stream().map(i -> i.toString()).collect(Collectors.joining("|")));
            editor.commit();
        } else {
            List<String> ids = new ArrayList<>(Arrays.asList(idStr.split("\\|")));
            ids.remove(String.valueOf(id));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(context.getString(key), ids.stream().map(i -> i.toString()).collect(Collectors.joining("|")));
            editor.commit();
        }
    }
}


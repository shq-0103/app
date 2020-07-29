package com.shq.movies.ui.adapter;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;

import com.shq.movies.R;

public final class MainTopImgAdapter extends PagerAdapter {

    private static final int[] DRAWABLES = {R.drawable.guide_1_bg, R.drawable.guide_2_bg, R.drawable.guide_3_bg};

    @Override
    public int getCount() {
        return DRAWABLES.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        AppCompatImageView imageView = new AppCompatImageView(container.getContext());
        imageView.setPaddingRelative(0, 0, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
                        container.getContext().getResources().getDisplayMetrics()));
        imageView.setImageResource(DRAWABLES[position]);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
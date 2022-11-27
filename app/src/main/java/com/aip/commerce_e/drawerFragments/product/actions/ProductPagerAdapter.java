package com.aip.commerce_e.drawerFragments.product.actions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;

import androidx.viewpager.widget.PagerAdapter;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductPagerAdapter extends PagerAdapter {
    private Context ctx;
    private String[] imgUrls;

    ProductPagerAdapter(Context ctx, String[] imgUrls){
        this.ctx = ctx;
        this.imgUrls = imgUrls;
    }
    @Override
    public int getCount() {
        return imgUrls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(ctx);
        Picasso.get().load(imgUrls[position]).fit().centerCrop().into(imageView);
        container.addView(imageView);
        notifyDataSetChanged();
        return imageView;
    }

    public void setImgUrls(String[] imgUrls) {
        this.imgUrls = imgUrls;
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }
}

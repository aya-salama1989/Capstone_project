package com.jobease.www.jobease.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jobease.www.jobease.R;

import butterknife.ButterKnife;

/**
 * Created by Dell on 05/08/2017.
 */

public class SideMenuRecyclerAdapter extends RecyclerView.Adapter<SideMenuRecyclerAdapter.SideMenuItemVH> {
    private SideMenuClickListener sideMenuClickListener;
    private Context context;

    public SideMenuRecyclerAdapter(SideMenuClickListener sideMenuClickListener) {
        this.sideMenuClickListener = sideMenuClickListener;
    }

    @Override
    public SideMenuItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_side_menu, parent, false);
        SideMenuItemVH sideMenuItemVH = new SideMenuItemVH(v);
        return sideMenuItemVH;
    }

    @Override
    public void onBindViewHolder(SideMenuItemVH holder, int position) {
        TypedArray sideItemImages = context.getResources().obtainTypedArray(R.array.home_images);
        TypedArray sideItemText = context.getResources().obtainTypedArray(R.array.home_texts);
        holder.setData(sideItemImages.getDrawable(position), sideItemText.getString(position));
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public interface SideMenuClickListener {
        void onSideItemCick(int position);
    }

    class SideMenuItemVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView txtVue;

        public SideMenuItemVH(View itemView) {
            super(itemView);
            imageView = ButterKnife.findById(itemView, R.id.iv_side_img);
            txtVue = ButterKnife.findById(itemView, R.id.tv_side_text);
            itemView.setOnClickListener(this);

        }

        private void setData(Drawable drawableRes, String textRes) {
            imageView.setImageDrawable(drawableRes);
            txtVue.setText(textRes);
        }

        @Override
        public void onClick(View v) {
            sideMenuClickListener.onSideItemCick(getAdapterPosition());
        }
    }
}

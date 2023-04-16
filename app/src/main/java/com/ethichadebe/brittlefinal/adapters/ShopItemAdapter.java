package com.ethichadebe.brittlefinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ethichadebe.brittlefinal.R;
import com.ethichadebe.brittlefinal.local.model.Shop;

import java.util.ArrayList;
import java.util.List;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ShopItemViewHolder> {
    private Context context;

    private List<Shop> shops = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ShopItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivPreview;
        public TextView tvShopName, tvComingSoon;
        public RelativeLayout rlCard;

        public ShopItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            ivPreview = itemView.findViewById(R.id.ivPreview);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            rlCard = itemView.findViewById(R.id.rlCard);
            tvComingSoon = itemView.findViewById(R.id.tvComingSoon);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public void setShopAdapter(Context context, List<Shop> shops) {
        this.context = context;
        this.shops = shops;
    }

    @NonNull
    @Override
    public ShopItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);

        return new ShopItemViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopItemViewHolder holder, int position) {
        Shop item = shops.get(position);

        holder.tvShopName.setText(item.getName());

        if (!item.isActive()) {
            holder.tvComingSoon.setVisibility(View.VISIBLE);
            switch (item.getName().toLowerCase()) {
                case "game":
                    holder.ivPreview.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.gamebw));
                    break;
                case "woolworths":
                    holder.ivPreview.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.woolworthsbw));
                    break;
            }
            holder.rlCard.setForeground(ContextCompat.getDrawable(context, R.color.opacity));
            holder.tvShopName.setTextColor(ContextCompat.getColor(context, R.color.textGrey));
        } else {
            Glide.with(context).load(item.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.ivPreview);
            holder.rlCard.setForeground(null);
        }

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);

        holder.itemView.setAnimation(animation);

    }

    @Override
    public int getItemCount() {
        return shops.size();
    }
}

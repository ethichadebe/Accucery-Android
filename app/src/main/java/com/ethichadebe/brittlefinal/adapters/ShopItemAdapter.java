package com.ethichadebe.brittlefinal.adapters;

import android.content.Context;
import android.util.Log;
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
    private static final String TAG = "ShopItemAdapter";
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
        public TextView tvShopName, tvMessage, tvItemCount, tvTotalPrice;
        public RelativeLayout rlCard;

        public ShopItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            tvItemCount = itemView.findViewById(R.id.tvItemCount);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            ivPreview = itemView.findViewById(R.id.ivPreview);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            rlCard = itemView.findViewById(R.id.rlCard);
            tvMessage = itemView.findViewById(R.id.tvMessage);

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
        holder.tvItemCount.setText(String.valueOf(item.getItemsCount()));
        holder.tvTotalPrice.setText(context.getResources().getString(R.string.price_display, item.getPrice()));


        if (!item.isActive()) {
            holder.tvMessage.setVisibility(View.VISIBLE);
            holder.tvMessage.setText(context.getResources().getString(R.string.coming_soon));
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
            holder.rlCard.setForeground(null);
            if (!item.isOpen()) {
                holder.tvMessage.setVisibility(View.VISIBLE);
                holder.tvMessage.setText("Shop locked, click to unlock");
                Log.d(TAG, "onBindViewHolder: " + item.getName() + " is closed");
                holder.tvShopName.setTextColor(ContextCompat.getColor(context, R.color.textGrey));
                holder.rlCard.setForeground(ContextCompat.getDrawable(context, R.color.opacity));
            }
            Glide.with(context).load(item.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.ivPreview);
        }

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);

        holder.itemView.setAnimation(animation);

    }

    @Override
    public int getItemCount() {
        return shops.size();
    }
}

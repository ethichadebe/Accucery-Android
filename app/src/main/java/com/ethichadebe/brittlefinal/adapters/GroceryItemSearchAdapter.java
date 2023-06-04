package com.ethichadebe.brittlefinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ethichadebe.brittlefinal.R;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;

import java.util.ArrayList;
import java.util.List;

public class GroceryItemSearchAdapter extends RecyclerView.Adapter<GroceryItemSearchAdapter.GroceryItemSearchViewHolder> {
    private Context context;

    private List<GroceryItem> groceryItems = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class GroceryItemSearchViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivItem;
        public TextView tvName, tvPrice, tvSavePrice;

        public GroceryItemSearchViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            ivItem = itemView.findViewById(R.id.ivItem);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSavePrice = itemView.findViewById(R.id.tvSavePrice);
            tvName = itemView.findViewById(R.id.tvName);

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

    public void setGroceryItemSearchAdapter(Context context, List<GroceryItem> groceryItems) {
        this.context = context;
        this.groceryItems = groceryItems;
    }

    @NonNull
    @Override
    public GroceryItemSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_item_search, parent, false);

        return new GroceryItemSearchViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryItemSearchViewHolder holder, int position) {
        GroceryItem item = groceryItems.get(position);

        Glide.with(context).load(item.getImage()).placeholder(R.drawable.food).into(holder.ivItem);
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(context.getResources().getString(R.string.price_display, item.getPrice()));
        switch (item.getShopId()) {
            case 1:
                holder.tvSavePrice.setTextColor(ContextCompat.getColor(context, R.color.checkersExtra));
                break;
            case 2:
                holder.tvSavePrice.setTextColor(ContextCompat.getColor(context, R.color.shopriteExtra));
                break;
            case 3:
                holder.tvSavePrice.setTextColor(ContextCompat.getColor(context, R.color.pnpSave));
                break;
        }
        holder.tvSavePrice.setText(context.getResources().getString(R.string.price_display, item.getSavePrice()));

    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

}

package com.ethichadebe.brittlefinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.ethichadebe.brittlefinal.R;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.local.model.Shop;
import com.google.android.material.transition.Hold;

import java.util.ArrayList;
import java.util.List;

public class GroceryItemSearchAdapter extends RecyclerView.Adapter<GroceryItemSearchAdapter.GroceryItemSearchViewHolder> {
    private static final String TAG = "GroceryItemAdapter";
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
        public TextView  tvName, tvPrice;

        public GroceryItemSearchViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            ivItem = itemView.findViewById(R.id.ivItem);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvName = itemView.findViewById(R.id.tvName);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
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

        GroceryItemSearchViewHolder civh = new GroceryItemSearchViewHolder(v, listener);

        return civh;
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryItemSearchViewHolder holder, int position) {
        GroceryItem item = groceryItems.get(position);

        //holder.lavCheckBox.setColorFilter();
        Glide.with(context).load("https://www.shoprite.co.za/"+item.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.ivItem);
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText("R" + item.getPrice());

    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

}

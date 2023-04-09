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

public class GroceryItemAdapter extends RecyclerView.Adapter<GroceryItemAdapter.GroceryItemViewHolder> {
    private static final String TAG = "GroceryItemAdapter";
    private Context context;

    private List<GroceryItem> groceryItems = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onAddQuantityClick(int position);

        void onSubQuantityClick(int position);
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class GroceryItemViewHolder extends RecyclerView.ViewHolder {
        public LottieAnimationView lavCheckBox;
        public ImageView ivImage;
        public TextView tvAdd, tvSubtract, tvItemName, tvItemPrice,tvSpace,tvItemQuantity;

        public GroceryItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            lavCheckBox = itemView.findViewById(R.id.lavCheckBox);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvAdd = itemView.findViewById(R.id.tvAdd);
            tvSubtract = itemView.findViewById(R.id.tvSubtract);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvSpace = itemView.findViewById(R.id.tvSpace);
            tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity);

            tvAdd.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onAddQuantityClick(position);
                    }
                }
            });

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

            tvSubtract.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onSubQuantityClick(position);
                    }
                }
            });
        }
    }

    public void setGroceryItemAdapter(Context context, List<GroceryItem> groceryItems) {
        this.context = context;
        this.groceryItems = groceryItems;
    }

    @NonNull
    @Override
    public GroceryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_item, parent, false);

        GroceryItemViewHolder civh = new GroceryItemViewHolder(v, listener);

        return civh;
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryItemViewHolder holder, int position) {
        GroceryItem item = groceryItems.get(position);

        if (item.isChecked()){
            holder.lavCheckBox.setSpeed(-1);
            holder.lavCheckBox.playAnimation();
        }else {
            holder.lavCheckBox.setSpeed(1);
            holder.lavCheckBox.playAnimation();
        }

        Glide.with(context).load(item.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.ivImage);
        holder.tvItemName.setText(item.getName());
        holder.tvItemPrice.setText("R" + item.getPrice());
        holder.tvItemQuantity.setText(" x" + item.getQuantity() + " = " + item.getPrice()*item.getQuantity());

        if (position == 0) {
            holder.tvSpace.setVisibility(View.VISIBLE);
        } else if (position == groceryItems.size() - 1) {
            holder.tvSpace.setVisibility(View.GONE);
        } else {
            holder.tvSpace.setVisibility(View.GONE);
        }

        //holder.tvQuantity.setText("" + item.getQuantity());

    /* if (!item.isActive()) {
            holder.rlCard.setForeground(context.getResources().getDrawable(R.color.opacity));
        } else {
            holder.rlCard.setForeground(null);
        }*/

    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

}

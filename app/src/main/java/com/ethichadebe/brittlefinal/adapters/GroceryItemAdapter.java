package com.ethichadebe.brittlefinal.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ethichadebe.brittlefinal.R;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

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
        public ImageView ivImage;

        public RelativeLayout rlItem;
        public TextView tvAdd, tvSubtract, tvItemName, tvItemPrice, tvItemQuantity;

        //Ad
        private ImageView ad_icon;
        private TextView ad_headline, ad_advertiser, ad_body, ad_action;
        private NativeAdView unifiedNativeAdView;

        public GroceryItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            rlItem = itemView.findViewById(R.id.rlItem);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvAdd = itemView.findViewById(R.id.tvAdd);
            tvSubtract = itemView.findViewById(R.id.tvSubtract);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity);

            ad_icon = itemView.findViewById(R.id.ad_icon);
            ad_headline = itemView.findViewById(R.id.ad_headline);
            ad_advertiser = itemView.findViewById(R.id.ad_advertiser);
            ad_body = itemView.findViewById(R.id.ad_body);
            ad_action = itemView.findViewById(R.id.ad_action);
            unifiedNativeAdView = itemView.findViewById(R.id.native_ad_layout);


            tvAdd.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onAddQuantityClick(position);
                    }
                }
            });

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

            tvSubtract.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
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

        return new GroceryItemViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryItemViewHolder holder, int position) {
        GroceryItem item = groceryItems.get(position);

        Glide.with(context).load(item.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.ivImage);
        holder.tvItemName.setText(item.getName());
        holder.tvItemPrice.setText(context.getResources().getString(R.string.price_display, (float) item.getPrice()));
        holder.tvItemQuantity.setText(context.getResources().getString(R.string.quantity_and_total, item.getQuantity(), item.getPrice() * item.getQuantity()));

        if (position == 0) {
            holder.ivImage.setVisibility(View.GONE);
            holder.tvItemQuantity.setVisibility(View.GONE);
            holder.rlItem.setVisibility(View.GONE);
        }

        if (item.isChecked()) {
            holder.rlItem.setForeground(ContextCompat.getDrawable(context, R.color.opacity));
        } else {
            holder.rlItem.setForeground(null);
        }

        if ((ThreadLocalRandom.current().nextInt(1, 10 + 1) == 3) && position != 0) {
            AdLoader adLoader = new AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110").forNativeAd(nativeAd -> {
                // Show the ad.
                mapAdView(nativeAd, holder);
            }).withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                    // Handle the failure by logging, altering the UI, and so on.
                }
            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    private void mapAdView(NativeAd fromGoogle, GroceryItemViewHolder holder) {
        holder.unifiedNativeAdView.setVisibility(View.VISIBLE);

        holder.unifiedNativeAdView.setIconView(holder.ad_icon);
        holder.unifiedNativeAdView.setHeadlineView(holder.ad_headline);
        holder.unifiedNativeAdView.setAdvertiserView(holder.ad_advertiser);
        holder.unifiedNativeAdView.setBodyView(holder.ad_body);
        holder.unifiedNativeAdView.setCallToActionView(holder.ad_action);

        //Icon
        if (fromGoogle.getIcon() == null) {
            Objects.requireNonNull(holder.unifiedNativeAdView.getIconView()).setVisibility(View.GONE);
        } else {
            ((ImageView) Objects.requireNonNull(holder.unifiedNativeAdView.getIconView())).setImageDrawable(fromGoogle.getIcon().getDrawable());
        }

        //Headline
        if (fromGoogle.getHeadline() == null) {
            Objects.requireNonNull(holder.unifiedNativeAdView.getHeadlineView()).setVisibility(View.GONE);
        } else {
            ((TextView) Objects.requireNonNull(holder.unifiedNativeAdView.getHeadlineView())).setText(fromGoogle.getHeadline());
        }

        //Advertiser
        if (fromGoogle.getAdvertiser() == null) {
            Objects.requireNonNull(holder.unifiedNativeAdView.getAdvertiserView()).setVisibility(View.GONE);
        } else {
            ((TextView) Objects.requireNonNull(holder.unifiedNativeAdView.getAdvertiserView())).setText(fromGoogle.getAdvertiser());
        }

        //Body
        if (fromGoogle.getBody() == null) {
            Objects.requireNonNull(holder.unifiedNativeAdView.getBodyView()).setVisibility(View.GONE);
        } else {
            ((TextView) Objects.requireNonNull(holder.unifiedNativeAdView.getBodyView())).setText(fromGoogle.getBody());
        }

        //Call Action
        if (fromGoogle.getCallToAction() == null) {
            Objects.requireNonNull(holder.unifiedNativeAdView.getCallToActionView()).setVisibility(View.GONE);
        } else {
            ((TextView) Objects.requireNonNull(holder.unifiedNativeAdView.getCallToActionView())).setText(fromGoogle.getCallToAction());
        }

        holder.unifiedNativeAdView.setNativeAd(fromGoogle);
        Log.d(TAG, "mapAdView: Ad loaded");
    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

}

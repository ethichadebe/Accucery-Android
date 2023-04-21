package com.ethichadebe.brittlefinal.Util;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.ethichadebe.brittlefinal.adapters.GroceryItemSearchAdapter;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Scraper extends AsyncTask<String, Void, Void> {

    private ArrayList<GroceryItem> items;
    private final Intent intent;
    private final GroceryItemSearchAdapter groceryItemAdapter;
    private final Context context;

    public ArrayList<GroceryItem> getItems() {
        return items;
    }

    public Scraper(Intent intent, GroceryItemSearchAdapter groceryItemAdapter, Context context) {
        this.intent = intent;
        this.groceryItemAdapter = groceryItemAdapter;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        groceryItemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Void doInBackground(String... url) {
        try {
            Log.d(TAG, "doInBackground: trying....: " + url[0]);
            items = new ArrayList<>();
            if (url[0].contains("https://www.pnp.co.za/pnpstorefront/pnp/en/search/?text=")) {
                Document document = Jsoup.connect(url[0]).get();
                Elements data = document.select("div.productCarouselItem");

                Log.d(TAG, "doInBackground: size " + data);
                int size = data.size();
                for (int i = 0; i < size; i++) {
                    String image = data.select("div.thumb").select("img").eq(i).attr("src");
                    String name = data.select("div.item-name").eq(i).text();
                    String price = data.select("div.product-price").select("div.currentPrice").eq(i).text().replaceAll("[^\\d.]", "");

                    cleanItem(name, price, image, i);

                }
            } else if (url[0].contains("https://www.woolworths.co.za/cat?Ntt=")) {
                Log.d(TAG, "doInBackground: it does contain");
                Document document = Jsoup.connect(url[0] + "&Dy=1").get();
                Elements data = document.select("div.product-list__item");

                Log.d(TAG, "doInBackground: size " + document);
                int size = data.size();
                for (int i = 0; i < size; i++) {
                    String image = data.select("div.product--image").select("img").eq(i).attr("src");
                    String name = data.select("div.product-card__name").select("a").eq(i).text();
                    String price = data.select("div.product__price").select("strong.price").eq(i).text().replaceAll("[^\\d.]", "");

                    cleanItem(name, price, image, i);
                }

            } else if (url[0].contains("https://www.makro.co.za/search/?text=")) {
                Log.d(TAG, "doInBackground: it does contain");
                Document document = Jsoup.connect(url[0] + "&Dy=1").get();
                Elements data = document.select("div.mak-product-tiles-container__product-tile");

                Log.d(TAG, "doInBackground: size " + document);
                int size = data.size();
                for (int i = 0; i < size; i++) {
                    String image = data.select("a.product-tile-inner__img").select("img").eq(i).attr("src");
                    String name = data.select("a.product-tile-inner__productTitle").select("span").eq(i).text();
                    String price = data.select("p.price").select("span.mak-save-price").eq(i).text() + "." +
                            data.select("p.price").select("span.mak-product__cents").eq(i).text().replaceAll("[^\\d.]", "");

                    cleanItem(name, price, image, i);
                }

            } else if (url[0].contains("https://www.game.co.za/l/search/?t=")) {
                Log.d(TAG, "doInBackground: it does contain");
                Document document = Jsoup.connect(url[0] + "&q=" + url[0].replace("https://www.game.co.za/l/search/?t=", "")
                        + "%3Arelevance").get();
                Elements data = document.select("div.r-14lw9ot");

                Log.d(TAG, "doInBackground: size " + document);
                int size = data.size();
                for (int i = 0; i < size; i++) {
                    String image = data.select("div.r-1p0dtai").select("img").eq(i).attr("src");
                    String name = data.select("a.css-4rbku5").select("div").eq(i).text();
                    String price = data.select("div.css-901oao").eq(i).text().replace(",", ".")
                            .replaceAll("[^\\d.]", "");

                    cleanItem(name, price, image, i);
                }

            } else {
                Log.d(TAG, "doInBackground: it does contain");
                Document document = Jsoup.connect(url[0]).get();
                Elements data = document.select("figure.item-product__content");

                Log.d(TAG, "doInBackground: size " + data);
                int size = data.size();
                for (int i = 0; i < size; i++) {
                    String image = data.select("figure.item-product__content").select("img").eq(i).attr("src");
                    String name = data.select("h3.item-product__name").select("a").eq(i).text();
                    String price = data.select("div.special-price__price").select("span").eq(i).text().replaceAll("[^\\d.]", "");

                    cleanItem(name, price, "https://www.shoprite.co.za/" + image, i);
                }

            }
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: error " + e.getMessage());
            throw new RuntimeException(e);
        }

        return null;
    }

    private void cleanItem(String name, String price, String image, int i) {
        if (price.isEmpty()) {
            price = "0.0";
        }
        Log.d(TAG, "doInBackground: Item: " + i + "------------------------------------------------------------------");
        Log.d(TAG, "doInBackground: name: " + name);
        Log.d(TAG, "doInBackground: price: " + price);
        Log.d(TAG, "doInBackground: image: " + image);
        items.add(new GroceryItem(name, Double.parseDouble(price), image, intent.getIntExtra("sID", 0)));
        groceryItemAdapter.setGroceryItemSearchAdapter(context, items);

        Log.d(TAG, "cleanItem: ");
    }
}

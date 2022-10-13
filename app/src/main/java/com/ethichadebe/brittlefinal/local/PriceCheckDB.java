package com.ethichadebe.brittlefinal.local;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ethichadebe.brittlefinal.local.dao.GroceryItemDao;
import com.ethichadebe.brittlefinal.local.dao.ShopDao;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.local.model.Shop;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {Shop.class, GroceryItem.class}, version = 87)
public abstract class PriceCheckDB extends RoomDatabase {
    private static final String TAG = "PriceCheckDB";

    private static PriceCheckDB instance;

    public abstract GroceryItemDao groceryItemDao();

    public abstract ShopDao shopDao();

    public static synchronized PriceCheckDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), PriceCheckDB.class, "PriceCheckDatabaseTest")
                    .addCallback(roomCallback)
                    .enableMultiInstanceInvalidation()
                    .build();
        }

        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private ShopDao shopDao;
        private GroceryItemDao groceryItemDao;

        private PopulateDBAsyncTask(PriceCheckDB db) {
            Log.d(TAG, "doInBackground: done");
            shopDao = db.shopDao();
           groceryItemDao = db.groceryItemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            shopDao.insert(new Shop(1, "CHECKERS", "https://theflamingo.co.za/wp-content/uploads/2018/11/Checkers-1.png", true));
            shopDao.insert(new Shop(2, "SHOPRITE", "https://pbs.twimg.com/profile_images/1136175013009211392/Rf69JN5r_400x400.jpg", true));
            shopDao.insert(new Shop(3, "Pick n Pay", "https://cdn-prd-02.pnp.co.za/sys-master/images/h42/hf7/8796170453022/onlineshopping_logo.png", true));
            shopDao.insert(new Shop(4, "Woolworths", "https://www.goldenwalkcentre.co.za/wp-content/uploads/2018/03/woolworths-copy.png", true));
            shopDao.insert(new Shop(5, "Makro", "https://www.thinklocal.co.za/images/NBSpu6SeLjmAuVp2/424846375810185_39_fs.jpg", true));
            shopDao.insert(new Shop(6, "Game", "https://www.goldenwalkcentre.co.za/wp-content/uploads/2018/03/woolworths-copy.png", true));

            groceryItemDao.insert(new GroceryItem("100 Pipers Scotch Whisky 750ml", 129.5,"dwerew",0,1));
            groceryItemDao.insert(new GroceryItem("100 Pipers Whisky Bottle 750ml", 49.5,"dwerew",0,1));
            groceryItemDao.insert(new GroceryItem("1659 Natural Sweet Rosè Wine Bottle 750ml", 453.5,"dwerew",0,1));
            groceryItemDao.insert(new GroceryItem("1659 Red Special Edition Red Wine Bottle 750ml", 24.5,"dwerew",0,1));
            groceryItemDao.insert(new GroceryItem("Act II Sweet & Salty Kettle Corn Microwave Popcorn 85g", 345.5,"dwerew",0,1));
            groceryItemDao.insert(new GroceryItem("100 Pipers Scotch Whisky 750ml", 12.5,"dwerew",0,1));
            groceryItemDao.insert(new GroceryItem("100 Pipers Whisky Bottle 750ml", 65.5,"dwerew",0,1));
            groceryItemDao.insert(new GroceryItem("1659 Natural Sweet Rosè Wine Bottle 750ml", 153.99,"dwerew",0,1));
            groceryItemDao.insert(new GroceryItem("1659 Red Special Edition Red Wine Bottle 750ml", 543.54,"dwerew",0,1));
            groceryItemDao.insert(new GroceryItem("Act II Sweet & Salty Kettle Corn Microwave Popcorn 85g", 121.90,"dwerew",0,1));

            return null;
        }
    }
}

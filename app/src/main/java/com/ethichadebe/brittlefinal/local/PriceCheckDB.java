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

@Database(entities = {Shop.class, GroceryItem.class}, version = 82)
public abstract class PriceCheckDB extends RoomDatabase {
    private static final String TAG = "PriceCheckDB";

    private static PriceCheckDB instance;

    public abstract GroceryItemDao groceryItemDao();

    public abstract ShopDao shopDao();

    public static synchronized PriceCheckDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), PriceCheckDB.class, "PriceCheckDatabaseTest")
                    .addCallback(roomCallback)
                    .fallbackToDestructiveMigration()
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
            shopDao.insert(new Shop(1, "CHECKERS", "https://theflamingo.co.za/wp-content/uploads/2018/11/Checkers-1.png",
                    "https://www.checkers.co.za/search/all?q=",true));
            shopDao.insert(new Shop(2, "SHOPRITE", "https://pbs.twimg.com/profile_images/1136175013009211392/Rf69JN5r_400x400.jpg",
                    "https://www.shoprite.co.za/search/all?q=", true));
            shopDao.insert(new Shop(3, "Pick n Pay", "https://cdn-prd-02.pnp.co.za/sys-master/images/h42/hf7/8796170453022/onlineshopping_logo.png",
                    "https://www.pnp.co.za/pnpstorefront/pnp/en/search/?text=",true));
            shopDao.insert(new Shop(4, "Woolworths",
                    "https://d1yjjnpx0p53s8.cloudfront.net/styles/logo-original-577x577/s3/092010/woolworths_sa.jpg?itok=nhBxmCsE",
                    "https://www.woolworths.co.za/cat?Ntt=banana&Dy=1",true));
            shopDao.insert(new Shop(5, "Makro", "https://www.thinklocal.co.za/images/NBSpu6SeLjmAuVp2/424846375810185_39_fs.jpg",
                    "https://www.makro.co.za/search/?text=", true));
            shopDao.insert(new Shop(6, "Game", "https://www.vitatechhealth.com/wp-content/uploads/2020/04/game-logo.png",
                    "https://www.game.co.za/l/search/?t=banana%20bag&q=banana%20bag%3Arelevance",true));

            return null;
        }
    }
}

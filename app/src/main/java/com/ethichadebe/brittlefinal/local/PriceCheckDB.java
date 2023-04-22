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

@Database(entities = {Shop.class, GroceryItem.class}, version = 67)
public abstract class PriceCheckDB extends RoomDatabase {
    private static final String TAG = "PriceCheckDB";

    private static PriceCheckDB instance;

    public abstract GroceryItemDao groceryItemDao();

    public abstract ShopDao shopDao();

    public static synchronized PriceCheckDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), PriceCheckDB.class, "AccuceryDatabaseTest")
                    .addCallback(roomCallback)
                    .fallbackToDestructiveMigration()
                    .enableMultiInstanceInvalidation()
                    .build();
        }

        return instance;
    }

    private static final Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private final ShopDao shopDao;

        private PopulateDBAsyncTask(PriceCheckDB db) {
            Log.d(TAG, "doInBackground: done");
            shopDao = db.shopDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            shopDao.insert(new Shop(1, "CHECKERS", "https://theflamingo.co.za/wp-content/uploads/2018/11/Checkers-1.png",
                    "https://www.checkers.co.za/search/all?q=",true,true));
            shopDao.insert(new Shop(2, "SHOPRITE", "https://pbs.twimg.com/profile_images/1136175013009211392/Rf69JN5r_400x400.jpg",
                    "https://www.shoprite.co.za/search/all?q=", true,true));
            shopDao.insert(new Shop(3, "Pick n Pay", "https://cdn-prd-02.pnp.co.za/sys-master/images/h42/hf7/8796170453022/onlineshopping_logo.png",
                    "https://www.pnp.co.za/pnpstorefront/pnp/en/search/?text=",true,true));
            shopDao.insert(new Shop(4, "Woolworths",
                    "https://d1yjjnpx0p53s8.cloudfront.net/styles/logo-original-577x577/s3/092010/woolworths_sa.jpg?itok=nhBxmCsE",
                    "https://www.woolworths.co.za/cat?Ntt=",true,false));
            shopDao.insert(new Shop(5, "Makro", "https://www.thinklocal.co.za/images/NBSpu6SeLjmAuVp2/424846375810185_39_fs.jpg",
                    "https://www.makro.co.za/search/?text=",true, true));
            shopDao.insert(new Shop(6, "Game", "data:image/png;base64,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",
                    "https://www.game.co.za/l/search/?t=",true,false));

            return null;
        }
    }
}

package com.ethichadebe.brittlefinal.local;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.local.model.Shop;

@Database(entities = {GroceryItem.class, Shop.class}, version = 5)
public abstract class PriceCheckDB extends RoomDatabase {

    private static PriceCheckDB instance;

    //public abstract GroceryItemDao groceryItemDao();

    public abstract ShopDao shopDao();

    public static synchronized PriceCheckDB getInstance(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(), PriceCheckDB.class,"GroceryItemsTable")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    private static Callback roomCallback = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void,Void,Void>{
        private ShopDao shopDao;

        private PopulateDBAsyncTask(PriceCheckDB db){
            shopDao = db.shopDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            shopDao.insert(new Shop(1,"CHECKERS","https://theflamingo.co.za/wp-content/uploads/2018/11/Checkers-1.png",true));
            shopDao.insert(new Shop(2,"SHOPRITE","https://www.shoprite.co.za/c-2256/All-Departments?q=%3Arelevance%3AbrowseAllStoresFacetOff%3AbrowseAllStoresFacetOff&page=550",true));
            shopDao.insert(new Shop(3,"CHECKERS","https://theflamingo.co.za/wp-content/uploads/2018/...",true));
            shopDao.insert(new Shop(1,"CHECKERS","https://theflamingo.co.za/wp-content/uploads/2018/...", true));
            return null;
        }
    }
}

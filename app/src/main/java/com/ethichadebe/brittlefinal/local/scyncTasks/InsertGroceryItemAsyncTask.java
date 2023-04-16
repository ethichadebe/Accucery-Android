package com.ethichadebe.brittlefinal.local.scyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.ethichadebe.brittlefinal.local.dao.GroceryItemDao;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;

public class InsertGroceryItemAsyncTask extends AsyncTask<GroceryItem,Void,Void> {
    private static final String TAG = "InsertGroceryItemAsyncT";
    private GroceryItemDao groceryItemDao;

    public InsertGroceryItemAsyncTask(GroceryItemDao groceryItemDao){
        this.groceryItemDao = groceryItemDao;
    }

    @Override
    protected Void doInBackground(GroceryItem... groceryItems) {
        Log.d(TAG, "doInBackground: Item inserted -> " +groceryItems[0].getName());
        groceryItemDao.insert(groceryItems[0]);
        return null;
    }
}

/*package com.ethichadebe.brittlefinal.local.scyncTasks;

import android.os.AsyncTask;

import com.ethichadebe.brittle2.local.GroceryItemDao;
import com.ethichadebe.brittle2.local.model.GroceryItem;

public class InsertGroceryItemAsyncTask extends AsyncTask<GroceryItem,Void,Void> {
    private GroceryItemDao groceryItemDao;

    public InsertGroceryItemAsyncTask(GroceryItemDao groceryItemDao){
        this.groceryItemDao = groceryItemDao;
    }

    @Override
    protected Void doInBackground(GroceryItem... groceryItems) {
        groceryItemDao.insert(groceryItems[0]);
        return null;
    }
}*/

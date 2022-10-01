/*package com.ethichadebe.brittlefinal.local.scyncTasks;

import android.os.AsyncTask;

import com.ethichadebe.brittle2.local.GroceryItemDao;
import com.ethichadebe.brittle2.local.model.GroceryItem;

public class UpdateGroceryItemAsyncTask extends AsyncTask<GroceryItem,Void,Void> {
    private GroceryItemDao groceryItemDao;

    public UpdateGroceryItemAsyncTask(GroceryItemDao groceryItemDao){
        this.groceryItemDao = groceryItemDao;
    }

    @Override
    protected Void doInBackground(GroceryItem... groceryItems) {
        groceryItemDao.update(groceryItems[0]);
        return null;
    }
}*/

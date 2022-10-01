/*package com.ethichadebe.brittlefinal.local.scyncTasks;

import android.os.AsyncTask;

public class DeleteAllGroceryItemAsyncTask extends AsyncTask<Void,Void,Void> {
    private GroceryItemDao groceryItemDao;
    private int shopId;

    public DeleteAllGroceryItemAsyncTask(GroceryItemDao groceryItemDao,int shopId){
        this.groceryItemDao = groceryItemDao;
        this.shopId = shopId;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        groceryItemDao.deleteAllGroceryItems(shopId);
        return null;
    }
}*/

package com.ethichadebe.brittlefinal.local.scyncTasks;

import android.os.AsyncTask;

import com.ethichadebe.brittlefinal.local.ShopDao;


public class DeleteAllShopsAsyncTask extends AsyncTask<Void,Void,Void> {
    private ShopDao shopDao;

    public DeleteAllShopsAsyncTask(ShopDao shopDao){
        this.shopDao = shopDao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        shopDao.deleteAllShops();
        return null;
    }
}

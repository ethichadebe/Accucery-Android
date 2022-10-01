package com.ethichadebe.brittlefinal.local.scyncTasks;

import android.os.AsyncTask;

import com.ethichadebe.brittlefinal.local.ShopDao;
import com.ethichadebe.brittlefinal.local.model.Shop;

public class DeleteShopAsyncTask extends AsyncTask<Shop,Void,Void> {
    private ShopDao shopDao;

    public DeleteShopAsyncTask(ShopDao shopDao){
        this.shopDao = shopDao;
    }

    @Override
    protected Void doInBackground(Shop... shops) {
        shopDao.delete(shops[0]);
        return null;
    }
}

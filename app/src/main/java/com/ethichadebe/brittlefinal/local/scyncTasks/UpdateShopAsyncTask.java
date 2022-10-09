package com.ethichadebe.brittlefinal.local.scyncTasks;

import android.os.AsyncTask;

import com.ethichadebe.brittlefinal.local.dao.ShopDao;
import com.ethichadebe.brittlefinal.local.model.Shop;

public class UpdateShopAsyncTask extends AsyncTask<Shop,Void,Void> {
    private ShopDao shopDao;

    public UpdateShopAsyncTask(ShopDao shopDao){
        this.shopDao = shopDao;
    }

    @Override
    protected Void doInBackground(Shop... shops) {
        shopDao.update(shops[0]);
        return null;
    }
}

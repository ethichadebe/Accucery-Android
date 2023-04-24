package com.ethichadebe.brittlefinal.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.local.model.User;
import com.ethichadebe.brittlefinal.repository.PriceCheckRepo;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private final PriceCheckRepo repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new PriceCheckRepo(application);
    }

    public void update(User user){
        repository.updateUser(user);
    }

    public LiveData<User> getUser(){
        return repository.getUser();
    }

}

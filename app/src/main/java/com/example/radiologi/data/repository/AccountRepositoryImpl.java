package com.example.radiologi.data.repository;

import androidx.lifecycle.LiveData;

import com.example.radiologi.data.dataSource.remote.RemoteDataSource;
import com.example.radiologi.data.dataSource.remote.response.LoginResponse;
import com.example.radiologi.utils.Event;
import com.example.radiologi.utils.vo.Resource;

import java.util.Map;

public class AccountRepositoryImpl implements Repository.AccountRepository {

    private final RemoteDataSource.Login remoteDataSource;
    private volatile static AccountRepositoryImpl instance = null;

    public AccountRepositoryImpl(RemoteDataSource.Login remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static AccountRepositoryImpl getInstance(RemoteDataSource.Login remoteDataSource){
        if (instance == null){
            synchronized (AccountRepositoryImpl.class){
                instance = new AccountRepositoryImpl(remoteDataSource);
            }
        }
        return instance;
    }

    @Override
    public LiveData<Event<Resource<LoginResponse>>> loginUser(Map<String, String> params) {
        return remoteDataSource.loginUsers(params);
    }
}

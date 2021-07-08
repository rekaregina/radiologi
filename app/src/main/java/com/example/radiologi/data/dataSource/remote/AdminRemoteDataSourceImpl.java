package com.example.radiologi.data.dataSource.remote;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.example.radiologi.data.dataSource.remote.response.AdminItemResponse;
import com.example.radiologi.data.dataSource.remote.vo.ApiResponse;
import com.example.radiologi.data.dataSource.remote.vo.StatusResponse;
import com.example.radiologi.networking.BaseVolley;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static com.example.radiologi.utils.Constants.ADMIN_DATA;
import static com.example.radiologi.utils.Constants.EMPTY;
import static com.example.radiologi.utils.Constants.NIP;
import static com.example.radiologi.utils.Constants.SUCCESS;

public class AdminRemoteDataSourceImpl implements RemoteDataSource.Admin {

    private final Context context;
    @SuppressLint("StaticFieldLeak")
    private volatile static AdminRemoteDataSourceImpl instance;

    public AdminRemoteDataSourceImpl(Context context) {
        this.context = context;
    }

    public static AdminRemoteDataSourceImpl getInstance(Context context){
        if (instance == null){
            synchronized (AdminRemoteDataSourceImpl.class){
                instance = new AdminRemoteDataSourceImpl(context);
            }
        }
        return instance;
    }

    @Override
    public LiveData<ApiResponse<AdminItemResponse>> getAdminData(String nip) {
        MutableLiveData<ApiResponse<AdminItemResponse>> result = new MutableLiveData<>();
        final Type type = new TypeToken<AdminItemResponse>(){}.getType();
        new BaseVolley<AdminItemResponse>(
                context,
                Request.Method.POST,
                ADMIN_DATA,
                type
        ) {
            @Override
            protected void onLoading() {
            }

            @Override
            protected void onSuccess(AdminItemResponse response) {
                final String status = response.getStatus();
                if (status.equals(SUCCESS)){
                    result.postValue(new ApiResponse<>(
                            StatusResponse.SUCCESS,
                            response,
                            null
                    ));
                } else {
                    result.postValue(new ApiResponse<>(
                            StatusResponse.ERROR,
                            null,
                            EMPTY
                    ));
                }
            }

            @Override
            protected void onError(String message) {
                result.postValue(new ApiResponse<>(
                        StatusResponse.ERROR,
                        null,
                        message
                ));
            }

            @Override
            protected Map<String, String> setParameter() {
                Map<String, String> params = new HashMap<>();
                params.put(NIP, nip);
                return params;
            }
        };
        return result;
    }
}

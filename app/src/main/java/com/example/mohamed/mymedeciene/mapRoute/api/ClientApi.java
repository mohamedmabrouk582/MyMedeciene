package com.example.mohamed.mymedeciene.mapRoute.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 03/01/2018.  time :22:57
 */

public class ClientApi {
    private static  Retrofit retrofit=null;
    private static final String BASEURL="https://maps.googleapis.com";

    public static Retrofit getRetrofit(){
        if (retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

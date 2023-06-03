package com.example.avocado.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingletonRetrofitObject {
    private static SingletonRetrofitObject singletonRetrofitObject;
    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    private static Retrofit retrofit;

    private SingletonRetrofitObject(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    //static으로 고치니 Fragment에서 접근 가능하게 됨. 나중에 ㄱㅊ은지 확인해야함
    public static synchronized SingletonRetrofitObject getSingletonRetrofitObject(){
        if(singletonRetrofitObject == null){
            singletonRetrofitObject = new SingletonRetrofitObject();
        }
        return singletonRetrofitObject;
    }

    public YoutubeAPI getYoutubeAPI(){
        return retrofit.create(YoutubeAPI.class);
    }
}

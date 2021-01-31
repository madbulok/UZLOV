package com.uzlov.developerslife.net;

import com.uzlov.developerslife.repository.model.Post;
import com.uzlov.developerslife.repository.model.ResponsePosts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DevelopersLifeAPI {

    // Получение рандомного поста
    @GET("random?json=true")
    Call<Post> getRandomPost();

    // Получение последних постов с сервера
    @GET("latest/0?json=true")
    Call<ResponsePosts> getLatestPosts();

    // Получение горячих постов с сервера
    @GET("hot/0?json=true")
    Call<ResponsePosts> getHotsPosts();

    // Получение топовых постов с сервера
    @GET("top/0?json=true")
    Call<ResponsePosts> getTopPosts();

}

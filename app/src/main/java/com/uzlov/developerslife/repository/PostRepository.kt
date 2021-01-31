package com.uzlov.developerslife.repository

import android.util.Log
import com.uzlov.developerslife.repository.model.Post
import com.uzlov.developerslife.net.DevelopersLifeAPI
import com.uzlov.developerslife.repository.model.ResponsePosts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostRepository<T>(val load: LoadableObjects<Post>) : ILoadListener<T> {
    private var retrofit: Retrofit
    private var api: DevelopersLifeAPI

    init {
        // init retrofit's objects
        retrofit = Retrofit.Builder()
            .baseUrl("https://developerslife.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(DevelopersLifeAPI::class.java)
    }

    override fun getNewRandomPost() {
        load.loadStart()
        api.randomPost.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    load.loadEnd()
                    load.loadObject(response.body() ?: Post())
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                // show error message!!!
                load.loadError(t.message.toString())
            }
        })
    }

    override fun getTopPosts() {
        api.topPosts.enqueue(object : Callback<ResponsePosts> {
            override fun onResponse(call: Call<ResponsePosts>, response: Response<ResponsePosts>) {
                Log.e("CODE: ", response.code().toString())
                if (response.isSuccessful) {
                    load.loadEnd()
                    load.loadObjects(response.body()?.result ?: emptyList())
                }
            }

            override fun onFailure(call: Call<ResponsePosts>, t: Throwable) {
                load.loadError(t.message.toString())
            }
        })
    }

    override fun getHotsPost() {
        api.hotsPosts.enqueue(object : Callback<ResponsePosts> {
            override fun onResponse(call: Call<ResponsePosts>, response: Response<ResponsePosts>) {
                if (response.isSuccessful) {
                    load.loadEnd()
                    load.loadObjects(response.body()?.result ?: emptyList())
                }
            }

            override fun onFailure(call: Call<ResponsePosts>, t: Throwable) {
                load.loadError(t.message.toString())
            }
        })
    }

    override fun getLastPost() {
        api.latestPosts.enqueue(object : Callback<ResponsePosts> {
            override fun onResponse(call: Call<ResponsePosts>, response: Response<ResponsePosts>) {
                if (response.isSuccessful) {
                    load.loadEnd()
                    load.loadObjects(response.body()?.result ?: emptyList())
                }
            }

            override fun onFailure(call: Call<ResponsePosts>, t: Throwable) {
                load.loadError(t.message.toString())
            }
        })
    }
}
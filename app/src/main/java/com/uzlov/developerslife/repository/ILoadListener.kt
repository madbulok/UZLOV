package com.uzlov.developerslife.repository

interface ILoadListener<T> {

    fun getNewRandomPost()
    fun getTopPosts()
    fun getHotsPost()
    fun getLastPost()

}
package com.uzlov.developerslife.repository


public interface LoadableObjects<T> {
    // control UI
    fun loadStart()
    fun loadEnd()
    fun loadError(message: String)

    // load data to UI
    fun loadObject(objects: T)
    fun loadObjects(objects: Collection<T>)
}
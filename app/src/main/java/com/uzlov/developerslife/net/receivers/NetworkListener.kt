package com.uzlov.developerslife.net.receivers

@FunctionalInterface
interface NetworkListener {
    fun networkStateChanged(isAvailable:Boolean)
}
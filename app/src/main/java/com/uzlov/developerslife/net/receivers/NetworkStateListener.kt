package com.uzlov.developerslife.net.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager


class NetworkStateListener(val networkListener: NetworkListener) : BroadcastReceiver(){
    private lateinit var connectivityManager: ConnectivityManager
    private val TYPE_WIFI = 1
    private val TYPE_MOBILE = 2
    private val TYPE_NOT_CONNECTED = 0
    private val NETWORK_STATUS_NOT_CONNECTED = 0
    private val NETWORK_STATUS_WIFI = 1
    private val NETWORK_STATUS_MOBILE = 2

    override fun onReceive(context: Context, intent: Intent?) {
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        println("START!!!")

        val status = getConnectionState()
        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent?.action ?: ""){
            if (status == NETWORK_STATUS_NOT_CONNECTED){
                networkListener.networkStateChanged(false)
                println("NET NO")
            } else {
                println("NET OK")
                networkListener.networkStateChanged(true)
            }
        }
    }

    private fun getConnectivityStatus(): Int {
        val activeNetwork = connectivityManager.activeNetworkInfo
        if (null != activeNetwork) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) return TYPE_WIFI
            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) return TYPE_MOBILE
        }
        return TYPE_NOT_CONNECTED
    }

    private fun getConnectionState(): Int {
        val conn = getConnectivityStatus()
        var status = 0
        when (conn) {
            TYPE_WIFI -> {
                status = NETWORK_STATUS_WIFI
            }
            TYPE_MOBILE -> {
                status = NETWORK_STATUS_MOBILE
            }
            TYPE_NOT_CONNECTED -> {
                status = NETWORK_STATUS_NOT_CONNECTED
            }
        }
        return status
    }
}

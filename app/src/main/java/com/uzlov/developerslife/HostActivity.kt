package com.uzlov.developerslife

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.uzlov.developerslife.fragments.HotsPostFragment
import com.uzlov.developerslife.fragments.LatestPostFragment
import com.uzlov.developerslife.fragments.RandomPostFragment
import com.uzlov.developerslife.fragments.TopPostFragment
import com.uzlov.developerslife.net.receivers.NetworkListener
import com.uzlov.developerslife.net.receivers.NetworkStateListener
import com.uzlov.developerslife.ui.main.SectionsPagerAdapter


class HostActivity : AppCompatActivity(){
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var broadcastReceiver : NetworkStateListener
    private lateinit var stubMetIsNotAvailable : LinearLayout
    private lateinit var frameLayout: FrameLayout
    private lateinit var mOpenSettingsBtn : Button

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        // слушатель состояния интернета
        broadcastReceiver = NetworkStateListener(object : NetworkListener {
            override fun networkStateChanged(isAvailable: Boolean) {
                updateStateNetwork(isAvailable)
            }
        })
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.addFragment(RandomPostFragment.newInstance(0), R.string.tab_text_0)
        sectionsPagerAdapter.addFragment(TopPostFragment.newInstance(1), R.string.tab_text_1)
        sectionsPagerAdapter.addFragment(LatestPostFragment.newInstance(2), R.string.tab_text_2)
        sectionsPagerAdapter.addFragment(HotsPostFragment.newInstance(3), R.string.tab_text_3)

        viewPager = findViewById(R.id.view_pager)
        stubMetIsNotAvailable = findViewById(R.id.no_connection_layout)
        mOpenSettingsBtn = findViewById(R.id.btnOpenSetting)
        frameLayout = findViewById(R.id.barLayout)
        viewPager.adapter = sectionsPagerAdapter


        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        // открыть настройки сети
        mOpenSettingsBtn.setOnClickListener {
            openSettings()
        }
    }

    private fun openSettings(){
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        startActivity(intent)
    }

    fun updateStateNetwork(isAvailable: Boolean) {
        runOnUiThread {
            if (isAvailable) {
                sectionsPagerAdapter.reconnectAll() // получаем данные с сервера при появлении интернета
                stubMetIsNotAvailable.visibility = View.INVISIBLE
                viewPager.visibility = View.VISIBLE

            } else {
                stubMetIsNotAvailable.visibility = View.VISIBLE
                viewPager.visibility = View.INVISIBLE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        sectionsPagerAdapter.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        sectionsPagerAdapter.onRestoreInstanceState(savedInstanceState)
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }
}
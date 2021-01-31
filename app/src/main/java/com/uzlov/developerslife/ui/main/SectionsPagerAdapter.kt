package com.uzlov.developerslife.ui.main

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.uzlov.developerslife.repository.ReConnectable

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = mutableListOf<ReConnectable>()
    private val names = mutableListOf<Int>()

    fun addFragment(fragment:ReConnectable, nameStringId:Int){
        fragments.add(fragment)
        names.add(nameStringId)
    }
    override fun getItem(position: Int): Fragment {
        return fragments[position] as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(names[position])
    }

    override fun getCount(): Int {
        return names.size
    }

    // вызов при появлении сети
    fun reconnectAll(){
        fragments.forEach {
            it.reconnect()
        }
    }

    fun onRestoreInstanceState(out:Bundle) {
        fragments.forEach {
            (it as Fragment).onViewStateRestored(out)
        }
    }

    fun onSaveInstanceState(out:Bundle) {
        fragments.forEach {
            (it as Fragment).onSaveInstanceState(out)
        }
    }
}
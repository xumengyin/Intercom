package com.jerry.intercom.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jerry.intercom.ui.IntercomFragment
import com.jerry.intercom.ui.MainFragment
import com.jerry.intercom.ui.SettingFragment

const val tab1 = 0
const val tab2 = 1
const val tab3 = 2

class MainAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragments = mapOf(
        tab1 to { MainFragment() },
        tab2 to { IntercomFragment() },
        tab3 to { SettingFragment() }
    )

    override fun getItemCount(): Int {

        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]?.invoke()?: throw Exception()
    }
}
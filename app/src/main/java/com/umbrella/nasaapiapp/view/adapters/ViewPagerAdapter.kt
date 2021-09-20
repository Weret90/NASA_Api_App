package com.umbrella.nasaapiapp.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.umbrella.nasaapiapp.view.fragments.MeteorsFragment
import com.umbrella.nasaapiapp.view.fragments.PhotoFromMarsFragment
import com.umbrella.nasaapiapp.view.fragments.PictureFragment

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val fragments = arrayOf(PictureFragment(), PhotoFromMarsFragment(), MeteorsFragment())

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }
}
package com.umbrella.nasaapiapp.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.umbrella.nasaapiapp.R
import com.umbrella.nasaapiapp.ui.view.adapters.ViewPagerAdapter

class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dotsIndicator = view.findViewById<DotsIndicator>(R.id.dots_indicator)
        val viewPager: ViewPager = view.findViewById(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(childFragmentManager)
        dotsIndicator.setViewPager(viewPager)
    }
}
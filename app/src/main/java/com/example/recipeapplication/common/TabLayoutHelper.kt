package com.example.recipeapplication.common

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

object TabLayoutHelper {
    // TabLayout'u ve ViewPager'ı ayarlayan fonksiyon
    fun setupTabs(tabLayout: TabLayout, viewPager: ViewPager2, titles: List<String>, context: Context) {
        // TabLayout için tab'ları oluşturuyoruz
        for (title in titles) {
            val tab: TabLayout.Tab = tabLayout.newTab()
            val tabTextView = TextView(context).apply {
                text = title
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                gravity = Gravity.CENTER
                isAllCaps = true
                setTextColor(Color.BLACK)
            }
            tab.customView = tabTextView
            tabLayout.addTab(tab)
        }

        // TabLayout ile ViewPager2'yi birbirine bağlıyoruz
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewPager.currentItem = it.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // ViewPager2 ile TabLayout'u senkronize ediyoruz
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}
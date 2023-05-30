package com.rizfadh.githubers.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rizfadh.githubers.ui.detail.FollowFragment

class FollowPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username = ""

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_POSITION, position)
            putString(FollowFragment.ARG_USERNAME, username)
        }
        return fragment
    }
}
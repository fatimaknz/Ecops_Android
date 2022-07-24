package com.offline.ecop.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.offline.ecop.fragment.ComplaintFragment
import com.offline.ecop.fragment.CriminalListFragment
import com.offline.ecop.fragment.PoliceListFragment

class AdministratorViewPagerAdapter(
    private val fm: FragmentManager,
    private val mTabTitleList: List<String>
) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return ComplaintFragment()
        } else if (position == 1) {
            return PoliceListFragment()
        } else {
            return CriminalListFragment()
        }
    }

    override fun getCount(): Int {
        return mTabTitleList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTabTitleList[position]
    }
}
package com.jans.societyoo.ui

import androidx.fragment.app.Fragment
import com.jans.societyoo.ui.login.FlatsFragment

interface FragmentSwitcher {
    fun siwtchFragment(fragment: Fragment, addToBackStack: Boolean)
}
package com.jans.societyoo.ui.navigation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jans.societyoo.R
import com.jans.societyoo.ui.navigation.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class NotificationDetailsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification_details, container, false)
    }

    override fun onAttach(context: Context) {
      //  (activity as MainActivity)?.hideBottomNavigation()
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
       // (activity as MainActivity)?.showBottomNavigation()
    }

}
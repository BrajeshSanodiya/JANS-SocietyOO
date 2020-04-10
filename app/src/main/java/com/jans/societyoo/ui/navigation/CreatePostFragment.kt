package com.jans.societyoo.ui.navigation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.jans.societyoo.R

/**
 * A simple [Fragment] subclass.
 */
class CreatePostFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_post, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        true
    }
}

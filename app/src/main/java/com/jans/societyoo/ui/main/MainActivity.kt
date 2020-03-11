package com.jans.societyoo.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jans.societyoo.R
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.model.User
import com.jans.societyoo.model.UserPostData
import com.jans.societyoo.ui.FragmentSwitcher
import com.jans.societyoo.ui.login.FlatsFragment
import com.jans.societyoo.ui.login.LoginActivity
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.viewmodel.MainActivityViewModel
import com.jans.tracking.PropertyName
import com.jans.tracking.Tracking
import com.jans.tracking.TrackingOptions


class MainActivity : AppCompatActivity() , FragmentSwitcher{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main)

        //val actionBar: ActionBar? = supportActionBar
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        supportActionBar!!.setLogo(R.drawable.header_logo);
        supportActionBar!!.setDisplayUseLogoEnabled(true);

        //startActivity(Intent(this, LoginActivity::class.java))s

       // PrintMsg.toastDebug(this,""+preferences.flats)
        addFragments(MainFragment.newInstance(),false)
    }



    fun addFragments(fragment: Fragment, addToBackStack: Boolean) {
        // Get the support fragment manager instance
        val manager = supportFragmentManager
        // Begin the fragment transition using support fragment manager
        val transaction = manager.beginTransaction()
        // Replace the fragment on container
        transaction.replace(R.id.fragment_container,fragment)
       if(addToBackStack)
            transaction.addToBackStack(fragment::class.java.name)

        // Finishing the transition
        transaction.commit()
    }

    override fun siwtchFragment(fragment: Fragment, addToBackStack: Boolean) {
        addFragments(fragment,addToBackStack)
    }
}

package com.jans.societyoo.ui.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jans.societyoo.R
import com.jans.societyoo.ui.FragmentSwitcher


class DashboardActivity : AppCompatActivity() , FragmentSwitcher{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_dashboard)

        //val actionBar: ActionBar? = supportActionBar
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        supportActionBar!!.setLogo(R.drawable.header_logo);
        supportActionBar!!.setDisplayUseLogoEnabled(true);

        //startActivity(Intent(this, LoginActivity::class.java))s

       // PrintMsg.toastDebug(this,""+preferences.flats)
        addFragments(DashboardFragment.newInstance(),false)
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

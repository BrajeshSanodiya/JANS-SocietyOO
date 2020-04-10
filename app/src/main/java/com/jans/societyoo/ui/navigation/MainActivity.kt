package com.jans.societyoo.ui.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.os.postDelayed
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jans.societyoo.R
import com.jans.societyoo.utils.PrintMsg
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
    }


    fun setupViews()
    {
        // Finding the Navigation Controller
        var navController = findNavController(R.id.fragNavHost)

        bottomNavView.setupWithNavController(navController)

        // Setting Up ActionBar with Navigation Controller
       // setupActionBarWithNavController(navController)

        // Pass the IDs of top-level destinations in AppBarConfiguration
        var appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf (
                R.id.homeFragment,
                R.id.createPostFragment,
                R.id.providerPostFragment,
                R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    private var backPressedOnce = false
    override fun onBackPressed()
    {
        var navController = findNavController(R.id.fragNavHost)
        if (navController.graph.startDestination == navController.currentDestination?.id)
        {
            if (backPressedOnce)
            {
                super.onBackPressed()
                return
            }
            backPressedOnce = true
            PrintMsg.toast(this, "Press BACK again to exit")
            Handler().postDelayed(2000) { backPressedOnce = false }
        }
        else { super.onBackPressed() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

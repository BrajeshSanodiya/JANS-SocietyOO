package com.jans.societyoo.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jans.societyoo.R
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.ui.FragmentSwitcher
import com.jans.societyoo.ui.dashboard.DashboardActivity
import com.jans.societyoo.viewmodel.LoginViewModel
import com.jans.societyoo.viewmodel.LoginViewModelFactory


class LoginActivity : AppCompatActivity(), FragmentSwitcher {
    var preferences = UserPreferences(this)
    private lateinit var loginViewModel: LoginViewModel
    // private var nonSwipeableViewPager: NonSwipeableViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /* nonSwipeableViewPager = findViewById(R.id.fragment_container)
         nonSwipeableViewPager!!.offscreenPageLimit=0
         nonSwipeableViewPager!!.adapter = NonSwipeableLoginPagerAdapter(supportFragmentManager)*/

        loginViewModel = ViewModelProvider(
            viewModelStore,
            LoginViewModelFactory(this)
        ).get(LoginViewModel::class.java)
        loginViewModel.loginViewState.observe(this, Observer {
            val loginEventState = it ?: return@Observer
            changeFragment(loginEventState.fragmentState)
        })

        changeFragment(LoginFragmentState.MOBILE_INPUT)
    }

    fun addFragments(fragment: Fragment, addToBackStack: Boolean) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()

        //manager!!.findFragmentById(R.id.fragment_container_login)?.let { transaction.remove(it) }

        if (addToBackStack)
            transaction.addToBackStack(fragment::class.java.name)
        transaction.replace(R.id.fragment_container_login, fragment, fragment::class.java.name)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)


        transaction.commit()
    }

    override fun siwtchFragment(fragment: Fragment, addToBackStack: Boolean) {
        addFragments(fragment, addToBackStack)
    }

    fun changeFragment(fragmentState: Int) {
        when (fragmentState) {
            LoginFragmentState.MOBILE_INPUT -> {
                siwtchFragment(MobileFragment.newInstance(isFromLogin = true), false)
            }
            LoginFragmentState.OTP_VERIFY -> {
                //Constants.autoOTPSendAllow=true
                siwtchFragment(OTPFragment.newInstance(isFromLogin = true), true)
            }
            LoginFragmentState.FLAT_CONFIRM -> {
                siwtchFragment(FlatsFragment.newInstance(isFromLogin = true), false)
            }
            LoginFragmentState.USER_PROFILE -> {
                siwtchFragment(UserProfileFragment.newInstance(isFromLogin = true), true)
            }
            LoginFragmentState.AFTER_LOGIN -> {
                var flatsDetail = loginViewModel.flatsDetailLiveData.value
                var mobile = flatsDetail?.get(0)!!.umMobile
                var userDetail = loginViewModel.userDetailLiveData.value
                UserPreferences::flatsDetail.set(preferences, flatsDetail.toString());
                UserPreferences::userDetail.set(preferences, userDetail.toString());
                UserPreferences::mobileNum.set(preferences, mobile);

                startActivity(Intent(this, DashboardActivity::class.java))
                finish();
            }
        }
    }

/*    class MyAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {
        private val NUM_ITEMS = 4
        override fun getCount(): Int {
            return NUM_ITEMS
        }
        override fun getItem(position: Int): Fragment {
            return when (position) {
                LoginFragmentState.MOBILE_INPUT -> return MobileFragment()
                LoginFragmentState.OTP_VERIFY -> return OTPFragment()
                LoginFragmentState.FLAT_CONFIRM -> return FlatsFragment()
                LoginFragmentState.USER_PROFILE -> return  UserProfileFragment()
                else ->return MobileFragment()
            }
        }
    }

    private inner class NonSwipeableLoginPagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val NUM_ITEMS = 4
        override fun getCount(): Int = NUM_ITEMS

        override fun getItem(position: Int): Fragment {
            return when (position) {
               0 -> return MobileFragment()
                1 -> return OTPFragment()
                2 -> return FlatsFragment.newInstance(isFromLogin = true)
                3 -> return  UserProfileFragment.newInstance(isFromLogin = true)
                else ->return UserProfileFragment.newInstance(isFromLogin = true)
            }
        }
    }*/


    /*override fun onBackPressed() {
        if(nonSwipeableViewPager!!.currentItem==1){
            loginViewModel.loginFragmentChanged(LoginFragmentState.MOBILE_INPUT)
        }else
        super.onBackPressed()

    }*/

    override fun onBackPressed() {
        super.onBackPressed()
        //finish()
       /* if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack(
                MobileFragment::class.java.name,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }*/
    }

}



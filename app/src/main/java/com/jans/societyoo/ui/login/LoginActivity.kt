package com.jans.societyoo.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.jans.societyoo.R
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.model.login.FlatDetail
import com.jans.societyoo.ui.MainActivity
import com.jans.societyoo.ui.customviews.NonSwipeableViewPager
import com.jans.societyoo.viewmodel.LoginViewModel
import com.jans.societyoo.viewmodel.LoginViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class LoginActivity : AppCompatActivity() {
    var preferences= UserPreferences(this)
    private lateinit var loginViewModel: LoginViewModel
    private var nonSwipeableViewPager: NonSwipeableViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        nonSwipeableViewPager = findViewById(R.id.login_pager)
        nonSwipeableViewPager!!.offscreenPageLimit=0
        nonSwipeableViewPager!!.adapter = NonSwipeableLoginPagerAdapter(supportFragmentManager)

        loginViewModel = ViewModelProvider(viewModelStore,
            LoginViewModelFactory()
        ).get(LoginViewModel::class.java)
        loginViewModel.loginViewState.observe(this, Observer {
            val loginEventState = it ?: return@Observer
                changeFragment(loginEventState.fragmentState)
        })
    }

    fun changeFragment(fragmentState: Int){
        if(fragmentState==LoginFragmentState.MOBILE_INPUT){
            nonSwipeableViewPager!!.currentItem=0
        }else if(fragmentState==LoginFragmentState.OTP_VERIFY){
            nonSwipeableViewPager!!.currentItem=1
        }else if(fragmentState==LoginFragmentState.FLAT_CONFIRM){
            nonSwipeableViewPager!!.currentItem=2
        }else if(fragmentState==LoginFragmentState.USER_PROFILE){
            nonSwipeableViewPager!!.currentItem=3
        }else if(fragmentState==LoginFragmentState.AFTER_LOGIN){

            var flatsDetail=loginViewModel.flatsDetailLiveData.value
            var mobile= loginViewModel.mobileNumberLiveData.value
            var userDetail= loginViewModel.userDetailLiveData.value
            UserPreferences::flatsDetail.set(preferences,flatsDetail.toString());
            UserPreferences::userDetail.set(preferences,userDetail.toString());
            UserPreferences::mobileNum.set(preferences,mobile);

            startActivity(Intent(this,MainActivity::class.java))
            finish();
        }
    }
    class MyAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {
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
                2 -> return FlatsFragment()
                3 -> return  UserProfileFragment()
                else ->return UserProfileFragment()
            }
        }
    }

    override fun onBackPressed() {
        if(nonSwipeableViewPager!!.currentItem==1){
            loginViewModel.loginFragmentChanged(LoginFragmentState.MOBILE_INPUT)
        }else
        super.onBackPressed()

    }
}



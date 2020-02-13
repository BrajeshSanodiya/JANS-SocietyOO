package com.jans.societyoo.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jans.societyoo.R
import com.jans.societyoo.ui.customviews.NonSwipeableViewPager


class LoginActivity : AppCompatActivity(), LoginCallbackListener {

    private lateinit var loginViewModel: LoginViewModel
    private var nonSwipeableViewPager: NonSwipeableViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        nonSwipeableViewPager = findViewById(R.id.login_pager)
        nonSwipeableViewPager!!.adapter = NonSwipeableLoginPagerAdapter(supportFragmentManager)

        loginViewModel = ViewModelProvider(
            viewModelStore,
            LoginViewModelFactory()
        ).get(LoginViewModel::class.java)

        loginViewModel.loginViewState.observe(this, Observer {
            val loginEventState = it ?: return@Observer
                changeFragment(loginEventState.fragmentState!!)
        })

    }
    fun changeFragment(fragmentState: Int){
        if(fragmentState==LoginState.MOBILE_INPUT){
            nonSwipeableViewPager!!.currentItem=0
        }else if(fragmentState==LoginState.OTP_VERIFY){
            nonSwipeableViewPager!!.currentItem=1
        }
    }

    private inner class NonSwipeableLoginPagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = 2

        override fun getItem(position: Int): Fragment {
            if (position == 0) {
                return MobileFragment()
            } else {
                return OTPFragment()
            }
        }
    }

    override fun onMobileOTPSend(mobileNumber: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    override fun onMobileOtpResend() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMobileOtpVerify(application: String?, otp: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}


/*fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}*/



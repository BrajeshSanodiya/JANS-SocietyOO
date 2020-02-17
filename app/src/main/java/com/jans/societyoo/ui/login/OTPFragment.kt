package com.jans.societyoo.ui.login


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jans.otpview.OnOtpCompletionListener
import com.jans.societyoo.R
import com.jans.societyoo.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.loading
import kotlinx.android.synthetic.main.fragment_otp.*
import kotlinx.android.synthetic.main.fragment_otp.view.*

/**
 * A simple [Fragment] subclass.
 */
class OTPFragment : Fragment(), OnOtpCompletionListener {

    private lateinit var loginViewModel: LoginViewModel
    var mobileNumber:String?=null;
    var otpValue:String?=null;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView =inflater.inflate(R.layout.fragment_otp, container, false);

        val otpView=rootView.otp_view
        val btnNext = rootView.btnNext
        val btnResend = rootView.btnResend
        val loading = rootView.loading

        otpView.setOtpCompletionListener(this)

        loginViewModel = ViewModelProvider(activity!!.viewModelStore, LoginViewModelFactory()).get(LoginViewModel::class.java)
        loginViewModel.loginOtpViewState.observe(this@OTPFragment, Observer {
            val mobileState = it ?: return@Observer
            btnNext.isEnabled = mobileState.isDataValid
            if (mobileState.otpError!= null) {
                //etMobile.error = getString(mobileState.mobileNumberError)
            }
            if(mobileState.isOtpResend){
                callOTPResend()
            }
            if(!TextUtils.isEmpty(mobileState.otpValue)){
                otpValue=mobileState.otpValue
            }
        })

        loginViewModel.mobileNumberLiveData.observe(this, Observer {
            mobileNumber=it
        })


        btnNext.setOnClickListener{
            Toast.makeText(context, "OTP Value : $otpValue", Toast.LENGTH_SHORT).show()
            context!!.startActivity(Intent(context,MainActivity::class.java))
        }

        btnResend.setOnClickListener{
            loginViewModel.otpResend()
        }
        return rootView
    }

    private fun callOTPResend() {
        Toast.makeText(context, mobileNumber, Toast.LENGTH_SHORT).show()
    }

    override fun onOtpCompleted(otp: String?) {
        loginViewModel.showOtpNextButton(otp,true)
    }


}

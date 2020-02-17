package com.jans.societyoo.ui.login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jans.otpview.OnOtpCompletionListener
import com.jans.societyoo.R
import com.jans.societyoo.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_otp.*
import kotlinx.android.synthetic.main.fragment_otp.view.*


/**
 * A simple [Fragment] subclass.
 */
class OTPFragment : Fragment(), OnOtpCompletionListener {

    private lateinit var loginViewModel: LoginViewModel
    var mobileNumber: String? = null
    var otpValue: String? = null
    var timerView:TextView?=null
    override fun onResume() {
        super.onResume()
        resentOtpTimmer()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_otp, container, false);

        val otpView = rootView.otp_view
        val btnNext = rootView.btnNext
        val btnResend = rootView.btnResend
        timerView=rootView.tvTimer
        val loading = rootView.loading

        otpView.setOtpCompletionListener(this)
        loginViewModel = ViewModelProvider(
            activity!!.viewModelStore,
            LoginViewModelFactory()
        ).get(LoginViewModel::class.java)
        loginViewModel.loginOtpViewState.observe(this@OTPFragment, Observer {
            val mobileState = it ?: return@Observer
            btnNext.isEnabled = mobileState.isDataValid
            //btnResend.isEnabled=mobileState.isDataValid
            if (mobileState.otpError != null) {
                //etMobile.error = getString(mobileState.mobileNumberError)
            }
            if (mobileState.isOtpResend) {
                callOTPResend()
            }
            if (!TextUtils.isEmpty(mobileState.otpValue)) {
                otpValue = mobileState.otpValue
            }
        })

        loginViewModel.mobileNumberLiveData.observe(this, Observer {
            mobileNumber = it
        })


        otpView.apply{
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                    {
                        Toast.makeText(context, "OTP : $otpValue", Toast.LENGTH_SHORT).show()
                        context!!.startActivity(Intent(context, MainActivity::class.java))
                    }

                }
                false
            }
        }

        btnNext.setOnClickListener {
            Toast.makeText(context, "OTP : $otpValue", Toast.LENGTH_SHORT).show()
            context!!.startActivity(Intent(context, MainActivity::class.java))
        }

        btnResend.setOnClickListener {
            loginViewModel.otpResend()
        }
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }
    private fun callOTPResend() {
        Toast.makeText(context, "OTP Resend to $mobileNumber", Toast.LENGTH_SHORT).show()
        btnResend.isEnabled = false
        resentOtpTimmer()
    }

    override fun onOtpCompleted(otp: String?) {
        loginViewModel.showOtpNextButton(otp, true)
    }

    override fun onOtpInComplete() {
        loginViewModel.hideNextButton()
    }

    fun resentOtpTimmer() {
       /* val handler = Handler()
        handler.postDelayed(Runnable {
            btnResend.isEnabled = true
        }, 100)*/
        object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (timerView!!.getVisibility() != View.VISIBLE) {
                    timerView!!.visibility=View.VISIBLE
                }
                tvTimer.text="wait ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                btnResend.isEnabled = true
                timerView!!.visibility=View.INVISIBLE
            }
        }.start()
    }




}

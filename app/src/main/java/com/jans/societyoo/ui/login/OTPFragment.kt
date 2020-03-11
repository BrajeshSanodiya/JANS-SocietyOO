package com.jans.societyoo.ui.login


import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.GoogleApiClient
import com.jans.otpview.OnOtpCompletionListener
import com.jans.otpview.OtpView
import com.jans.societyoo.MySMSBroadcastReceiver
import com.jans.societyoo.R
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.model.ApiDataObject
import com.jans.societyoo.model.login.UserData
import com.jans.societyoo.model.login.SendOTPData
import com.jans.societyoo.ui.main.MainActivity
import com.jans.societyoo.utils.Constants
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.PrintMsg
import com.jans.societyoo.viewmodel.LoginViewModel
import com.jans.societyoo.viewmodel.LoginViewModelFactory
import kotlinx.android.synthetic.main.fragment_otp.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 */
class OTPFragment : Fragment(), OnOtpCompletionListener, MySMSBroadcastReceiver.OTPReceiveListener  {


    var mCredentialsApiClient: GoogleApiClient? = null
    val smsBroadcast = MySMSBroadcastReceiver()
    var preferences: UserPreferences? = null
    private lateinit var loginViewModel: LoginViewModel
    var mobileNumber: String? = null
    var otpValue: String? = null
    var timerView: TextView? = null
    var btnResend: TextView? = null
    var otpView: OtpView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCredentialsApiClient = GoogleApiClient.Builder(requireActivity())
            .addApi(Auth.CREDENTIALS_API)
            .build()

        preferences = UserPreferences(requireContext())
        Constants.autoOTPSendAllow=true
    }

    override fun onStart() {
        super.onStart()
        startSMSListener()
        smsBroadcast.initOTPListener(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)

        requireContext().applicationContext.registerReceiver(smsBroadcast, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        if (smsBroadcast != null && context != null) {
            LocalBroadcastManager.getInstance(requireContext().applicationContext)
                .unregisterReceiver(smsBroadcast)
        }
    }
    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_otp, container, false);

        otpView = rootView.otp_view
        val btnNext = rootView.btnNext
        btnResend = rootView.btnResend
        timerView = rootView.tvTimer
        val loading = rootView.loading

        otpView!!.setOtpCompletionListener(this)
        loginViewModel = ViewModelProvider(
            requireActivity().viewModelStore,
            LoginViewModelFactory()
        ).get(LoginViewModel::class.java)
        loginViewModel.loginOtpViewState.observe(viewLifecycleOwner, Observer {
            val mobileState = it ?: return@Observer
            btnNext.isEnabled = mobileState.isDataValid
            if (btnNext.isEnabled) {
                GlobalScope.launch(Dispatchers.Main) {
                    delay(1000)
                    nextBtnClick()
                }
            }
            if (mobileState.otpError != null) {
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


        otpView!!.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        Toast.makeText(context, "OTP : $otpValue", Toast.LENGTH_SHORT).show()
                        requireContext().startActivity(Intent(context, MainActivity::class.java))
                    }
                }
                false
            }
        }

        btnNext.setOnClickListener {
            nextBtnClick()
        }

        btnResend!!.setOnClickListener {
            loginViewModel.otpResend()
        }
        return rootView
    }

    override fun onResume() {
        super.onResume()
        if(Constants.autoOTPSendAllow){
            callOTPResend()
            Constants.autoOTPSendAllow=false
        }
    }

    private fun nextBtnClick() {
        loginViewModel.verifyOtp(mobileNumber!!, otpValue!!).observe(viewLifecycleOwner, Observer {
            val result = it
            if (result is MyResult.Success) {
                val data: ApiDataObject<UserData> = result.data
                if (data.dis_msg == 1 && !TextUtils.isEmpty(data.msg))
                    PrintMsg.toast(context, data.msg);
                if (data.success_stat == 1) {
                    if (data.data_details.flatsDetails.size > 0) {
                        PrintMsg.println("flatDetails : " + data.data_details.flatsDetails.size)
                        if (data.data_details.flatsDetails.size > 1)
                        {
                            loginViewModel.setFlatsUsers(data.data_details.flatsDetails,data.data_details.userDetails,data.data_details.flatsDetails.get(0).umMobile)
                            loginViewModel.loginFragmentChanged(LoginFragmentState.FLAT_CONFIRM)
                        }
                        else if (data.data_details.userDetails == null && data.data_details.userDetails.profileId==0){
                            loginViewModel.setFlatsUsers(data.data_details.flatsDetails,data.data_details.userDetails,data.data_details.flatsDetails.get(0).umMobile)
                            loginViewModel.loginFragmentChanged(LoginFragmentState.USER_PROFILE)
                        }
                        else{
                            loginViewModel.openAfterLoginScreen()
                        }
                    }
                }
            } else if (result is MyResult.Error) {
                PrintMsg.toastDebug(context, result.message)
            }
        })

    }

    private fun callOTPResend() {

        loginViewModel.sendOtp(mobileNumber!!).observe(viewLifecycleOwner, Observer {
            val result = it
            if (result is MyResult.Success) {
                val data: ApiDataObject<SendOTPData> = result.data
                if (data.dis_msg == 1 && !TextUtils.isEmpty(data.msg))
                    PrintMsg.toast(context, data.msg);
                if (data.success_stat == 1) {
                    PrintMsg.toastDebug(context, "Your OTP is ${data.data_details.data_details}")
                    PrintMsg.println("Your OTP is ${data.data_details.data_details}")
                    btnResend!!.isEnabled = false
                    resentOtpTimmer()
                }
            } else if (result is MyResult.Error) {
                PrintMsg.toastDebug(context, result.message)
            }
        })
    }


    override fun onOtpCompleted(otp: String?) {
        loginViewModel.showOtpNextButton(otp, true)
    }

    override fun onOtpInComplete() {
        loginViewModel.hideNextButton()
    }

    fun resentOtpTimmer() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (timerView!!.getVisibility() != View.VISIBLE) {
                    timerView!!.visibility = View.VISIBLE
                }
                timerView!!.text = "Waiting for the OTP ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                btnResend!!.isEnabled = true
                timerView!!.visibility = View.INVISIBLE
            }
        }.start()
    }

    private fun startSMSListener() {

        val client = SmsRetriever.getClient(requireContext() /* context */)
        val task = client.startSmsRetriever()
        task.addOnSuccessListener {
            // Successfully started retriever, expect broadcast intent
            // ...
            // otpTxtView.text = "Waiting for the OTP"
            //Toast.makeText(context, "SMS Retriever starts", Toast.LENGTH_LONG).show()
        }

        task.addOnFailureListener {
            // otpTxtView.text = "Cannot Start SMS Retriever"
           //Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
        }
    }


    override fun onOTPReceived(otp: String) {
        if (smsBroadcast != null && context != null) {
            LocalBroadcastManager.getInstance(requireContext().applicationContext)
                .unregisterReceiver(smsBroadcast)
            PrintMsg.println("OTP Received " +otp)
            otpView!!.setText(otp)
        }

    }

    override fun onOTPTimeOut() {
        //otpTxtView.setText("Timeout")
        PrintMsg.println("SMS retriever API Timeout")
    }


}

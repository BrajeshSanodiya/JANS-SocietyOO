package com.jans.societyoo.ui.login


import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
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
import com.jans.societyoo.model.ApiDataWithOutObject
import com.jans.societyoo.model.login.UserData
import com.jans.societyoo.ui.navigation.MainActivity
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

private const val ARG_IS_FROM_LOGIN = "is_from_login"

class OTPFragment : Fragment(), OnOtpCompletionListener, MySMSBroadcastReceiver.OTPReceiveListener {


    var mCredentialsApiClient: GoogleApiClient? = null
    val smsBroadcast = MySMSBroadcastReceiver()
    var preferences: UserPreferences? = null
    private lateinit var loginViewModel: LoginViewModel
    var mobileNumber: String? = null
    var otpValue: String? = null
    var timerView: TextView? = null
    var btnResend: TextView? = null
    var otpView: OtpView? = null
    var progressBar:ProgressBar? =null
    private var isFromLogin: Boolean = false

    companion object {
        @JvmStatic
        fun newInstance(isFromLogin: Boolean) =
            OTPFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_FROM_LOGIN, isFromLogin)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isFromLogin = it.getBoolean(ARG_IS_FROM_LOGIN)
        }
        mCredentialsApiClient = GoogleApiClient.Builder(requireActivity())
            .addApi(Auth.CREDENTIALS_API)
            .build()

        preferences = UserPreferences(requireContext())
        //Constants.autoOTPSendAllow=true
    }


    /* override fun onStart() {
         super.onStart()
         startSMSListener()
         smsBroadcast.initOTPListener(this)
         val intentFilter = IntentFilter()
         intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)

         requireContext().applicationContext.registerReceiver(smsBroadcast, intentFilter)
     }
 */
/*    override fun onStop() {
        super.onStop()
        if (smsBroadcast != null && context != null) {
            LocalBroadcastManager.getInstance(requireContext().applicationContext)
                .unregisterReceiver(smsBroadcast)
        }
    }*/


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
        progressBar = rootView.progress_bar
        otpView!!.setOtpCompletionListener(this)
        loginViewModel = ViewModelProvider(
            requireActivity().viewModelStore,
            LoginViewModelFactory(requireActivity())
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

        startSMSListener()
        smsBroadcast.initOTPListener(this)
        GlobalScope.launch(Dispatchers.Main) {
            delay(500)
            callOTPResend()
        }
        return rootView
    }

    fun setProgressBarVisibility(visible:Boolean){
        if(progressBar!=null){
            if(visible){
                progressBar!!.visibility=View.VISIBLE
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }else{
                progressBar!!.visibility=View.GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
    }

    override fun onResume() {
        super.onResume()
        /*if(Constants.autoOTPSendAllow){
            callOTPResend()
            Constants.autoOTPSendAllow=false
        }*/
        if (smsBroadcast != null && context != null) {
            requireContext().applicationContext.registerReceiver(
                smsBroadcast,
                IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (smsBroadcast != null && context != null) {
            LocalBroadcastManager.getInstance(requireContext().applicationContext)
                .unregisterReceiver(smsBroadcast)
        }
    }

    private fun nextBtnClick() {
        setProgressBarVisibility(true)
        loginViewModel.verifyOtp(mobileNumber!!, otpValue!!).observe(viewLifecycleOwner, Observer {
            val result = it
            if (result is MyResult.Success) {
                val data: ApiDataObject<UserData> = result.data
                if (data.dis_msg == 1 && !TextUtils.isEmpty(data.msg))
                    PrintMsg.toast(context, data.msg);
                if (data.success_stat == 1) {
                    if (data.data_details.flatsDetails.size > 0) {
                        loginViewModel.setFlatDetailsDB(result.data.data_details.flatsDetails)
                        loginViewModel.setUserDetailDB(result.data.data_details.userDetails)
                        GlobalScope.launch(Dispatchers.Main) {
                            delay(500)
                            setProgressBarVisibility(false)
                            if (data.data_details.flatsDetails.size > 1) {
                                loginViewModel.loginFragmentChanged(LoginFragmentState.FLAT_CONFIRM)
                            } else if (data.data_details.userDetails == null && data.data_details.userDetails.profileId == 0) {
                                loginViewModel.loginFragmentChanged(LoginFragmentState.USER_PROFILE)
                            } else {
                                loginViewModel.openAfterLoginScreen()
                            }
                        }
                    }else{
                        setProgressBarVisibility(false)
                    }
                }
                else{
                    setProgressBarVisibility(false)
                }
            } else if (result is MyResult.Error) {
                setProgressBarVisibility(false)
                PrintMsg.toastDebug(context, result.message)
            }
        })

    }

    private fun callOTPResend() {
        setProgressBarVisibility(true)
        loginViewModel.sendOtp(mobileNumber!!).observe(viewLifecycleOwner, Observer {
            val result = it
            if (result is MyResult.Success) {
                setProgressBarVisibility(false)
                val data: ApiDataWithOutObject = result.data
                if (data.dis_msg == 1 && !TextUtils.isEmpty(data.msg))
                    PrintMsg.toast(context, data.msg);
                if (data.success_stat == 1) {
                    btnResend!!.isEnabled = false
                    resentOtpTimmer()
                }
            } else if (result is MyResult.Error) {
                setProgressBarVisibility(false)
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
            PrintMsg.println("OTP Received " + otp)
            otpView!!.setText(otp)
        }

    }

    override fun onOTPTimeOut() {
        //otpTxtView.setText("Timeout")
        PrintMsg.println("SMS retriever API Timeout")
    }


}

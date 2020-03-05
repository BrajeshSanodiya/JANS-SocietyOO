package com.jans.societyoo.ui.login


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import com.jans.societyoo.R
import com.jans.societyoo.viewmodel.LoginViewModel
import com.jans.societyoo.viewmodel.LoginViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MobileFragment : Fragment() {
    var mCredentialsApiClient: GoogleApiClient? = null
    private val RC_HINT = 2
    var etMobile:EditText?=null
    var loading:ProgressBar?=null
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCredentialsApiClient = GoogleApiClient.Builder(activity!!)
            .addApi(Auth.CREDENTIALS_API)
            .build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var rootView =inflater.inflate(R.layout.fragment_login, container, false);
        etMobile = rootView.etMobile
        val btnNext = rootView.btnNext
        loading = rootView.loading
        loginViewModel = ViewModelProvider(activity!!.viewModelStore,
            LoginViewModelFactory()
        ).get(LoginViewModel::class.java)
        loginViewModel.loginMobileViewState.observe(viewLifecycleOwner, Observer {
            val mobileState = it ?: return@Observer
                btnNext.isEnabled = mobileState.isDataValid
            if(btnNext.isEnabled){
                GlobalScope.launch (Dispatchers.Main){
                    delay(1000)
                    nextButtonClick()
                }
            }
            if (mobileState.mobileNumberError != null) {
                etMobile!!.error = getString(mobileState.mobileNumberError)
            }
        })

    /*    etMobile.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> //Do Something
                        requestHint()
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
*/
        etMobile!!.apply{
            afterTextChanged {
                loginViewModel.mobileDataChanged(etMobile.text.toString())
            }
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                    {
                            loginViewModel.openOtpScreen(etMobile.text.toString())
                    }
                }
                false
            }
            btnNext.setOnClickListener {
                nextButtonClick()
            }
        }

        // Start a coroutine
        GlobalScope.launch {
            delay(1000)
            requestHint()
        }

        return rootView
    }

    private fun nextButtonClick(){
        loading!!.visibility = View.VISIBLE
        loginViewModel.openOtpScreen(etMobile!!.text.toString().trim())
    }
    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun requestHint() {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val intent = Auth.CredentialsApi.getHintPickerIntent(
            mCredentialsApiClient, hintRequest)

        try {
            startIntentSenderForResult(intent.intentSender, RC_HINT, null, 0, 0, 0,null)
        } catch (e: Exception) {
            Log.e("TAG", e.message)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_HINT && resultCode == Activity.RESULT_OK) {
            /*You will receive user selected phone number here if selected and send it to the server for request the otp*/
            var credential: Credential = data!!.getParcelableExtra(Credential.EXTRA_KEY)
            val phoneInstance = PhoneNumberUtil.getInstance()
            val phoneNumber = phoneInstance.parse(credential.id, null)
            val isValid: Boolean = phoneInstance.isValidNumber(phoneNumber);
            Log.e("number is",credential.id)
            if(isValid){
                val number=deleteCountry(credential.id)
                Log.e("valid number is", number)
                etMobile!!.setText(number)
            }
        }
    }
    fun deleteCountry(phone: String) : String{
        val phoneInstance = PhoneNumberUtil.getInstance()
        try {
            val phoneNumber = phoneInstance.parse(phone, null)
            return phoneNumber?.nationalNumber?.toString()?:phone
        }catch (_ : Exception) {
        }
        return phone
    }
}

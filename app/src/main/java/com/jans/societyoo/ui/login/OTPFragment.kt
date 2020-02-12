package com.jans.societyoo.ui.login


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jans.otpview.OnOtpCompletionListener
import com.jans.societyoo.R
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_login.view.loading
import kotlinx.android.synthetic.main.fragment_otp.view.*
import kotlinx.android.synthetic.main.fragment_login.view.btnNext as btnNext1

/**
 * A simple [Fragment] subclass.
 */
class OTPFragment : Fragment(), OnOtpCompletionListener {

    private lateinit var loginCallbackListener:LoginCallbackListener
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView =inflater.inflate(R.layout.fragment_otp, container, false);

        val otpView=rootView.otp_view
        val btnNext = rootView.btnNext
        val loading = rootView.loading

        otpView.setOtpCompletionListener(this)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
        loginViewModel.loginFormState.observe(this@OTPFragment, Observer {
            val mobileState = it ?: return@Observer
            btnNext.isEnabled = mobileState.isDataValid
            if (mobileState.mobileNumberError != null) {
                //etMobile.error = getString(mobileState.mobileNumberError)
            }
        })

        loginViewModel.loginResult.observe(this@OTPFragment, Observer {
            val loginResult = it ?: return@Observer
            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
                loginViewModel.loginFragmentChanged(LoginState.OTP_VERIFY)
            }
        })


        return rootView
    }


    private fun updateUiWithUser(model: LoginUserView) {
        val mobileNumber = model.mobileNumber
        Toast.makeText(
            context,
            "$mobileNumber",
            Toast.LENGTH_LONG
        ).show()
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

    override fun onOtpCompleted(otp: String?) {
        Toast.makeText(context,"completed",Toast.LENGTH_LONG).show()
    }


}

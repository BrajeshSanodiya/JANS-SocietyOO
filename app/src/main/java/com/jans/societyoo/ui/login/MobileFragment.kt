package com.jans.societyoo.ui.login


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.jans.societyoo.R
import kotlinx.android.synthetic.main.fragment_login.view.*

/**
 * A simple [Fragment] subclass.
 */
class MobileFragment : Fragment() {

    private lateinit var loginCallbackListener:LoginCallbackListener
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView =inflater.inflate(R.layout.fragment_login, container, false);

        val etMobile = rootView.etMobile
        val btnNext = rootView.btnNext
        val loading = rootView.loading
        loginViewModel = ViewModelProvider(activity!!.viewModelStore, LoginViewModelFactory()).get(LoginViewModel::class.java)
        loginViewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val mobileState = it ?: return@Observer
            btnNext.isEnabled = mobileState.isDataValid
            if (mobileState.mobileNumberError != null) {
                etMobile.error = getString(mobileState.mobileNumberError)
            }
        })

        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer {
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

        etMobile.apply{
            afterTextChanged {
                loginViewModel.mobileDataChanged(etMobile.text.toString())
            }
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.mobile(etMobile.text.toString())
                }
                false
            }
            btnNext.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.mobile(etMobile.text.toString().trim())
            }
        }


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




}

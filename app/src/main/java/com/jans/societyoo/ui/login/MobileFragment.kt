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
import com.jans.societyoo.viewmodel.LoginViewModel
import com.jans.societyoo.viewmodel.LoginViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.view.*

class MobileFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView =inflater.inflate(R.layout.fragment_login, container, false);
        val etMobile = rootView.etMobile
        val btnNext = rootView.btnNext
        val loading = rootView.loading
        loginViewModel = ViewModelProvider(activity!!.viewModelStore,
            LoginViewModelFactory()
        ).get(LoginViewModel::class.java)
        loginViewModel.loginMobileViewState.observe(viewLifecycleOwner, Observer {
            val mobileState = it ?: return@Observer
                btnNext.isEnabled = mobileState.isDataValid
            if (mobileState.mobileNumberError != null) {
                etMobile.error = getString(mobileState.mobileNumberError)
            }
        })

        etMobile.apply{
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
                loading.visibility = View.VISIBLE
                loginViewModel.openOtpScreen(etMobile.text.toString().trim())
            }
        }
        return rootView
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

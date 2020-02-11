package com.jans.societyoo.ui.login

import android.app.Activity
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.jans.societyoo.R


class LoginActivitySample : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val etMobile = findViewById<EditText>(R.id.etMobile)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val loading = findViewById<ProgressBar>(R.id.loading)

//        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
//            .get(LoginViewModel::class.java)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivitySample, Observer {
            val mobileState = it ?: return@Observer

            // disable login button unless both username / password is valid
            btnNext.isEnabled = mobileState.isDataValid

            if (mobileState.mobileNumberError != null) {
                etMobile.error = getString(mobileState.mobileNumberError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivitySample, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
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
               /* loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())*/
            }
        }
    }

    private fun updateUiWithUser(model: LoginUserView) {
        //val welcome = getString(R.string.welcome)
        val mobileNumber = model.mobileNumber
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$mobileNumber",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

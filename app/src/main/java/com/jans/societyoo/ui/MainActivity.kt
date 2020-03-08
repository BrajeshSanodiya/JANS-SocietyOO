package com.jans.societyoo.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jans.societyoo.R
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.model.User
import com.jans.societyoo.model.UserData
import com.jans.societyoo.model.UserPostData
import com.jans.societyoo.ui.login.LoginActivity
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.PrintMsg
import com.jans.societyoo.viewmodel.MainActivityViewModel
import com.jans.tracking.PropertyName
import com.jans.tracking.Tracking
import com.jans.tracking.TrackingOptions
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var preferences=UserPreferences(this)
    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main)

        //val actionBar: ActionBar? = supportActionBar
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        supportActionBar!!.setLogo(R.drawable.header_logo);
        supportActionBar!!.setDisplayUseLogoEnabled(true);

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        btnSaveShared_dashboard.setOnClickListener{
            UserPreferences::userName.set(preferences,etSaveShared_dashboard.text.toString().trim());
            UserPreferences::emailAccount.set(preferences,"sanodiya.brajesh@gmail.com");
            Toast.makeText(this,"Preference Saved Successfully!",Toast.LENGTH_LONG).show()
            etSaveShared_dashboard.setText("")
        }

        btnShowShared_dashboard.setOnClickListener{
            tvShowShared_dashboard.setText(preferences.userName.toString())
            println(preferences.userName)
            println(preferences.emailAccount)
        }

        btnSendTracking_dashboard.setOnClickListener{
            // Tracking
            val trackMap = mutableMapOf<String, String>()
            trackMap[PropertyName.APP_NAME] = "JANS-SocietyOO"
            Tracking.trackEvent("app_open", trackMap,
                superPropertyMap = mapOf(Pair(PropertyName.LOGIN_STATUS, "Non Logged In")),
                trackRequestOption = TrackingOptions(firebase = true, comscore = false)
            )
            Toast.makeText(this,"Tracking Send Successfully!",Toast.LENGTH_LONG).show()
        }

        btnLogin_dashboard.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnGetUserService_dashboard.setOnClickListener {  getUser(1)}
        btnUserList_dashboard.setOnClickListener { getUserList() }

        var userPostData=UserPostData(title="Brajesh",body = "hiii", userId = 1)
        btnPostData_dashboard.setOnClickListener { postUser(userPostData) }

        //startActivity(Intent(this, LoginActivity::class.java))s

        PrintMsg.toastDebug(this,""+preferences.flats)
    }


    private fun getUser(id: Int) {
         progressBar!!.visibility= View.VISIBLE
         viewModel.getUser(id).observe(this, Observer {
             val result = it
             if (result is MyResult.Success) {
                 val user: User = result.data
                 Toast.makeText(applicationContext,user.toString() , Toast.LENGTH_LONG).show()
             }else if(result is MyResult.Error){
                 Toast.makeText(applicationContext, result.message, Toast.LENGTH_LONG).show()
             }
             progressBar!!.visibility= View.GONE
         })
    }
    private fun getUserList(){
        progressBar!!.visibility= View.VISIBLE
        viewModel.getUserList().observe(this, Observer {
            val result = it
            if (result is MyResult.Success) {
                val userList: List<User> = result.data
                Toast.makeText(applicationContext,userList.toString() , Toast.LENGTH_LONG).show()
            }else if(result is MyResult.Error){
                Toast.makeText(applicationContext, result.message, Toast.LENGTH_LONG).show()
            }
            progressBar!!.visibility= View.GONE
        })
    }

    private fun postUser(postData: UserPostData){
        progressBar!!.visibility= View.VISIBLE
        viewModel.postUser(postData).observe(this, Observer {
            val result = it
            if (result is MyResult.Success) {
                //val user: UserData = result.data
                Toast.makeText(applicationContext,result.data.toString() , Toast.LENGTH_LONG).show()
            }else if(result is MyResult.Error){
                Toast.makeText(applicationContext, result.message, Toast.LENGTH_LONG).show()
            }
            progressBar!!.visibility= View.GONE
        })
    }

}

package com.jans.societyoo.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jans.societyoo.R
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.model.User
import com.jans.societyoo.model.UserPostData
import com.jans.societyoo.ui.FragmentSwitcher
import com.jans.societyoo.ui.login.FlatsFragment
import com.jans.societyoo.ui.login.LoginActivity
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.PrintMsg
import com.jans.societyoo.viewmodel.MainActivityViewModel
import com.jans.tracking.PropertyName
import com.jans.tracking.Tracking
import com.jans.tracking.TrackingOptions
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {
    lateinit var fragmentSwitcher: FragmentSwitcher
    lateinit var preferences:UserPreferences
    lateinit var viewModel: MainActivityViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentSwitcher=context as FragmentSwitcher
        preferences = UserPreferences(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        view.btnDefaultFlat_dashboard.setOnClickListener{
            fragmentSwitcher.siwtchFragment(FlatsFragment(),true)
        }


        view.btnDeleteShared_dashboard.setOnClickListener{
            UserPreferences::flatsDetail.set(preferences,"");
            UserPreferences::userDetail.set(preferences,"");
            UserPreferences::mobileNum.set(preferences,"");
            UserPreferences::appOpenFirstTime.set(preferences,false)
            PrintMsg.toast(requireContext(),"LogOut Successfully! Kill the app and Open again..")
        }

        view.btnSaveShared_dashboard.setOnClickListener{
            UserPreferences::userName.set(preferences,view.etSaveShared_dashboard.text.toString().trim());
            UserPreferences::emailAccount.set(preferences,"sanodiya.brajesh@gmail.com");
            PrintMsg.toast(requireContext(),"Preference Saved Successfully!")
            view.etSaveShared_dashboard.setText("")
        }

        view.btnShowShared_dashboard.setOnClickListener{
            view.tvShowShared_dashboard.setText(preferences.userName.toString())
            println(preferences.userName)
            println(preferences.emailAccount)
        }

        view.btnSendTracking_dashboard.setOnClickListener{
            // Tracking
            val trackMap = mutableMapOf<String, String>()
            trackMap[PropertyName.APP_NAME] = "JANS-SocietyOO"
            Tracking.trackEvent("app_open", trackMap,
                superPropertyMap = mapOf(Pair(PropertyName.LOGIN_STATUS, "Non Logged In")),
                trackRequestOption = TrackingOptions(firebase = true, comscore = false)
            )
            PrintMsg.toastDebug(requireContext(),"Tracking Send Successfully!")
        }

        view.btnLogin_dashboard.setOnClickListener{
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        view.btnGetUserService_dashboard.setOnClickListener {  getUser(view,1)}
        view.btnUserList_dashboard.setOnClickListener { getUserList(view) }

        var userPostData= UserPostData(title="Brajesh",body = "hiii", userId = 1)
        view.btnPostData_dashboard.setOnClickListener { postUser(view,userPostData) }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainFragment().apply {
            }
    }



    private fun getUser(view: View,id: Int) {
        view.progressBar!!.visibility= View.VISIBLE
        viewModel.getUser(id).observe(viewLifecycleOwner, Observer {
            val result = it
            if (result is MyResult.Success) {
                val user: User = result.data
                PrintMsg.toastDebug(requireContext(),user.toString())
            }else if(result is MyResult.Error){
                PrintMsg.toastDebug(requireContext(), result.message)
            }
            view.progressBar!!.visibility= View.GONE
        })
    }
    private fun getUserList(view: View){
        view.progressBar!!.visibility= View.VISIBLE
        viewModel.getUserList().observe(viewLifecycleOwner, Observer {
            val result = it
            if (result is MyResult.Success) {
                val userList: List<User> = result.data
                PrintMsg.toastDebug(requireContext(),userList.toString())
            }else if(result is MyResult.Error){
                PrintMsg.toastDebug(requireContext(), result.message)
            }
            view.progressBar!!.visibility= View.GONE
        })
    }

    private fun postUser(view: View,postData: UserPostData){
        view.progressBar!!.visibility= View.VISIBLE
        viewModel.postUser(postData).observe(viewLifecycleOwner, Observer {
            val result = it
            if (result is MyResult.Success) {
                //val user: UserData = result.data
                PrintMsg.toastDebug(requireContext(),result.data.toString() )
            }else if(result is MyResult.Error){
                PrintMsg.toastDebug(requireContext(),result.message)
            }
            view.progressBar!!.visibility= View.GONE
        })
    }
}

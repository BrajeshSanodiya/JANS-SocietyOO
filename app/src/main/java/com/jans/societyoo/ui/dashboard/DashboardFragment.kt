package com.jans.societyoo.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jans.roomexplorer.RoomExplorer
import com.jans.societyoo.R
import com.jans.societyoo.data.local.db.DatabaseInstance
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.model.ApiDataObject
import com.jans.societyoo.model.User
import com.jans.societyoo.model.login.FlatDetail
import com.jans.societyoo.model.login.UserDetail
import com.jans.societyoo.model.main.Services
import com.jans.societyoo.ui.FragmentSwitcher
import com.jans.societyoo.ui.login.FlatsFragment
import com.jans.societyoo.ui.login.LoginActivity
import com.jans.societyoo.ui.login.UserProfileFragment
import com.jans.societyoo.ui.societyservice.ServiceAdapter
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.PrintMsg
import com.jans.societyoo.viewmodel.LoginViewModel
import com.jans.societyoo.viewmodel.LoginViewModelFactory
import com.jans.societyoo.viewmodel.MainActivityViewModel
import com.jans.societyoo.viewmodel.MainActivityViewModelFactory
import com.jans.tracking.PropertyName
import com.jans.tracking.Tracking
import com.jans.tracking.TrackingOptions
import kotlinx.android.synthetic.main.fragment_dashboard.view.*

class DashboardFragment : Fragment() {
    lateinit var fragmentSwitcher: FragmentSwitcher
    lateinit var preferences:UserPreferences
    lateinit var viewModel: MainActivityViewModel
    //lateinit var viewModelUser: LoginViewModel
    var userDetail: UserDetail? = null
    var flats: List<FlatDetail>? = null
    companion object {
        @JvmStatic
        fun newInstance() =
            DashboardFragment().apply {
            }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentSwitcher=context as FragmentSwitcher
        preferences = UserPreferences(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.fragment_dashboard,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(viewModelStore,MainActivityViewModelFactory(requireContext())).get(MainActivityViewModel::class.java)
       /* viewModelUser=ViewModelProvider(viewModelStore,LoginViewModelFactory(requireActivity())).get(LoginViewModel::class.java)
        viewModelUser.userDetailLiveData.observe(viewLifecycleOwner, Observer {
            val _userDetail = it
            if (_userDetail != null) {
                userDetail = _userDetail
                viewModelUser.getAllFlatsDB()
            }
        })
        viewModelUser.getUserDetailDB()
        viewModelUser.flatsDetailLiveData.observe(viewLifecycleOwner, Observer {
            val _flats = it
            if (_flats != null && _flats.size > 0) {
                flats = _flats
            }
        })*/
        getDashboardServices(view)
       /*     var list:ArrayList<Service> = ArrayList() //listOf<Service>(Service("Adam", 1,"",1, "test"))
        for (i in 1..10){
            var service=Service("testing"+1,i,"",0,"test"+i)
            list.add(service)
        }*/

        view.service_dashboard.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        //view.service_dashboard.adapter=ServiceAdapter(context = requireContext(), dataSource = list)


        view.btnDefaultFlat_dashboard.setOnClickListener{
            fragmentSwitcher.siwtchFragment(FlatsFragment.newInstance(isFromLogin = false),true)
        }

        view.btnUpdateProfile_dashboard.setOnClickListener{
            fragmentSwitcher.siwtchFragment(UserProfileFragment.newInstance(isFromLogin = false),true)
        }

        view.btnDeleteShared_dashboard.setOnClickListener{
            UserPreferences::flatsDetail.set(preferences,"");
            UserPreferences::userDetail.set(preferences,"");
            UserPreferences::mobileNum.set(preferences,"");
            UserPreferences::appOpenFirstTime.set(preferences,false)
            PrintMsg.toast(requireContext(),"LogOut Successfully! Kill the app and Open again..")
        }

        view.btnDatabase_dashboard.setOnClickListener{
            RoomExplorer.show(requireContext(), DatabaseInstance::class.java, DatabaseInstance.DB_NAME)
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

        //var userPostData= UserPostData(title="Brajesh",body = "hiii", userId = 1)
        //view.btnPostData_dashboard.setOnClickListener { postUser(view,userPostData) }

    }

    private fun getDashboardServices(view: View) {
        viewModel.getDashboardServicesDB().observe(viewLifecycleOwner, Observer {
            val result = it
            if(result!=null && result.size>0){
                view.service_dashboard.adapter=
                    ServiceAdapter(
                        context = requireContext(),
                        dataSource = result
                    )
                view.service_dashboard.adapter!!.notifyDataSetChanged()
            }else{
                viewModel.socityIdLiveData.observe(viewLifecycleOwner, Observer {
                    val result=it
                    if(result!=null && result!=0){
                        getDashboardServicesAPI(view,result)
                    }
                })
                viewModel.getSocityIdDB()
            }
        })
    }


    private fun getDashboardServicesAPI(view: View,serviceId: Int) {
        viewModel.getDashboardServices(serviceId).observe(viewLifecycleOwner, Observer {
            val result = it
            if (result is MyResult.Success) {
                val data: ApiDataObject<Services> = result.data
                if (data.dis_msg == 1 && !TextUtils.isEmpty(data.msg))
                    PrintMsg.toast(context, data.msg);
                if (data.success_stat == 1) {
                    viewModel.setDashboardServicesDB(result.data.data_details)
                    view.service_dashboard.adapter=
                        ServiceAdapter(
                            context = requireContext(),
                            dataSource = data.data_details.services
                        )
                    view.service_dashboard.adapter!!.notifyDataSetChanged()
                }
            } else if (result is MyResult.Error) {
                PrintMsg.toastDebug(context, result.message)
            }
        })
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

}

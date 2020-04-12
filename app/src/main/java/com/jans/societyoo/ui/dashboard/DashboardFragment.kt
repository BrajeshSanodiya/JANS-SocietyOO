package com.jans.societyoo.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jans.roomexplorer.RoomExplorer
import com.jans.societyoo.R
import com.jans.societyoo.data.local.db.DatabaseInstance
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.model.ApiDataObject
import com.jans.societyoo.model.login.FlatDetail
import com.jans.societyoo.model.login.UserDetail
import com.jans.societyoo.model.services.Service
import com.jans.societyoo.model.services.Services
import com.jans.societyoo.ui.FragmentSwitcher
import com.jans.societyoo.ui.login.FlatsFragment
import com.jans.societyoo.ui.login.LoginActivity
import com.jans.societyoo.ui.login.UserProfileFragment
import com.jans.societyoo.ui.services.ProviderPostActivity
import com.jans.societyoo.ui.services.ServiceAdapter
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.PrintMsg
import com.jans.societyoo.viewmodel.DashboardActivityViewModel
import com.jans.societyoo.viewmodel.DashboardActivityViewModelFactory
import com.jans.tracking.PropertyName
import com.jans.tracking.Tracking
import com.jans.tracking.TrackingOptions
import kotlinx.android.synthetic.main.fragment_dashboard.view.*

class DashboardFragment : Fragment() {
    lateinit var fragmentSwitcher: FragmentSwitcher
    lateinit var preferences:UserPreferences
    lateinit var viewModel: DashboardActivityViewModel
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

        viewModel = ViewModelProvider(viewModelStore,DashboardActivityViewModelFactory(requireContext())).get(DashboardActivityViewModel::class.java)

        getDashboardServices(view)

        view.service_dashboard.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        view.btnPostProvider_dashboard.setOnClickListener {
            var intent= Intent(context, ProviderPostActivity::class.java)
            //var intent= Intent(requireContext(), ProviderPostActivity::class.java)
            requireContext().startActivity(intent)
        }

        view.btnDefaultFlat_dashboard.setOnClickListener{
            fragmentSwitcher.siwtchFragment(FlatsFragment.newInstance(isFromLogin = false),true)
        }

        view.btnUpdateProfile_dashboard.setOnClickListener{
            fragmentSwitcher.siwtchFragment(UserProfileFragment.newInstance(isFromLogin = false),true)
        }

        view.btnDeleteShared_dashboard.setOnClickListener{
            UserPreferences::mobileNumV2.set(preferences,"")
            UserPreferences::appOpenFirstTimeV2.set(preferences,false)
            UserPreferences::defaultFlatId.set(preferences,0)
            UserPreferences::defaultUserId.set(preferences,0)
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
            println()
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

    }

    private fun getDashboardServices(view: View) {
   /*     viewModel.getDashboardServicesDB().observe(viewLifecycleOwner, Observer {
            val result = it
            if(result!=null && result.size>0){
                view.service_dashboard.adapter=
                    ServiceAdapter(
                        context = requireContext(),
                        dataSource =getDummyList(result)
                    )
                view.service_dashboard.adapter!!.notifyDataSetChanged()
            }else{*/
                viewModel.socityIdLiveData.observe(viewLifecycleOwner, Observer {
                    val result=it
                    if(result!=null && result!=0){
                        getDashboardServicesAPI(view,result)
                    }
                })
                viewModel.getSocityIdDB()
           /* }
        })*/
    }

    fun getDummyList(list: List<Service>): List<Service> {
       /* if (list != null && list.size > 0) {
            val tempList: ArrayList<Service> = ArrayList<Service>()
            for (item in list) {
                tempList.add(item)
            }
            val tempItem = list.get(0)
            for (item in 0..9) {
                tempList.add(tempItem)
            }
            return tempList
        }*/
        return list
    }

    private fun getDashboardServicesAPI(view: View,serviceId: Int) {
        viewModel.getSocietyServices(serviceId).observe(viewLifecycleOwner, Observer {
            val result = it
            if (result is MyResult.Success) {
                val data: ApiDataObject<Services> = result.data
                if (data.dis_msg == 1 && !TextUtils.isEmpty(data.msg))
                    PrintMsg.toast(context, data.msg);
                if (data.success_stat == 1 && data.data_details!=null) {
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

}

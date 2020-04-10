package com.jans.societyoo.ui.navigation

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jans.societyoo.R
import com.jans.societyoo.databinding.FragmentHomeBinding
import com.jans.societyoo.model.ApiDataObject
import com.jans.societyoo.model.services.Services
import com.jans.societyoo.ui.services.ServiceAdapter
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.PrintMsg
import com.jans.societyoo.viewmodel.DashboardActivityViewModel
import com.jans.societyoo.viewmodel.DashboardActivityViewModelFactory
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.service_home

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dashboardViewModel: DashboardActivityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        dashboardViewModel = ViewModelProvider(viewModelStore,DashboardActivityViewModelFactory(requireContext())).get(DashboardActivityViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.dashboardViewModel=dashboardViewModel
        binding.serviceHome.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        dashboardViewModel.run {
            socityIdLiveData.observe(viewLifecycleOwner, Observer {
                it?.let { getSocietyServices(it).observe(viewLifecycleOwner, Observer { handleServiceResponse(it)}) }
            })
            getSocityIdDB()
        }
    }

    private fun handleServiceResponse(it: MyResult<ApiDataObject<Services>>) {
        when (it) {
            is MyResult.Success<ApiDataObject<Services>> -> {
                if (it.data.dis_msg == 1 && !TextUtils.isEmpty(it.data.msg))
                    PrintMsg.toast(context, it.data.msg);
                if (it.data.success_stat == 1) {
                    dashboardViewModel.setDashboardServicesDB(it.data.data_details)
                    binding.serviceHome.adapter=
                        ServiceAdapter(
                            context = requireContext(),
                            dataSource = it.data.data_details.services
                        )
                    binding.serviceHome.adapter!!.notifyDataSetChanged()
                }
            }
        }
    }

}

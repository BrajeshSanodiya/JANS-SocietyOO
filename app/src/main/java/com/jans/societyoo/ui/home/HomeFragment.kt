package com.jans.societyoo.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jans.societyoo.databinding.FragmentHomeBinding
import com.jans.societyoo.model.ApiDataObject
import com.jans.societyoo.model.ApiDataObjectWithCursor
import com.jans.societyoo.model.post.Post
import com.jans.societyoo.model.services.Services
import com.jans.societyoo.ui.customviews.EndlessRecyclerViewScrollListener
import com.jans.societyoo.ui.post.PostAdapter
import com.jans.societyoo.ui.services.ServiceAdapter
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.PrintMsg
import com.jans.societyoo.viewmodel.DashboardActivityViewModel
import com.jans.societyoo.viewmodel.DashboardActivityViewModelFactory


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var dashboardViewModel: DashboardActivityViewModel
    private lateinit var postAdapter: PostAdapter
    private lateinit var scrollListener:EndlessRecyclerViewScrollListener
    val listPost=ArrayList<Post>()
    private var societyId =0
    private var cursor:String=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        dashboardViewModel = ViewModelProvider(
            viewModelStore,
            DashboardActivityViewModelFactory(requireContext())
        ).get(DashboardActivityViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        binding.swipeRefresh.setOnRefreshListener {
            if(societyId!=0){
                dashboardViewModel.getPostList(societyId).observe(viewLifecycleOwner, Observer { handlePostListResponse(it) })
            }
            binding.swipeRefresh.isRefreshing = false   // reset the SwipeRefreshLayout (stop the loading spinner)
        }
        binding.dashboardViewModel = dashboardViewModel
        binding.serviceHome.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        var linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        postAdapter=PostAdapter(
            context = requireContext(),
            dataSource = listPost
        )
        binding.postHome.adapter = postAdapter

        binding.postHome.layoutManager=linearLayoutManager

        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if(societyId!=0 && !TextUtils.isEmpty(cursor)){
                    dashboardViewModel.getPostList(societyId,cursor).observe(viewLifecycleOwner, Observer { handlePostListWithCursorResponse(it) })
                }
            }
        }
        binding.postHome.addOnScrollListener(scrollListener)


        dashboardViewModel.run {
            getSocityIdDB()
            socityIdLiveData.observe(viewLifecycleOwner, Observer {
                it?.let {
                    societyId=it
                    if(societyId!=0){
                        getSocietyServices(societyId).observe(
                            viewLifecycleOwner,
                            Observer { handleServiceResponse(it) })
                        getPostList(societyId).observe(viewLifecycleOwner, Observer { handlePostListResponse(it) })
                    }
                }
            })

        }
    }

    private fun handlePostListResponse(it: MyResult<ApiDataObjectWithCursor<List<Post>>>?) {
        when (it) {
            is MyResult.Success<ApiDataObjectWithCursor<List<Post>>> -> {
                progressBarVisibility(false)
                if (it.data.dis_msg == 1 && !TextUtils.isEmpty(it.data.msg))
                    PrintMsg.toast(context, it.data.msg);
                if (it.data.success_stat == 1) {
                    clearPostList()
                    cursor=it.data.cursor
                    appendPostList(it.data.data_details)
                }
            }
            is MyResult.Error -> {
                progressBarVisibility(false)
            }

            is MyResult.Loading -> {
                progressBarVisibility(true)
            }
        }
    }

    private fun handlePostListWithCursorResponse(it: MyResult<ApiDataObjectWithCursor<List<Post>>>?) {
        when (it) {
            is MyResult.Success<ApiDataObjectWithCursor<List<Post>>> -> {
                progressBarVisibility(false)
                if (it.data.dis_msg == 1 && !TextUtils.isEmpty(it.data.msg))
                    PrintMsg.toast(context, it.data.msg);
                if (it.data.success_stat == 1) {
                    cursor=it.data.cursor
                    appendPostList(it.data.data_details)

                }
            }
            is MyResult.Error -> {
                progressBarVisibility(false)
            }

            is MyResult.Loading -> {
                progressBarVisibility(true)
            }
        }
    }

    private fun handleServiceResponse(it: MyResult<ApiDataObject<Services>>) {
        when (it) {
            is MyResult.Success<ApiDataObject<Services>> -> {
                if (it.data.dis_msg == 1 && !TextUtils.isEmpty(it.data.msg))
                    PrintMsg.toast(context, it.data.msg);
                if (it.data.success_stat == 1) {
                    dashboardViewModel.setDashboardServicesDB(it.data.data_details)
                    binding.serviceHome.adapter = ServiceAdapter(
                        context = requireContext(),
                        dataSource = it.data.data_details.services
                    )
                    binding.serviceHome.adapter!!.notifyDataSetChanged()

                    //callPostFeed(0)
                }
            }

            is MyResult.Error -> {
                // binding.progressBar.visibility = View.GONE
            }

            is MyResult.Loading -> {
                // binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun clearPostList(){
        listPost?.let {
            listPost.clear()
            postAdapter.notifyDataSetChanged()
            scrollListener.resetState()
        }
    }
    private fun appendPostList(list: List<Post>){
        listPost.addAll(list)
        postAdapter.notifyDataSetChanged()
    }

    fun progressBarVisibility(visible: Boolean) {
        if (visible) {
            binding.progressBar.visibility = View.VISIBLE
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            binding.progressBar.visibility = View.GONE
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


}

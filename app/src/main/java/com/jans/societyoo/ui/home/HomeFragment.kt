package com.jans.societyoo.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jans.societyoo.R
import com.jans.societyoo.databinding.FragmentHomeBinding
import com.jans.societyoo.model.ApiDataObject
import com.jans.societyoo.model.post.Post
import com.jans.societyoo.model.services.Services
import com.jans.societyoo.ui.customviews.MyRecyclerScroll
import com.jans.societyoo.ui.post.PostAdapter
import com.jans.societyoo.ui.services.ServiceAdapter
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.PrintMsg
import com.jans.societyoo.viewmodel.DashboardActivityViewModel
import com.jans.societyoo.viewmodel.DashboardActivityViewModelFactory


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dashboardViewModel: DashboardActivityViewModel

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


      /*  var animation: Animation = AnimationUtils.loadAnimation(context, R.anim.simple_grow);
        binding.serviceHome.startAnimation(animation);*/

        binding.dashboardViewModel = dashboardViewModel
        binding.serviceHome.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.postHome.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        /*binding.postHome.setOnScrollListener(object : MyRecyclerScroll() {
            override fun show() {
                binding.serviceHome.visibility=View.VISIBLE
                binding.serviceHome.animate().translationY(0F).setInterpolator(DecelerateInterpolator(2F)).start()
            }

            override fun hide() {
                binding.serviceHome.animate().translationY( -binding.serviceHome.getHeight().toFloat() )
                    .setInterpolator(AccelerateInterpolator(2F)).start()
                binding.serviceHome.visibility=View.GONE
            }
        })*/

        dashboardViewModel.run {
            socityIdLiveData.observe(viewLifecycleOwner, Observer {
                it?.let {
                    getSocietyServices(it).observe(
                        viewLifecycleOwner,
                        Observer { handleServiceResponse(it) })
                }
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
                    binding.serviceHome.adapter = ServiceAdapter(
                        context = requireContext(),
                        dataSource = it.data.data_details.services
                    )
                    binding.serviceHome.adapter!!.notifyDataSetChanged()

                    handleHomeResponse()
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

    private fun handleHomeResponse() {
        var listPost=ArrayList<Post>()

        var imgList=ArrayList<String>()
        imgList.add("https://image.freepik.com/free-vector/artistic-mobile-wallpaper-with-hand-drawn-roses_79603-466.jpg")
        imgList.add("https://wallpapercave.com/wp/wp5211914.jpg")
        imgList.add("https://www.lialip.com/wp-content/uploads/2019/04/4d17124dd94475826bcd16fcf929697c-521x1024.jpg")
        imgList.add("https://i.pinimg.com/736x/5a/64/39/5a643997b2f873a84e4ff9153587bfd1.jpg")
        listPost.add(Post(
            desc="Free AI Code Completion Plugin for Your IDE!<br />" +
                    "<br />" +
                    "Free Plugin for IntelliJ, Android Studio, and Eclipse: Write fast & error free code with AI code completions<br />" +
                    "<br />" +
                    "Now supporting Kotlin \uD83C\uDF89\uD83C\uDF8A\uD83D\uDCAA<br />" +
                    "<br />" +
                    "Get it now at http://bit.ly/2VYlIWO",
            images =imgList,
            userName = "Brajesh",
            userImg = "https://static-exp1.licdn.com/sc/p/com.linkedin.jobs-guest-frontend%3Ajobs-guest-frontend-static-content%2B0.0.2225/f/%2Fjobs-guest-frontend%2Fimages%2Fcommon%2Fpeople%2Fuser13.jpg",
            publishTime = "5 April at 14:28 "

        ))
        var imgList1=ArrayList<String>()
        //imgList1.add("https://images.wallpaperscraft.com/image/landscape_art_moon_127187_1280x1024.jpg")
        listPost.add(Post(
            desc="1) Just by saying \" Har Har Mahadev \" you cannot win a war.........Chatrapati Shivaji knew this.<br />" +
                    "<br />" +
                    "2) Just by saying \"Quit India\", British shall not leave India....... Gandhiji knew this.<br />" +
                    "<br />" +
                    "3) Just by asking blood you won't get freedom......... Netaji Knew this.<br />" +
                    "<br />" +
                    "4) Just by drinking water from common well Casteism shall not end......... Dr. Ambedkar knew this.<br />" +
                    "<br />" +
                    "5) Similarly by clapping and lighting candles Corona shall not disappear..........Modiji knows this.<br />" +
                    "<br />" +
                    "But behind these all the motive was noble to raise the Confidence, unity and Spirit of people.\uD83D\uDC4F\uD83C\uDFFB\uD83D\uDE07",
            images =imgList1,
            userName = "Sanodiya Brajesh",
            userImg = "https://static-exp1.licdn.com/sc/p/com.linkedin.jobs-guest-frontend%3Ajobs-guest-frontend-static-content%2B0.0.1732/f/%2Fjobs-guest-frontend%2Fimages%2Fcommon%2Fpeople%2Fuser23.jpg",
            publishTime = "7 April at 20:45 "

        ))

        var imgList2=ArrayList<String>()
        imgList2.add("https://images.wallpaperscraft.com/image/landscape_art_moon_127187_1280x1024.jpg")
        listPost.add(Post(
            desc="एक ही शहर में रहना है मगर मिलना नहीं,<br />" +
                    "देखते हैं ये अज़िय्यत भी गवारा कर के !!!<br />" +
                    "<br />" +
                    "~ऐतबार साजिद<br />" +
                    "<br />" +
                    "#Corona<br />" +
                    "#SocialDistancing<br />" +
                    "#Lockdown",
            images =imgList2,
            userName = "Brajesh Kumar",
            userImg = "https://static-exp1.licdn.com/sc/p/com.linkedin.jobs-guest-frontend%3Ajobs-guest-frontend-static-content%2B0.0.1732/f/%2Fjobs-guest-frontend%2Fimages%2Fcommon%2Fpeople%2Fuser23.jpg",
            publishTime = "7 April at 20:45 "

        ))

        var imgList3=ArrayList<String>()
        imgList3.add("https://ze-robot.com/dl/be/beautiful-anime-scenery-wallpaper-1280%C3%971024.jpg")
        listPost.add(Post(
            desc="Do you spend way too much time making videos for your content marketing needs?<br />" +
                    "<br />" +
                    "Enter Lumen5 - the #1 video creator for content marketing that uses powerful A.I. to help you create a professional video in minutes.<br />" +
                    "<br />" +
                    "✔️Save time by repurposing text content into video<br />" +
                    "✔️Access millions of stock videos<br />" +
                    "✔️Brand your video content<br />" +
                    "<br />" +
                    "Join 400,000+ brands creating videos with Lumen5.",
            images =imgList3,
            userName = "Brajesh Sanodiya",
            userImg = "https://static-exp1.licdn.com/sc/p/com.linkedin.jobs-guest-frontend%3Ajobs-guest-frontend-static-content%2B0.0.2019/f/%2Fjobs-guest-frontend%2Fimages%2Fcommon%2Fpeople%2Fuser8.jpg",
            publishTime = "7 April at 20:45 "

        ))

        var imgList4=ArrayList<String>()
        imgList4.add("https://i.pinimg.com/originals/1a/aa/e1/1aaae1f693b0b17e0811566132aef5a7.jpg")
        imgList4.add("https://www.lialip.com/wp-content/uploads/2019/04/4d17124dd94475826bcd16fcf929697c-521x1024.jpg")
        imgList4.add("https://i.pinimg.com/736x/5a/64/39/5a643997b2f873a84e4ff9153587bfd1.jpg")
        imgList4.add("https://wallpaperaccess.com/full/384028.jpg")
        imgList4.add("https://interactive-examples.mdn.mozilla.net/media/examples/grapefruit-slice-332-332.jpg")
        listPost.add(Post(
            desc="\uD83D\uDD34LIVE on camera\uD83D\uDE32 Watch me RANK A VIDEO on Google in 60 seconds \uD83D\uDE32\uD83D\uDE32 <br />" +
                    "People Think I'm some kind of RANKING magician, I just use Video Marketing Blaster: http://vmblaster.com/sdr-vmb",
            images =imgList4,
            userName = "Brajesh Kumar",
            userImg = "https://static-exp1.licdn.com/sc/p/com.linkedin.jobs-guest-frontend%3Ajobs-guest-frontend-static-content%2B0.0.1732/f/%2Fjobs-guest-frontend%2Fimages%2Fcommon%2Fpeople%2Fuser23.jpg",
            publishTime = "7 April at 20:45 "

        ))

        var imgList5=ArrayList<String>()
        imgList5.add("https://wallpaperaccess.com/full/384028.jpg")
        listPost.add(Post(
            desc="See how we're reimagining the future of project management with Jira Software",
            images =imgList5,
            userName = "Brajesh Kumar Sanodiya",
            userImg = "https://static-exp1.licdn.com/sc/p/com.linkedin.jobs-guest-frontend%3Ajobs-guest-frontend-static-content%2B0.0.1986/f/%2Fjobs-guest-frontend%2Fimages%2Fcommon%2Fpeople%2Fuser20.jpg",
            publishTime = "7 April at 20:45 "

        ))


        var imgList6=ArrayList<String>()
        imgList6.add("https://interactive-examples.mdn.mozilla.net/media/examples/grapefruit-slice-332-332.jpg")
        listPost.add(Post(
            desc="See how we're reimagining the future of project management with Jira Software",
            images =imgList6,
            userName = "Abc Xyz...",
            userImg = "https://static-exp1.licdn.com/sc/p/com.linkedin.jobs-guest-frontend%3Ajobs-guest-frontend-static-content%2B0.0.1865/f/%2Fjobs-guest-frontend%2Fimages%2Fcommon%2Fpeople%2Fuser17.jpg",
            publishTime = "10 April at 16:00"

        ))

        var imgList7=ArrayList<String>()
        imgList7.add("https://images-na.ssl-images-amazon.com/images/I/51LmfAyTgEL._SX466_.jpg")
        listPost.add(Post(
            desc="See how we're reimagining the future of project management with Jira Software",
            images =imgList7,
            userName = "Testing...",
            userImg = "https://static-exp1.licdn.com/sc/p/com.linkedin.jobs-guest-frontend%3Ajobs-guest-frontend-static-content%2B0.0.2019/f/%2Fjobs-guest-frontend%2Fimages%2Fcommon%2Fpeople%2Fuser24.jpg",
            publishTime = "7 April at 20:45 "

        ))

        binding.postHome.adapter = PostAdapter(
            context = requireContext(),
            dataSource = listPost
        )
        binding.postHome.adapter!!.notifyDataSetChanged()

    }

}

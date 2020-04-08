package com.jans.societyoo.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jans.societyoo.R
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.model.ApiDataObject
import com.jans.societyoo.model.login.FlatDetail
import com.jans.societyoo.model.login.UserData
import com.jans.societyoo.model.login.UserDetail
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.PrintMsg
import com.jans.societyoo.viewmodel.LoginViewModel
import com.jans.societyoo.viewmodel.LoginViewModelFactory
import com.jans.societyoo.viewmodel.UserProfileViewModel
import com.jans.societyoo.viewmodel.UserProfileViewModelFactory
import kotlinx.android.synthetic.main.fragment_otp.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val ARG_IS_FROM_LOGIN = "is_from_login"

class FlatsFragment : Fragment() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userProfileModel: UserProfileViewModel
    var flats: List<FlatDetail>? = null
    var userDetail: UserDetail? = null
    var mobileNumber: String? = null
    var rgFlats: RadioGroup? = null
    var checkedUserId: Int = 0
    var progressBar:ProgressBar?=null
    private var isFromLogin: Boolean = false
    lateinit var preferences: UserPreferences
    companion object {
        @JvmStatic
        fun newInstance(isFromLogin: Boolean) =
            FlatsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_FROM_LOGIN, isFromLogin)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isFromLogin = it.getBoolean(ARG_IS_FROM_LOGIN)
        }
        preferences = UserPreferences(requireContext())
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_flats, container, false)
        rgFlats = rootView.findViewById(R.id.rgFlats_Flats)
        progressBar = rootView.progress_bar
        setProgressBarVisibility(true)
        var btnNext: Button = rootView.findViewById(R.id.btnNext_Flats)
        var btnSave: Button = rootView.findViewById(R.id.btnSave_Flats)
        if (isFromLogin) {
            btnNext.visibility = View.VISIBLE
            btnSave.visibility = View.GONE
        } else {
            btnNext.visibility = View.GONE
            btnSave.visibility = View.VISIBLE
        }

        userProfileModel =
            ViewModelProvider(viewModelStore, UserProfileViewModelFactory(requireContext())).get(
                UserProfileViewModel::class.java
            )

        loginViewModel = ViewModelProvider(
            requireActivity().viewModelStore,
            LoginViewModelFactory(requireActivity())
        ).get(LoginViewModel::class.java)
        loginViewModel.flatsDetailLiveData.observe(viewLifecycleOwner, Observer {
            val _flats = it
            if (_flats != null && _flats.size > 0) {
                flats = _flats
                mobileNumber = _flats[0].umMobile
                showFlats()
                /*if(userDetail!=null && userDetail!!.defultUserId!=null && userDetail!!.defultUserId!=0){
                    var index = 0
                    for (item in flats!!) {
                        if (userDetail!!.defultUserId == item.userId) {
                            var btnRadio: RadioButton = rgFlats!!.get(index) as RadioButton
                            btnRadio.isChecked = true
                        }
                        index++
                    }
                }*/
            }
        })

        loginViewModel.userDetailLiveData.observe(viewLifecycleOwner, Observer {
            val _userDetail = it
            if (_userDetail != null) {
                userDetail = _userDetail
                loginViewModel.getAllFlatsDB()
            }
        })
        /* loginViewModel.mobileNumberLiveData.observe(this, Observer {
             mobileNumber = it
         })*/


        loginViewModel.loginFlatViewState.observe(viewLifecycleOwner, Observer {
            val result = it
            if (isFromLogin) btnNext.isEnabled = result.isItemChecked else btnSave.isEnabled =
                result.isItemChecked
            checkedUserId = result.selectedUserId
        })
        btnNext.setOnClickListener {
            if (userDetail == null || userDetail!!.profileId == 0) {
                loginViewModel.loginFragmentChanged(LoginFragmentState.USER_PROFILE)
            } else {
                loginViewModel.openAfterLoginScreen()
            }
        }
        btnSave.setOnClickListener {
            userDetail!!.defaultUserId = checkedUserId
            //UserPreferences::defaultUserId.set(preferences,userDetail!!.defultUserId);
            updateUserProfile()
        }

        rgFlats!!.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
               if(checkedId!!>=0 && checkedId!!<flats!!.size){
                   loginViewModel.setFlatsConfirm(flats!!.get(checkedId).userId, true)
               }
            }
        })



        if (!isFromLogin) {
            loginViewModel.getUserDetailDB()
        }else{
            loginViewModel.getAllFlatsDB()
        }


        return rootView
    }

    fun setProgressBarVisibility(visible:Boolean){
        if(progressBar!=null){
            if(visible){
                progressBar!!.visibility=View.VISIBLE
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }else{
                progressBar!!.visibility=View.GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
    }

    fun showFlats() {
        setProgressBarVisibility(false)
        if (flats != null && rgFlats != null) {
            rgFlats!!.removeAllViews()
            var index: Int = 0
            for (flat in flats!!) {
                val radioButton = RadioButton(context)
                radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_flat, 0)
                val lp: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                lp.setMargins(0, 20, 0, 20)
                radioButton.id = index
                radioButton.tag = flat.flatId
                radioButton.layoutParams = lp
                radioButton.text ="Flat No. " + flat.flatNu + ", " + flat.flatFloorNu + " Floor, " + flat.towerName + " Tower, " + flat.societyName + ", " + flat.societyAddress + ", " + flat.societyCity
                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f)
                if(userDetail!=null && userDetail!!.defaultUserId!=null && userDetail!!.defaultUserId!=0){
                    if(flat.userId==userDetail!!.defaultUserId)
                        radioButton.isChecked=true
                }
                index++
                rgFlats!!.addView(radioButton)
            }
        }
    }

    private fun updateUserProfile() {
        setProgressBarVisibility(true)
        userProfileModel.updateProfile(userDetail!!)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val result = it
                if (result is MyResult.Success) {
                    val data: ApiDataObject<UserData> = result.data
                    if (data.dis_msg == 1 && !TextUtils.isEmpty(data.msg))
                        PrintMsg.toast(context, data.msg);
                    if (data.success_stat == 1) {
                        loginViewModel.setFlatDetailsDB(result.data.data_details.flatsDetails)
                        loginViewModel.setUserDetailDB(result.data.data_details.userDetails)
                        GlobalScope.launch(Dispatchers.Main) {
                            delay(500)
                            setProgressBarVisibility(false)
                            UserPreferences::defaultUserId.set(preferences,result.data.data_details.userDetails.defaultUserId);
                            UserPreferences::defaultFlatId.set(preferences,result.data.data_details.userDetails.defaultFlatId);
                            requireActivity().supportFragmentManager.popBackStack()
                        }
                    }else{
                        setProgressBarVisibility(false)
                    }
                } else if (result is MyResult.Error) {
                    setProgressBarVisibility(false)
                    PrintMsg.toastDebug(context, result.message)
                }
            })
    }
}

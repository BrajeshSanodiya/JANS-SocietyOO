package com.jans.societyoo.ui.login

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jans.societyoo.R
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
import java.util.*

private const val ARG_IS_FROM_LOGIN = "is_from_login"
class UserProfileFragment : Fragment() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userProfileModel: UserProfileViewModel
    var flats: List<FlatDetail>? = null
    var userDetail: UserDetail? = null
    var mobileNo: String?=null
    var gender:String="M"
    var rgGender:RadioGroup?=null
    private var year = 0
    private var month = 0
    private var day = 0
    private var etDate: TextView? = null
    var progressBar:ProgressBar?=null
    private var isFromLogin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isFromLogin = it.getBoolean(ARG_IS_FROM_LOGIN)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(isFromLogin: Boolean) =
            UserProfileFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_FROM_LOGIN, isFromLogin)
                }
            }
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_user_profile, container, false)
        etDate = rootView.findViewById(R.id.etDate_UserProfile)
        setCurrentDateOnView()
        addListenerOnButton()

        progressBar = rootView.progress_bar
        progressBar!!.visibility=View.VISIBLE

        var etName: EditText = rootView.findViewById(R.id.etName_UserProfile)
        var etEmail: EditText = rootView.findViewById(R.id.etEmail_UserProfile)
        var etMobile2: EditText = rootView.findViewById(R.id.etMobileSec_UserProfile)
        rgGender=rootView.findViewById(R.id.rgGender_UserProfile)
        var btnNext: Button = rootView.findViewById(R.id.btnNext_UserProfile)

        if(!isFromLogin){
            btnNext.text=resources.getString(R.string.save_btn)
            btnNext.isEnabled=true
        }

        loginViewModel = ViewModelProvider(requireActivity().viewModelStore,LoginViewModelFactory(requireActivity())).get(LoginViewModel::class.java)
        userProfileModel = ViewModelProvider(viewModelStore,  UserProfileViewModelFactory(requireContext())).get(UserProfileViewModel::class.java)

        /*loginViewModel.mobileNumberLiveData.observe(viewLifecycleOwner,androidx.lifecycle.Observer {
            val mobile=it
            if(!TextUtils.isEmpty(mobile))
                mobileNo=mobile
        })*/

        loginViewModel.flatsDetailLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val _flats = it
            if (_flats != null && _flats.size > 0) {
                flats = _flats
                mobileNo=_flats[0].umMobile
            }
        })

        loginViewModel.userDetailLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val _userDetail = it
            progressBar!!.visibility=View.GONE
            if (_userDetail != null && _userDetail.profileId != 0) {
                userDetail = _userDetail
                if(!TextUtils.isEmpty(_userDetail.name)) etName.setText(_userDetail.name)
                if(!TextUtils.isEmpty(_userDetail.email)) etEmail.setText(_userDetail.email)
                if(!TextUtils.isEmpty(_userDetail.mobile2)) etMobile2.setText(_userDetail.mobile2)
                if(!TextUtils.isEmpty(_userDetail.gender)) setGenderRadioBox(_userDetail.gender!!)
                if(!TextUtils.isEmpty(_userDetail.dob)) etDate!!.setText(_userDetail.dob)
            }
        })

        userProfileModel.userProfileViewState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                var loginUserProfileViewState = it
                if (loginUserProfileViewState.errorEmail) {
                    etEmail.error = getString(R.string.invalid_email)
                }
                if (loginUserProfileViewState.errorName) {
                    etName.error = getString(R.string.invalid_name)
                }
                if (loginUserProfileViewState.errorDOB) etDate!!.error =
                    getString(R.string.invalid_name) else etDate!!.error = null
                if (loginUserProfileViewState.validName && loginUserProfileViewState.validEmail) {
                    btnNext.isEnabled = true
                }

            })

       /* etName.onFocusChangeListener = OnFocusChangeListener { view, focused ->
            val et = view as EditText
            if (et.text.length > 0 && !focused) {
                userProfileModel.checkName(et.text.toString())
            }
        }
*/
        etName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length > 0) {
                    userProfileModel.checkName(s.trim().toString())
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,before: Int, count: Int) {}
        })

        etEmail.onFocusChangeListener = OnFocusChangeListener { view, focused ->
            val et = view as EditText
            if (et.text.length > 0 && !focused) {
                userProfileModel.checkEmail(et.text.toString())
            }
        }

        rgGender!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioM -> {
                    gender="M"
                }
                R.id.radioF -> {
                    gender="F"
                }
                R.id.radioT -> {
                    gender="T"
                }
            }
        })
        btnNext.setOnClickListener {

            if (userProfileModel.checkDOB(etDate!!.text.toString())) {
                if(userDetail==null){
                    userDetail=UserDetail(name=etName.text.toString(),email = etEmail.text.toString(), mobile = mobileNo!!,mobile2 = etMobile2.text.toString(),gender = gender, dob = etDate!!.text.toString())
                }else{
                    userDetail!!.name=etName.text.toString()
                    userDetail!!.email=etEmail.text.toString()
                    userDetail!!.mobile=mobileNo
                    userDetail!!.mobile2=etMobile2.text.toString()
                    userDetail!!.gender=gender
                    userDetail!!.dob=etDate!!.text.toString()
                }
                updateUserProfile(userDetail!!)
            }
        }

        loginViewModel.getAllFlatsDB()
        loginViewModel.getUserDetailDB()

        return rootView
    }
    fun setGenderRadioBox(gender:String){
        when (gender) {
            "M"->{
                var btnRadio:RadioButton=rgGender!!.get(0) as RadioButton
                btnRadio.isChecked=true
            }
            "F"->{
                var btnRadio:RadioButton=rgGender!!.get(1) as RadioButton
                btnRadio.isChecked=true
            }
            "T"->{
                var btnRadio:RadioButton=rgGender!!.get(2) as RadioButton
                btnRadio.isChecked=true
            }
        }
    }

    private fun updateUserProfile(userDetail: UserDetail) {
        progressBar!!.visibility=View.VISIBLE
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
                        GlobalScope.launch(Dispatchers.Main){
                            delay(500)
                            progressBar!!.visibility=View.GONE
                            if(isFromLogin)
                                loginViewModel.openAfterLoginScreen()
                            else
                                requireActivity().supportFragmentManager.popBackStack()
                        }
                    }else{
                        progressBar!!.visibility=View.GONE
                    }
                } else if (result is MyResult.Error) {
                    progressBar!!.visibility=View.GONE
                    PrintMsg.toastDebug(context, result.message)
                }
            })
    }

    fun setCurrentDateOnView() {
        val c: Calendar = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)
    }

    fun addListenerOnButton() {
        etDate!!.setOnClickListener {
            onCreateDialog()
        }
    }

    private fun onCreateDialog() {
        var datePicker: DatePickerDialog = DatePickerDialog(
            requireContext(), datePickerListener,
            year, month, day
        )
        datePicker.setTitle("Select You DOB")
        datePicker.show()
    }

    private val datePickerListener =
        OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
            // when dialog box is closed, below method will be called.
            year = selectedYear
            month = selectedMonth
            day = selectedDay
            var disDay:String=""
            var disMonth:String=""
            if(month < 10) disMonth="0"
            disMonth+=month+1
            if(day < 10) disDay ="0"
            disDay+=day

            etDate!!.setText(
                StringBuilder().append(year).append("-").append(disMonth)
                    .append("-").append(disDay)
            )

            etDate!!.error = null
        }


}

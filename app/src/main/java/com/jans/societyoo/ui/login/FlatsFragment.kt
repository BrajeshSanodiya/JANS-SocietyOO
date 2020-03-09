package com.jans.societyoo.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jans.societyoo.R
import com.jans.societyoo.model.login.FlatDetail
import com.jans.societyoo.model.login.UserDetail
import com.jans.societyoo.viewmodel.LoginViewModel
import com.jans.societyoo.viewmodel.LoginViewModelFactory


/**
 * A simple [Fragment] subclass.
 */
class FlatsFragment : Fragment() {
    private lateinit var loginViewModel: LoginViewModel
    var flats: List<FlatDetail>? = null
    var userDetail:UserDetail?=null
    var rgFlats:RadioGroup?=null
    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_flats, container, false)
        rgFlats = rootView.findViewById(R.id.rgFlats_Flats)
        var btnNext:Button=rootView.findViewById(R.id.btnNext_Flats)

        loginViewModel = ViewModelProvider(
            activity!!.viewModelStore,
            LoginViewModelFactory()
        ).get(LoginViewModel::class.java)
        loginViewModel.loginFlatViewState.observe(viewLifecycleOwner, Observer {
            val result = it
            if(result.flats!=null && result.flats.size>0)
            {
                flats=result.flats
                showFlats()
            }
            if(result.userDetail!=null){
                userDetail=result.userDetail
            }
            btnNext.isEnabled=result.isItemChecked
        })
        btnNext.setOnClickListener {
            //var loginFlatViewState:LoginFlatsViewState= loginViewModel.loginFlatViewState.value!!
            //PrintMsg.toastDebug(context,loginFlatViewState.toString());
            if (userDetail == null || userDetail!!.userProfileId==0)
                loginViewModel.loginFragmentChanged(LoginFragmentState.USER_PROFILE)
            else
                loginViewModel.openAfterLoginScreen()
        }

        rgFlats!!.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                loginViewModel.setFlatsConfirm(flats!!.get(checkedId).flatId,true)
            }
        })
        return rootView
    }

    fun showFlats(){
        if(flats!=null && rgFlats!=null ){
            rgFlats!!.removeAllViews()
            var index:Int=0
            for (flat in flats!!)
            {
                val radioButton = RadioButton(context)
                radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_flat,0)
                val lp: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                lp.setMargins(0,20,0,20)
                radioButton.id=index
                radioButton.tag=flat.flatId
                radioButton.layoutParams = lp
                radioButton.text = "Flat No. "+flat.flatNu+", "+flat.flatFloorNu+" Floor, "+flat.towerName+" Tower, "+flat.societyName+", "+flat.societyAddress+", "+flat.societyCity
                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,16.0f)
                index++
                rgFlats!!.addView(radioButton)
            }
        }
    }
}

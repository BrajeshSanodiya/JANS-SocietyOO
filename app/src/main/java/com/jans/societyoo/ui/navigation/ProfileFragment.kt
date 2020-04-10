package com.jans.societyoo.ui.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.jans.roomexplorer.RoomExplorer
import com.jans.societyoo.R
import com.jans.societyoo.data.local.db.DatabaseInstance
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.ui.login.FlatsFragment
import com.jans.societyoo.ui.login.LoginActivity
import com.jans.societyoo.ui.services.ProviderPostActivity
import com.jans.societyoo.ui.services.ProviderPostFragment
import com.jans.societyoo.utils.PrintMsg
import com.jans.tracking.PropertyName
import com.jans.tracking.Tracking
import com.jans.tracking.TrackingOptions
import kotlinx.android.synthetic.main.fragment_profile.view.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    lateinit var preferences: UserPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = UserPreferences(requireContext())
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.btnPostProvider_profile.setOnClickListener {
            /*var intent= Intent(context, ProviderPostActivity::class.java)
            requireContext().startActivity(intent)*/
            Navigation.findNavController(it).navigate(R.id.action_profileFragment_to_providerPostFragment)

        }

        view.btnDefaultFlat_profile.setOnClickListener{
            val args = FlatsFragment.bundleArgs(false)
            Navigation.findNavController(it).navigate(R.id.action_profileFragment_to_flatsFragment,args)
        }

        view.btnUpdateProfile_profile.setOnClickListener{
            val args = FlatsFragment.bundleArgs(false)
            Navigation.findNavController(it).navigate(R.id.action_profileFragment_to_userProfileFragment,args)
        }

        view.btnDeleteShared_profile.setOnClickListener{
            UserPreferences::mobileNum.set(preferences,"")
            UserPreferences::appOpenFirstTime.set(preferences,false)
            UserPreferences::defaultFlatId.set(preferences,0)
            UserPreferences::defaultUserId.set(preferences,0)
            PrintMsg.toast(requireContext(),"LogOut Successfully! Kill the app and Open again..")
        }

        view.btnDatabase_profile.setOnClickListener{
            RoomExplorer.show(requireContext(), DatabaseInstance::class.java, DatabaseInstance.DB_NAME)
        }

        view.btnSaveShared_profile.setOnClickListener{
            UserPreferences::userName.set(preferences,view.etSaveShared_profile.text.toString().trim());
            UserPreferences::emailAccount.set(preferences,"sanodiya.brajesh@gmail.com");
            PrintMsg.toast(requireContext(),"Preference Saved Successfully!")
            view.etSaveShared_profile.setText("")
        }

        view.btnShowShared_profile.setOnClickListener{
            view.tvShowShared_profile.setText(preferences.userName.toString())
            println(preferences.emailAccount)
        }

        view.btnSendTracking_profile.setOnClickListener{
            // Tracking
            val trackMap = mutableMapOf<String, String>()
            trackMap[PropertyName.APP_NAME] = "JANS-SocietyOO"
            Tracking.trackEvent("app_open", trackMap,
                superPropertyMap = mapOf(Pair(PropertyName.LOGIN_STATUS, "Non Logged In")),
                trackRequestOption = TrackingOptions(firebase = true, comscore = false)
            )
            PrintMsg.toastDebug(requireContext(),"Tracking Send Successfully!")
        }

        view.btnLogin_profile.setOnClickListener{
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

    }


}

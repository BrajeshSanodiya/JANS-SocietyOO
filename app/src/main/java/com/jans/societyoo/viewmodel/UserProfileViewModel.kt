package com.jans.societyoo.viewmodel


import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.jans.societyoo.data.repository.LoginRepository
import com.jans.societyoo.model.login.FlatDetail
import com.jans.societyoo.model.login.OtpRequest
import com.jans.societyoo.model.login.UserDetail
import com.jans.societyoo.ui.login.LoginUserProfileViewState
import com.jans.societyoo.utils.PrintMsg
import java.text.SimpleDateFormat
import java.util.*

public class UserProfileViewModel : ViewModel() {

    val userProfileViewState=MutableLiveData<LoginUserProfileViewState>()

    private val loginRepository: LoginRepository = LoginRepository();

    // A placeholder username validation check
    fun checkEmail(email:String) {
        if (email.contains('@') && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userProfileViewState.value= LoginUserProfileViewState(validName= validName(),validEmail= true,validGender = validGender(),validDOB = validDOB(),errorName= errorName(),errorEmail= false,errorGender = errorGender(),errorDOB = errorDOB())
        } else {
            userProfileViewState.value= LoginUserProfileViewState(validName= validName(),validEmail= false,validGender = validGender(),validDOB = validDOB(),errorName=errorName(),errorEmail= true)
        }
    }
    fun checkName(name:String) {
        if(name.length > 5) {
            userProfileViewState.value= LoginUserProfileViewState(validName = true,validEmail= validEmail(),validGender = validGender(),validDOB = validDOB(),errorName= false,errorEmail= errorEmail(),errorGender = errorGender(),errorDOB = errorDOB())
        }else{
            userProfileViewState.value= LoginUserProfileViewState(validName = false,validEmail= validEmail(),validGender = validGender(),validDOB = validDOB(),errorName= true,errorEmail= errorEmail(),errorGender = errorGender(),errorDOB = errorDOB())
        }
    }

    fun checkGender(name:String) {
        if(name.length > 5) {
            userProfileViewState.value= LoginUserProfileViewState(validName = validName(),validEmail= validEmail(),validGender = validGender(),validDOB = validDOB(),errorName= errorName(),errorEmail= errorEmail(),errorGender = errorGender(),errorDOB = errorDOB())

        }else{
            userProfileViewState.value= LoginUserProfileViewState(validName = validName(),validEmail= validEmail(),validGender = validGender(),validDOB = validDOB(),errorName= errorName(),errorEmail= errorEmail(),errorGender = errorGender(),errorDOB = errorDOB())
        }
    }

    fun checkDOB(dob:String):Boolean {
        if(dob.length > 5) {
            if(compareDates(dob)){
                userProfileViewState.value= LoginUserProfileViewState(validName = validName(),validEmail= validEmail(),validGender = validGender(),validDOB = true,errorName= errorName(),errorEmail= errorEmail(),errorGender = errorGender(),errorDOB = false)
                return true
            }
            else{
                userProfileViewState.value= LoginUserProfileViewState(validName = validName(),validEmail= validEmail(),validGender = validGender(),validDOB = false,errorName= errorName(),errorEmail= errorEmail(),errorGender = errorGender(),errorDOB = true)
                return false
            }
        }else{
            userProfileViewState.value= LoginUserProfileViewState(validName = validName(),validEmail= validEmail(),validGender = validGender(),validDOB = false,errorName= errorName(),errorEmail= errorEmail(),errorGender = errorGender(),errorDOB = true)
            return false
        }
    }

    fun updateProfile(userDetail: UserDetail) = liveData {
        val result = loginRepository.updateUserProfile(userDetail)
        PrintMsg.println("API Response : updateProfile : ${result.toString()}")
        emit(result)
    }

    fun compareDates(givenDateString: String?): Boolean {
        val format = SimpleDateFormat("dd-MM-yyyy")
        var True = false
        try {
            val givenDate: Date = format.parse(givenDateString)
            val currentDateString = format.format(Date())
            val currentDate: Date = format.parse(currentDateString)

            if (givenDate.after(currentDate)) {
                True = false
            }
            if (givenDate.before(currentDate)) {
                True = true
            }
            if (givenDate.equals(currentDate)) {
                True = false
            }
        } catch (e: Exception) {
           PrintMsg.println("ERROR! - comparing DATES "+ e.toString())
        }
        return True
    }

    private fun validEmail():Boolean{
        if(userProfileViewState.value!=null)
            return userProfileViewState.value!!.validEmail
        else
            return  false
    }

    private fun validName():Boolean{
        if(userProfileViewState.value!=null)
            return userProfileViewState.value!!.validName
        else
            return false
    }

    private fun validGender():Boolean{
        if(userProfileViewState.value!=null)
            return userProfileViewState.value!!.validGender
        else
            return false
    }

    private fun validDOB():Boolean{
        if(userProfileViewState.value!=null)
            return userProfileViewState.value!!.validDOB
        else
            return false
    }

    private fun errorName():Boolean{
        if(userProfileViewState.value!=null)
            return userProfileViewState.value!!.errorName
        else
            return  false
    }

    private fun errorEmail():Boolean{
        if(userProfileViewState.value!=null)
            return userProfileViewState.value!!.errorEmail
        else
            return  false
    }
    private fun errorGender():Boolean{
        if(userProfileViewState.value!=null)
            return userProfileViewState.value!!.errorGender
        return  false
    }
    private fun errorDOB():Boolean{
        if(userProfileViewState.value!=null)
            return userProfileViewState.value!!.errorDOB
        else
            return  false
    }

}
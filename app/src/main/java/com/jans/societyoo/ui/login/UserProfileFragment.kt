package com.jans.societyoo.ui.login

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jans.societyoo.R
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class UserProfileFragment : Fragment() {
    private var year = 0
    private var month = 0
    private var day = 0
    private var etDate: TextView? = null
   // private var dpResult: DatePicker? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView= inflater.inflate(R.layout.fragment_user_profile, container, false)
        etDate=rootView.findViewById(R.id.etDate_UserProfile)
        //dpResult = rootView.findViewById(R.id.dpResult) as DatePicker
        setCurrentDateOnView()
        addListenerOnButton()
        return rootView
    }

    // display current date
    fun setCurrentDateOnView() {
        val c: Calendar = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)
        // set current date into textview
       /* etDate!!.setText(
            StringBuilder() // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" ")
        )*/
        // set current date into datepicker
       // dpResult!!.init(year, month, day, null)
    }

    fun addListenerOnButton() {
        etDate!!.setOnClickListener{
            onCreateDialog()
        }
    }

    private fun onCreateDialog() {
       var datePicker:DatePickerDialog= DatePickerDialog(
            context!!, datePickerListener,
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

            // set selected date into textview
            etDate!!.setText(
                StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" ")
            )

            // set selected date into datepicker also
            //dpResult!!.init(year, month, day, null)
        }

}

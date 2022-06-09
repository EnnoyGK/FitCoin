package com.example.pedometerappkt.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.pedometerappkt.R
import com.example.pedometerappkt.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.pedometerappkt.other.Constants.KEY_NAME
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.login.etLogin
import kotlinx.android.synthetic.main.login.btnApplyChanges
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.main.*
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment:Fragment(R.layout.login) {

    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject
    var isFirstAppOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!isFirstAppOpen){
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment2, true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment2_to_trackingFragment2,
                savedInstanceState,
                navOptions
            )
        }

        btnApplyChanges.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if(success){
                findNavController().navigate(R.id.action_setupFragment2_to_trackingFragment2)

            }else{
                Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_SHORT).show()
            }
        }

        tvRegister.setOnClickListener {

            findNavController().navigate(R.id.action_setupFragment2_to_registerFragment)

        }
    }


    private fun writePersonalDataToSharedPref(): Boolean {
        val name = etLogin.text.toString()

        if(name.isEmpty()){
           return false
        }
        sharedPref.edit()
            .putString(KEY_NAME, name)
            //.putFloat(KEY_WEIGHT, weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply()
        //val toolbarText = "Let's go, $name!"
        //requireActivity().tvToolbarTitle.text = toolbarText
        return true
    }
}



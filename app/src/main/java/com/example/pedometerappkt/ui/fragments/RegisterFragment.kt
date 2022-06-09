package com.example.pedometerappkt.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pedometerappkt.R
import com.example.pedometerappkt.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.pedometerappkt.other.Constants.KEY_NAME
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_setup.etLogin
import kotlinx.android.synthetic.main.login.tvRegister
import kotlinx.android.synthetic.main.register.*
import javax.inject.Inject

class RegisterFragment:Fragment(R.layout.register) {

    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject
    var isFirstAppOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*if(!isFirstAppOpen){
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment2_to_trackingFragment2,
                savedInstanceState,
                navOptions
            )
        }*/

        tvRegister.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if(success){
                findNavController().navigate(R.id.action_registerFragment_to_trackingFragment2)

            }else{
                Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    private fun writePersonalDataToSharedPref(): Boolean {
        val login = etLogin.text.toString()
        val email = etEmail.text.toString()
        val name = etName.text.toString()
        val pwd = etPwd.text.toString()
        val repeatPwd = etRepeatPwd.text.toString()

        if(login.isEmpty() || email.isEmpty() || name.isEmpty() || pwd.isEmpty()
            || repeatPwd.isEmpty() || pwd != repeatPwd){
           return false
        }

        sharedPref.edit()
            .putString(KEY_NAME, login)
            //.putFloat(KEY_WEIGHT, weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply()
        //val toolbarText = "Let's go, $name!"
        //requireActivity().tvToolbarTitle.text = toolbarText
        return true
    }
}



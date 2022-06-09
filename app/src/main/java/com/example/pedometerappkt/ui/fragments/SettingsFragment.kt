package com.example.pedometerappkt.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pedometerappkt.R
import com.example.pedometerappkt.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.pedometerappkt.other.Constants.KEY_NAME
import com.example.pedometerappkt.other.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.profile.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment:Fragment(R.layout.profile) {



    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFieldsFromSharedPref()
        btnApplyChanges.setOnClickListener {
            val success = applyChangesToSharedPref()
            if(success){
                Snackbar.make(view, "Saved changes", Snackbar.LENGTH_LONG).show()
            }else{
                Snackbar.make(view, "Please fill out all the fields", Snackbar.LENGTH_LONG).show()
            }
        }
        btnLogOut.setOnClickListener {
            logout()
            findNavController().navigate(R.id.action_settingsFragment2_to_setupFragment2)
        }
    }

    private fun loadFieldsFromSharedPref(){
        val name = sharedPreferences.getString(KEY_NAME, "")
        //val weight = sharedPreferences.getFloat(KEY_WEIGHT, 80f)
        //tvProfileName.setText(name)
        //etWeight.setText(weight.toString())
    }

    private fun applyChangesToSharedPref(): Boolean{
        val weightText = etWeight.text.toString()
        //val weightText = etWeight.text.toString()
        if(weightText.isEmpty()){
            return false
        }
        sharedPreferences.edit()
            //.putString(KEY_NAME, nameText)
            .putFloat(KEY_WEIGHT, weightText.toFloat())
            .apply()
        //val toolbarText = "Let's go $nameText!"
        //requireActivity().tvToolbarTitle.text = toolbarText
        return true
    }

    private fun logout(){
        sharedPreferences.edit()
            .putBoolean(KEY_FIRST_TIME_TOGGLE, true)
            .apply()
    }
}
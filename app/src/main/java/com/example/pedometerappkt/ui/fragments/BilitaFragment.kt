package com.example.pedometerappkt.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.pedometerappkt.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.catalog_item_sportmaster.*

class BilitaFragment: Fragment(R.layout.catalog_item_belita) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnBuy.setOnClickListener(){
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog(){
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Купить промокод?")
            .setMessage("Вы уверены, что хотите продолжить и купить промокод?")
            .setPositiveButton("Да"){_, _ ->
                showSuccessDialog()
            }
            .setNegativeButton("Нет"){ dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }

    private fun showSuccessDialog(){
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Поздравляем!")
            .setMessage("Ваш промокод:\nPROMO")
            .setNegativeButton("Ок"){ dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }
}
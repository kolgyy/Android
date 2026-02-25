package com.korotaev.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.NavOptions

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editInput = view.findViewById<EditText>(R.id.edit_input)
        val editPassword = view.findViewById<EditText>(R.id.edit_password)
        val buttonSignIn = view.findViewById<Button>(R.id.button_signIn)
        val checkBox = view.findViewById<CheckBox>(R.id.check_isAutomatically)

        sharedPreferences = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)

        buttonSignIn.setOnClickListener {
            val login = editInput.text.toString()
            val password = editPassword.text.toString()

            if (login.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val savedLogin = sharedPreferences.getString("login", null)
            val savedPassword = sharedPreferences.getString("password", null)

            if (login == savedLogin && password == savedPassword) {
                // Сохраняем автологин
                sharedPreferences.edit().putBoolean("isChecked", checkBox.isChecked).apply()

                // Навигация на контент с очисткой back stack
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true) // удаляем loginFragment из back stack
                    .build()

                findNavController().navigate(R.id.oneFragment, null, navOptions)

            } else {
                Toast.makeText(requireContext(), "Данные неверны", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
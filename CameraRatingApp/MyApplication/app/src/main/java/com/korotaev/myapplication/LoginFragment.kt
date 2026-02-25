package com.korotaev.myapplication

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editInput = view.findViewById<EditText>(R.id.edit_input)
        val editPassword = view.findViewById<EditText>(R.id.edit_password)
        val buttonSignIn = view.findViewById<Button>(R.id.button_signIn)
        val checkBox = view.findViewById<CheckBox>(R.id.check_isAutomatically)
        val prefs = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)

        auth = FirebaseAuth.getInstance()

        buttonSignIn.setOnClickListener {
            val login = editInput.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (login.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Используем OnSuccessListener / OnFailureListener
            auth.signInWithEmailAndPassword(login, password)
                .addOnSuccessListener {
                    // Успешная авторизация
                    prefs.edit()
                        .putBoolean("isLoggedIn", true)
                        .putBoolean("isAutoLogin", checkBox.isChecked)
                        .apply()

                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.loginFragment, true) // очистка back stack
                        .build()
                    findNavController().navigate(R.id.oneFragment, null, navOptions)
                }
                .addOnFailureListener { exception ->
                    // Cообщения об ошибке
                    val message = when (exception) {
                        is FirebaseAuthInvalidCredentialsException -> "Неверный пароль"
                        is FirebaseAuthInvalidUserException -> "Пользователь не найден"
                        else -> exception.localizedMessage ?: "Ошибка входа"
                    }
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
        }
    }
}
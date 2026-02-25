package com.korotaev.myapplication

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private lateinit var buttonByNumber: Button
    private lateinit var buttonByEmail: Button
    private lateinit var editInput: EditText
    private lateinit var editPassword: EditText
    private lateinit var editRepeatPassword: EditText
    private lateinit var buttonRegister: Button
    private var isByNumber = false

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val prefs = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)

        buttonByNumber = view.findViewById(R.id.button_byNumber)
        buttonByEmail = view.findViewById(R.id.button_byEmail)
        editInput = view.findViewById(R.id.edit_input)
        editPassword = view.findViewById(R.id.edit_password)
        editRepeatPassword = view.findViewById(R.id.edit_repeatPassword)
        buttonRegister = view.findViewById(R.id.button_Register)

        selectEmailMode()

        buttonByNumber.setOnClickListener { selectNumberMode() }
        buttonByEmail.setOnClickListener { selectEmailMode() }

        buttonRegister.setOnClickListener {
            val inputText = editInput.text.toString().trim()
            val password = editPassword.text.toString().trim()
            val repeatPassword = editRepeatPassword.text.toString().trim()

            // Валидация
            if (isByNumber) {
                if (!inputText.startsWith("+")) {
                    Toast.makeText(requireContext(), "Телефон должен начинаться с +", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else {
                if (!inputText.contains("@")) {
                    Toast.makeText(requireContext(), "Некорректный email", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            if (password.length < 8) {
                Toast.makeText(requireContext(), "Пароль должен быть минимум 8 символов", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != repeatPassword) {
                Toast.makeText(requireContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Создание пользователя в Firebase
            if (!isByNumber) {
                auth.createUserWithEmailAndPassword(inputText, password)
                    .addOnSuccessListener {
                        // Сохраняем локально статус регистрации и автологин
                        prefs.edit()
                            .putBoolean("isRegistered", true)
                            .putBoolean("isAutoLogin", true)
                            .apply()

                        // Навигация на контент и очистка back stack
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.registrationFragment, true)
                            .build()
                        findNavController().navigate(R.id.oneFragment, null, navOptions)
                    }
                    .addOnFailureListener { exception ->
                        val message = when (exception) {
                            is FirebaseAuthUserCollisionException -> "Пользователь с таким email уже существует"
                            is FirebaseAuthInvalidCredentialsException -> "Некорректный email"
                            is FirebaseNetworkException -> "Ошибка сети. Проверьте интернет"
                            else -> "Ошибка регистрации: ${exception.localizedMessage}"
                        }
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Регистрация по номеру пока не реализована", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectNumberMode() {
        isByNumber = true
        editInput.text.clear()
        buttonByNumber.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        buttonByNumber.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_light))
        buttonByEmail.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
        buttonByEmail.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
        editInput.hint = "Введите номер"
        editInput.inputType = InputType.TYPE_CLASS_PHONE
    }

    private fun selectEmailMode() {
        isByNumber = false
        editInput.text.clear()
        buttonByEmail.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        buttonByEmail.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_light))
        buttonByNumber.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
        buttonByNumber.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
        editInput.hint = "Введите email"
        editInput.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }
}